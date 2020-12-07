package models.keyboards;

import handlers.ChoosePortfolioHandler;
import handlers.PortfolioStatisticHandler;
import handlers.MarketOperationHandler;
import handlers.SearchAssetHandler;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.List;

public class Keyboard {
    public static ResponseKeyboard getMenuKeyboard() {
        List<String> firstRow = List.of("\uD83D\uDCBCПосмотреть портфель\uD83D\uDCBC",
                "❌Сбросить портфель❌");
        List<String> secondRow = List.of("\uD83D\uDD0EНайти актив\uD83D\uDD0D",
                "\uD83D\uDCC8Показать статистику\uD83D\uDCC8");
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

    public static InlineKeyboard getPaginationKeyboard(String oneOf) {
        InlineButtonInfo previousInfo = new InlineButtonInfo("Previous", PortfolioStatisticHandler.PREVIOUS);
        InlineButtonInfo oneOfInfo = new InlineButtonInfo(oneOf, PortfolioStatisticHandler.STAY);
        InlineButtonInfo nextInfo = new InlineButtonInfo("Next", PortfolioStatisticHandler.NEXT);
        List<InlineButtonInfo> keyboardInfo = List.of(previousInfo, oneOfInfo, nextInfo);
        return new InlineKeyboard(List.of(keyboardInfo));
    }

    public static InlineKeyboard getBuySellKeyboard() {
        InlineButtonInfo buy = new InlineButtonInfo(
                "Купить",
                MarketOperationHandler.BUY);
        InlineButtonInfo sell = new InlineButtonInfo(
                "Продать",
                MarketOperationHandler.SELL);
        List<InlineButtonInfo> keyboardInfo = List.of(buy, sell);
        return new InlineKeyboard(List.of(keyboardInfo));
    }

    public static InlineKeyboard getBuyKeyboard() {
        InlineButtonInfo increase = new InlineButtonInfo(
                "+ 1",
                MarketOperationHandler.INCREASE_BUY);
        InlineButtonInfo acceptBuying = new InlineButtonInfo(
                "Подтвердить покупку",
                MarketOperationHandler.ACCEPT_BUY);
        List<InlineButtonInfo> keyboardInfo = List.of(increase, acceptBuying);
        return new InlineKeyboard(List.of(keyboardInfo));
    }

    public static InlineKeyboard getSellKeyboard() {
        InlineButtonInfo increase = new InlineButtonInfo(
                "+ 1",
                MarketOperationHandler.INCREASE_SELL);
        InlineButtonInfo acceptSelling = new InlineButtonInfo(
                "Подтвердить продажу",
                MarketOperationHandler.ACCEPT_SELL);
        List<InlineButtonInfo> keyboardInfo = List.of(increase, acceptSelling);
        return new InlineKeyboard(List.of(keyboardInfo));
    }
}
