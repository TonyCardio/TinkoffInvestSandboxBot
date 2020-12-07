package wrappers;

import models.keyboards.InlineKeyboard;
import models.keyboards.KeyboardType;
import models.keyboards.KeyboardsConverter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class EditMessageResponse implements ResponseMessage {
    private final String message;
    private final long chatId;
    private final Integer messageId;
    private final KeyboardType keyboardType;
    private final InlineKeyboard inlineKeyboard;

    //region Property

    public String getMessage() {
        return message;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public long getChatId() {
        return chatId;
    }

    //endregion

    public EditMessageResponse(long chatId, String message, Integer messageId, InlineKeyboard inlineKeyboard) {
        this.message = message;
        this.chatId = chatId;
        this.messageId = messageId;
        this.keyboardType = KeyboardType.InLine;
        this.inlineKeyboard = inlineKeyboard;
    }


    @Override
    public BotApiMethod createMessage() {
        EditMessageText messageText = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(messageId)
                .setText(message);
        if (keyboardType.equals(KeyboardType.InLine))
            messageText.setReplyMarkup(KeyboardsConverter.getInlineKeyboardMarkup(inlineKeyboard));
        return messageText;

    }
}
