package models.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardsConverter {

    public ReplyKeyboard getReplyKeyboardMarkup(ResponseKeyboard keyboard) {
        List<KeyboardRow> rows = getKeyboardRows(keyboard.getKeyboardRows());
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup();
        replyKeyboard.setResizeKeyboard(true).setKeyboard(rows);
        return replyKeyboard;
    }

    public static InlineKeyboardMarkup getInlineKeyboardMarkup(InlineKeyboard keyboard) {
        List<List<InlineKeyboardButton>> rows = getInlineKeyboardRows(keyboard.getKeyboardRows());
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        return inlineKeyboard.setKeyboard(rows);
    }

    private static List<KeyboardRow> getKeyboardRows(List<List<String>> keyboardTexts) {
        List<KeyboardRow> rowList = new ArrayList<>();

        for (List<String> keyboardText : keyboardTexts) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (String buttonText : keyboardText) {
                KeyboardButton button = new KeyboardButton().setText(buttonText);
                keyboardRow.add(button);
            }
            rowList.add(keyboardRow);
        }

        return rowList;
    }

    private static List<List<InlineKeyboardButton>> getInlineKeyboardRows(List<List<InlineButtonInfo>> keyboardInfo) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (List<InlineButtonInfo> keyboardText : keyboardInfo) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            for (InlineButtonInfo buttonInfo : keyboardText) {
                InlineKeyboardButton button = new InlineKeyboardButton()
                        .setText(buttonInfo.getButtonText())
                        .setCallbackData(buttonInfo.getCallBackData());
                keyboardRow.add(button);
            }
            rowList.add(keyboardRow);
        }

        return rowList;
    }
}
