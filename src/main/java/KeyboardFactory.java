import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {
    public static ReplyKeyboardMarkup makeReplyKeyboard(List<List<String>> keyboardTexts){
        ArrayList<KeyboardRow> rows = getKeyboardRows(keyboardTexts);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true).setKeyboard(rows);
        return keyboard;
    }

    public static InlineKeyboardMarkup makeInlineKeyboard(List<List<InlineButtonInfo>> keyboardInfo){
        List<List<InlineKeyboardButton>> rows = getInlineKeyboardRows(keyboardInfo);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        return keyboard.setKeyboard(rows);
    }

    private static ArrayList<KeyboardRow> getKeyboardRows(List<List<String>> keyboardTexts){
        ArrayList<KeyboardRow> rowList = new ArrayList<>();
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

    private static List<List<InlineKeyboardButton>> getInlineKeyboardRows(List<List<InlineButtonInfo>> keyboardInfo){
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
