package models;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

public interface Handler {
    List<SendMessage> handle(User user, String message);

    State handledState();

    List<String> handledCallBackQuery();
}
