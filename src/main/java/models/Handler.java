package models;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import wrappers.ResponseMessage;
import wrappers.UpdateWrapper;

import java.util.List;

public interface Handler {
    List<ResponseMessage> handleMessage(User user, UpdateWrapper message);

    List<ResponseMessage> handleCallbackQuery(User user, UpdateWrapper callbackQuery);

    State handledState();

    List<String> handledCallBackQuery();
}
