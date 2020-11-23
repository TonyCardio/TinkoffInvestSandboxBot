package wrappers;

import org.telegram.telegrambots.meta.api.objects.Update;

public class UpdateWrapper {
    private long chatId;
    private String messageData;
    private Integer messageId;
    private boolean isCallBackQuery;

    //region Property
    public boolean isCallBackQuery() {
        return isCallBackQuery;
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

    //endregion

    public UpdateWrapper(Update update) {
        if (update.hasCallbackQuery()) {
            isCallBackQuery = true;
            chatId = update.getCallbackQuery().getFrom().getId();
            messageData = update.getCallbackQuery().getData();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            messageData = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
        }
    }
}
