package handlers;

import models.Handler;

import models.State;
import models.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Collections;
import java.util.List;

public class SearchAssetHandler implements Handler {
    @Override
    public List<SendMessage> handle(User user, String message) {
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
