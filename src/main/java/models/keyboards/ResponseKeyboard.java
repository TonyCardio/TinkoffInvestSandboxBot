package models.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public interface ResponseKeyboard {
    public ReplyKeyboard convertToReplyKeyboard();
}
