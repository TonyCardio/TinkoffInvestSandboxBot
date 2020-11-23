package wrappers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public class EditMessageResponse implements ResponseMessage {
    private final String message;
    private final long chatId;
    private final long messageId;

    public EditMessageResponse(String message, long chatId, long messageId) {
        this.message = message;
        this.chatId = chatId;
        this.messageId = messageId;
    }

    @Override
    public BotApiMethod createMessage() {
        return null;
    }
}
