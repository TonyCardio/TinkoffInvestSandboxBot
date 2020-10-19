package models;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface Handler {
    List<BotApiMethod> handleMessage(User user, Message message);

    List<BotApiMethod> handleCallbackQuery(User user, CallbackQuery callbackQuery);

    State handledState();

    List<String> handledCallBackQuery();
}
