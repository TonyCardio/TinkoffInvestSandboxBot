package handlers;

import models.Handler;
import models.State;
import models.User;
import wrappers.ResponseMessage;
import wrappers.SimpleMessageResponse;
import wrappers.WrappedUpdate;

import java.util.Collections;
import java.util.List;

public class StartHandler implements Handler {
    @Override
    public List<ResponseMessage> handleMessage(User user, WrappedUpdate wrapper) {
        String startText = "*Привет! Я InvestBot*\n" +
                "Я помогу тебе улучшить твои навыки в инвестировании и трейдинге" +
                "Для начала введи свой api-ключ для Tinkoff песочницы.\n" +
                "Успехов! \n\n" +
                "*Тут помощь* - /help";
        SimpleMessageResponse startMessage = new SimpleMessageResponse(user.getChatId(), startText);
        startMessage.enableMarkdown();
        SimpleMessageResponse authMessage = new SimpleMessageResponse(user.getChatId(), "Введите свой токен");
        user.setState(State.NON_AUTHORIZED);
        user.setLastQueryTime();
        return List.of(startMessage, authMessage);
    }

    @Override
    public List<ResponseMessage> handleCallbackQuery(User user, WrappedUpdate wrapper) {
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
