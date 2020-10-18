package models;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UpdateReceiver {
    private final List<Handler> handlers;
    private final ConcurrentHashMap<Long, User> chatIdToUser;
    private static String helpers = "*Я здесь чтобы помочь тебе*\n\n" +
            "*Как получить токен?*\n" +
            "1.Зайдите в свой аккаунт на tinkoff.ru\n" +
            "2.Перейдите в раздел инвестиций\n" +
            "3.Перейдите в настройки\n" +
            "4.Функция \"Подтверждение сделок кодом\" должна быть отключена\n" +
            "5.Выпустите токен OpenAPI для Sandbox. " +
            "Возможно, система попросит вас авторизоваться еще раз. " +
            "Не беспокойтесь, это необходимо для подключения.\n" +
            "6.Скопируйте и сохраните токен. Вводите его в диалоге с ботом при авторизации." +
            "Токен отображается только один раз, просмотреть его позже не получится." +
            "Тем не менее вы можете выпускать неограниченное количество токенов.";

    public UpdateReceiver(List<Handler> handlers) {
        this.handlers = handlers;
        chatIdToUser = new ConcurrentHashMap<Long, User>();
    }

    public List<SendMessage> handle(Update update) {
        long chatId;
        if (isUpdateWithText(update))
            chatId = update.getMessage().getChatId();
        else
            chatId = update.getCallbackQuery().getFrom().getId();

        if (!chatIdToUser.containsKey(chatId))
            chatIdToUser.put(chatId, new User(chatId));
        User user = chatIdToUser.get(chatId);

        long timeFromLastQuery = new Date().getTime() - user.getLastQueryTime();
        if (timeFromLastQuery > 1.8e+6 && !user.getState().equals(State.NON_AUTHORIZED))
            user.setState(State.NON_AUTHORIZED);

        try {
            if (isUpdateWithText(update)) {
                final Message message = update.getMessage();
                if (message.getText().equals("/start") && !user.getState().equals(State.NONE))
                    user.setState(State.NONE);
                return getHandlerByState(user.getState()).handle(user, message);
            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                return getHandlerByCallBackQuery(callbackQuery.getData())
                        .handleCallbackQuery(user, callbackQuery);
            }
            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByState(State state) {
        return handlers.stream()
                .filter(handler -> handler.handledState() != null)
                .filter(handler -> handler.handledState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(h -> h.handledCallBackQuery().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    public static List<SendMessage> handleHelp(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        SendMessage helpMessage = new SendMessage(chatId, helpers);
        return List.of(helpMessage);
    }

    private boolean isUpdateWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
