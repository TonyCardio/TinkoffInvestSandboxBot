package models.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public class Keyboard {
    public static ReplyKeyboardMarkup getMenuKeyboard() {
        List<String> firstRow = List.of("\uD83D\uDCB5Добавить валюту\uD83D\uDCB5",
                "\uD83D\uDCBCПосмотреть портфель\uD83D\uDCBC");
        List<String> secondRow = List.of("❌Сбросить портфель❌",
                "\uD83D\uDD0EНайти актив\uD83D\uDD0D");
        List<List<String>> rows = List.of(firstRow, secondRow);
        return KeyboardFactory.makeReplyKeyboard(rows);
    }

    public static InlineKeyboardMarkup getYesNoKeyboard() {
        InlineButtonInfo yesButtonInfo = new InlineButtonInfo("ДА", "YES");
        InlineButtonInfo noButtonInfo = new InlineButtonInfo("НЕТ", "NO");
        List<InlineButtonInfo> infoList = List.of(yesButtonInfo, noButtonInfo);
        return KeyboardFactory.makeInlineKeyboard(List.of(infoList));
    }
}
