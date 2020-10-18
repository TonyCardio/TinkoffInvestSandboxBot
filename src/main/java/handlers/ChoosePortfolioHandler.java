package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tinkoff.invest.openapi.SandboxContext;
import ru.tinkoff.invest.openapi.models.Currency;
import ru.tinkoff.invest.openapi.models.sandbox.CurrencyBalance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ChoosePortfolioHandler implements Handler {
    public static final String CREATE_NEW_PORTFOLIO = "/create_new";
    public static final String CONTINUE_WITH_OLD_PORTFOLIO = "/continue";
    public static final String USD = "/USD";
    public static final String ACCEPT = "/accept";
    private static final BigDecimal addUSDStep = new BigDecimal(50);

    @Override
    public List<SendMessage> handle(User user, String message) {
        List<SendMessage> messages = new ArrayList<>();

        //TODO: replace with HashMap
        if (message.equalsIgnoreCase(CONTINUE_WITH_OLD_PORTFOLIO)) {
            messages = handleContinue(user);
        } else if (message.equalsIgnoreCase(CREATE_NEW_PORTFOLIO))
            messages = handleCreateNewPortfolio(user);
        else if (message.equalsIgnoreCase(USD))
            messages = handleAddCurrency(user, message);
        else if (message.equalsIgnoreCase(ACCEPT))
            messages = handleAccept(user);

        user.setLastQueryTime();
        return messages;
    }

    private List<SendMessage> handleContinue(User user) {
        List<SendMessage> messages = new ArrayList<>();

        messages.add(new SendMessage(user.getChatId(),
                "Вы работаете со старым портфелем")
                .setReplyMarkup(Keyboard.getMenuKeyboard()));
        user.setState(State.MAIN_MENU);
        return messages;
    }

    private List<SendMessage> handleCreateNewPortfolio(User user) {
        List<SendMessage> messages = new ArrayList<>();

        //TODO: Keyboards.getCreateNewPortfolioKeyboard
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("добавить 50$", USD),
                createInlineKeyboardButton("Готово", ACCEPT));
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        messages.add(new SendMessage(user.getChatId(),
                "Добавьте валюту или подтвердите создание портфеля")
                .setReplyMarkup(inlineKeyboardMarkup));

        return messages;
    }

    private List<SendMessage> handleAddCurrency(User user, String message) {
        user.increaseUSDAmount(addUSDStep);

        String text = String.format(
                "Количество валюты обновлено \nUSD: %s", user.getStartUSDAmount());

        //TODO: Keyboards.getCreateNewPortfolioKeyboard
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("добавить 50$", USD),
                createInlineKeyboardButton("Готово", ACCEPT));
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(
                new SendMessage(user.getChatId(), text),
                new SendMessage(user.getChatId(),
                        "Добавьте валюту или подтвердите создание портфеля")
                        .setReplyMarkup(inlineKeyboardMarkup));
    }

    private List<SendMessage> handleAccept(User user) {
        CurrencyBalance currencyBalanceUSD = new CurrencyBalance(
                Currency.USD, user.getStartUSDAmount());

        SandboxContext context = user.getApi().getSandboxContext();
        context.clearAll(null).join();
        context.setCurrencyBalance(currencyBalanceUSD, null).join();

        user.setState(State.MAIN_MENU);
        return List.of(new SendMessage(user.getChatId(),
                "Новый портфель успешно создан")
                .setReplyMarkup(Keyboard.getMenuKeyboard()));
    }

    private static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        return new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(command);
    }

    @Override
    public State handledState() {
        return State.CHOOSE_PORTFOLIO;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return List.of(CONTINUE_WITH_OLD_PORTFOLIO, CREATE_NEW_PORTFOLIO, USD, ACCEPT);
    }
}
