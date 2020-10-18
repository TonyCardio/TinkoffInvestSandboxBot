package handlers;

import models.Handler;
import models.State;
import models.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Collections;
import java.util.List;

public class StartHandler implements Handler {
    @Override
    public List<SendMessage> handle(User user, Message message) {
        String startText = "*Привет! Я InvestBot*\n" +
                "Я помогу тебе улучшить твои навыки в инвестировании и трейдинге" +
                "Для начала введи свой api-ключ для Tinkoff песочницы.\n" +
                "Успехов! \n\n" +
                "*Тут помощь* - /help";
        SendMessage startMessage = new SendMessage(user.getChatId(), startText);
        startMessage.setReplyMarkup(new ReplyKeyboardRemove());
        SendMessage authMessage = new SendMessage(user.getChatId(), "Введите свой токен");
        user.setState(State.NON_AUTHORIZED);
        user.setLastQueryTime();
        return List.of(startMessage, authMessage);
    }

    @Override
    public List<SendMessage> handleCallbackQuery(User user, CallbackQuery callbackQuery) {
        return Collections.emptyList();
    }

    @Override
    public State handledState() {
        return State.NONE;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return Collections.emptyList();
    }
}
