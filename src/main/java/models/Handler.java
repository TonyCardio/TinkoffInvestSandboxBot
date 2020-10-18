package models;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface Handler {
    List<SendMessage> handleMessage(User user, Message message);

    List<SendMessage> handleCallbackQuery(User user, CallbackQuery callbackQuery);

    State handledState();

    List<String> handledCallBackQuery();
}
