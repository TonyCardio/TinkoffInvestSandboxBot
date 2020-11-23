package models.keyboards;

import handlers.ChoosePortfolioHandler;
import handlers.SearchAssetHandler;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public class Keyboard {
    public static ResponseKeyboard getMenuKeyboard() {
        List<String> firstRow = List.of("\uD83D\uDCBCПосмотреть портфель\uD83D\uDCBC",
                "❌Сбросить портфель❌");
        List<String> secondRow = List.of("\uD83D\uDD0EНайти актив\uD83D\uDD0D");
        List<List<String>> rows = List.of(firstRow, secondRow);
        return new ResponseKeyboard(rows);
    }

    public static ResponseKeyboard getToMenuKeyboard() {
        return new ResponseKeyboard(
                List.of(List.of(SearchAssetHandler.TO_MENU)));
    }

    public static InlineKeyboard getAuthKeyboard() {
        InlineButtonInfo choosePortfolioButtonInfo = new InlineButtonInfo(
                "Продолжить со старым портфелем",
                ChoosePortfolioHandler.CONTINUE_WITH_OLD_PORTFOLIO);
        InlineButtonInfo createPortfolioButtonInfo = new InlineButtonInfo(
                "Создать новый",
                ChoosePortfolioHandler.CREATE_NEW_PORTFOLIO);
        List<InlineButtonInfo> firstRowInfo = List.of(choosePortfolioButtonInfo);
        List<InlineButtonInfo> secondRowInfo = List.of(createPortfolioButtonInfo);
        return new InlineKeyboard(List.of(firstRowInfo, secondRowInfo));
    }

    public static InlineKeyboard getAddCurrencyKeyboard() {
        InlineButtonInfo addUSDButtonInfo = new InlineButtonInfo(
                "Добавить 50$",
                ChoosePortfolioHandler.USD);
        InlineButtonInfo acceptButtonInfo = new InlineButtonInfo(
                "Готово",
                ChoosePortfolioHandler.ACCEPT);
        List<InlineButtonInfo> keyboardInfo = List.of(addUSDButtonInfo, acceptButtonInfo);
        return new InlineKeyboard(List.of(keyboardInfo));
    }
}
