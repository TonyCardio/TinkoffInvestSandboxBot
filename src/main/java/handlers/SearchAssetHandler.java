package handlers;

import models.Handler;

import models.State;
import models.User;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Collections;
import java.util.List;

public class SearchAssetHandler implements Handler {
    @Override
    public List<BotApiMethod> handleMessage(User user, Message message) {
        return Collections.emptyList();
    }

    @Override
    public List<BotApiMethod> handleCallbackQuery(User user, CallbackQuery callbackQuery) {
        return Collections.emptyList();
    }

    @Override
    public State handledState() {
        return State.SEARCH_ASSET;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return Collections.emptyList();
    }
}
