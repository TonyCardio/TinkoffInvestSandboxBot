package models;

import wrappers.ResponseMessage;
import wrappers.WrappedUpdate;

import java.util.List;

public interface Handler {
    List<ResponseMessage> handleMessage(User user, WrappedUpdate message);

    List<ResponseMessage> handleCallbackQuery(User user, WrappedUpdate callbackQuery);

    State handledState();

    List<String> handledCallBackQuery();
}
