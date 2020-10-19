package handlers;

import models.Handler;
import models.State;
import models.User;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Collections;
import java.util.List;

public class StartHandler implements Handler {
    @Override
    public List<BotApiMethod> handleMessage(User user, Message message) {
        String startText = "*Привет! Я InvestBot*\n" +
                "Я помогу тебе улучшить твои навыки в инвестировании и трейдинге" +
                "Для начала введи свой api-ключ для Tinkoff песочницы.\n" +
                "Успехов! \n\n" +
                "*Тут помощь* - /help";
        SendMessage startMessage = new SendMessage(user.getChatId(), startText);
        startMessage.setReplyMarkup(new ReplyKeyboardRemove());
        startMessage.enableMarkdown(true);
        SendMessage authMessage = new SendMessage(user.getChatId(), "Введите свой токен");
        user.setState(State.NON_AUTHORIZED);
        user.setLastQueryTime();
        return List.of(startMessage, authMessage);
    }

    @Override
    public List<BotApiMethod> handleCallbackQuery(User user, CallbackQuery callbackQuery) {
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
