package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.invest.openapi.SandboxContext;
import ru.tinkoff.invest.openapi.models.Currency;
import ru.tinkoff.invest.openapi.models.sandbox.CurrencyBalance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChoosePortfolioHandler implements Handler {
    public static final String CREATE_NEW_PORTFOLIO = "/create_new";
    public static final String CONTINUE_WITH_OLD_PORTFOLIO = "/continue";
    public static final String USD = "/USD";
    public static final String ACCEPT = "/accept";
    private static final BigDecimal addUSDStep = new BigDecimal(50);

    @Override
    public List<BotApiMethod> handleMessage(User user, Message message) {
        return Collections.emptyList();
    }

    @Override
    public List<BotApiMethod> handleCallbackQuery(User user, CallbackQuery callbackQuery) {
        String command = callbackQuery.getData();
        List<BotApiMethod> messages = new ArrayList<>();

        //TODO: replace with HashMap
        if (command.equalsIgnoreCase(CONTINUE_WITH_OLD_PORTFOLIO)) {
            messages = handleContinue(user);
        } else if (command.equalsIgnoreCase(CREATE_NEW_PORTFOLIO))
            messages = handleCreateNewPortfolio(user);
        else if (command.equalsIgnoreCase(USD))
            messages = handleAddCurrency(user, callbackQuery.getMessage());
        else if (command.equalsIgnoreCase(ACCEPT))
            messages = handleAccept(user);

        user.setLastQueryTime();
        return messages;
    }

    private List<BotApiMethod> handleContinue(User user) {
        List<BotApiMethod> messages = new ArrayList<>();

        messages.add(new SendMessage(user.getChatId(),
                "Вы работаете со старым портфелем")
                .setReplyMarkup(Keyboard.getMenuKeyboard()));
        user.setState(State.MAIN_MENU);
        return messages;
    }

    private List<BotApiMethod> handleCreateNewPortfolio(User user) {
        List<BotApiMethod> messages = new ArrayList<>();
        messages.add(new SendMessage(user.getChatId(),
                "Добавьте валюту или подтвердите создание портфеля")
                .setReplyMarkup(Keyboard.getAddCurrencyKeyboard()));
        return messages;
    }

    private List<BotApiMethod> handleAddCurrency(User user, Message message) {
        user.increaseUSDAmount(addUSDStep);

        String text = String.format("Количество валюты обновлено \nUSD: %s\n\n" +
                        "Добавьте валюту или подтвердите создание портфеля",
                user.getStartUSDAmount());
        //git push -u origin keyboard-extension
        //git merge keyboard-extension
        return List.of(new EditMessageText()
                .setChatId(user.getChatId())
                .setMessageId(message.getMessageId())
                .setText(text)
                .setReplyMarkup(Keyboard.getAddCurrencyKeyboard()));
    }

    private List<BotApiMethod> handleAccept(User user) {
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


    @Override
    public State handledState() {
        return State.CHOOSE_PORTFOLIO;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return List.of(CONTINUE_WITH_OLD_PORTFOLIO, CREATE_NEW_PORTFOLIO, USD, ACCEPT);
    }
}
