package wrappers;

import models.keyboards.KeyboardType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public class SimpleMessageResponse implements ResponseMessage {
    private final  long chatId;
    private  final String message;
    private  final KeyboardType keyboardType;

    public  SimpleMessageResponse(long chatId, String message, KeyboardType keyboardType){
        this.chatId = chatId;
        this.message = message;
        this.keyboardType = keyboardType;
    }
    @Override
    public BotApiMethod createMessage() {
        return null;
    }
}
