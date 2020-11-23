package wrappers;

import models.keyboards.InlineKeyboard;
import models.keyboards.KeyboardType;
import models.keyboards.KeyboardsConverter;
import models.keyboards.ResponseKeyboard;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class SimpleMessageResponse implements ResponseMessage {
    private final long chatId;
    private final String message;
    private final KeyboardType keyboardType;
    private final InlineKeyboard inlineKeyboard;
    private final ResponseKeyboard responseKeyboard;
    private boolean enableMarkdown;

    //region Property

    public long getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    //endregion

    public SimpleMessageResponse(long chatId, String message, InlineKeyboard inlineKeyboard) {
        this.chatId = chatId;
        this.message = message;
        this.keyboardType = KeyboardType.InLine;
        this.inlineKeyboard = inlineKeyboard;
        this.responseKeyboard = null;
    }

    public SimpleMessageResponse(long chatId, String message, ResponseKeyboard responseKeyboard) {
        this.chatId = chatId;
        this.message = message;
        this.inlineKeyboard = null;
        this.keyboardType = KeyboardType.Response;
        this.responseKeyboard = responseKeyboard;
    }

    public SimpleMessageResponse(long chatId, String message) {
        this.chatId = chatId;
        this.message = message;
        this.keyboardType = KeyboardType.None;
        this.inlineKeyboard = null;
        this.responseKeyboard = null;
    }

    public void enableMarkdown() {
        enableMarkdown = true;
    }

    @Override
    public BotApiMethod createMessage() {
        SendMessage sendMessage = new SendMessage(chatId,
                message);
        if (keyboardType.equals(KeyboardType.InLine))
            sendMessage.setReplyMarkup(KeyboardsConverter.getInlineKeyboardMarkup(inlineKeyboard));
        else if (keyboardType.equals(KeyboardType.Response))
            sendMessage.setReplyMarkup(KeyboardsConverter.getReplyKeyboardMarkup(responseKeyboard));
        sendMessage.enableMarkdown(enableMarkdown);
        return sendMessage;
    }
}
