package wrappers;

import org.telegram.telegrambots.meta.api.objects.Update;

public class WrappedUpdate {
    private long chatId;
    private String messageData;
    private String command;
    private Integer messageId;
    private boolean hasCallBackQuery;

    //region Property
    public boolean hasHasCallBackQuery() {
        return hasCallBackQuery;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public String getMessageData() {
        return messageData;
    }

    public long getChatId() {
        return chatId;
    }

    public String getCommand() { return command; }

    //endregion

    public WrappedUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            hasCallBackQuery = true;
            chatId = update.getCallbackQuery().getFrom().getId();
            command = update.getCallbackQuery().getMessage().getText();
            messageData = update.getCallbackQuery().getData();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            messageData = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
        }
    }
}
