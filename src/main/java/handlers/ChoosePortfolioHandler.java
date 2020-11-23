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
import wrappers.EditMessageResponse;
import wrappers.ResponseMessage;
import wrappers.SimpleMessageResponse;
import wrappers.UpdateWrapper;

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
    public List<ResponseMessage> handleMessage(User user, UpdateWrapper wrapper) {
        return Collections.emptyList();
    }

    @Override
    public List<ResponseMessage> handleCallbackQuery(User user, UpdateWrapper wrapper) {
        String command = wrapper.getMessageData();
        List<ResponseMessage> messages = new ArrayList<>();

        //TODO: replace with HashMap
        if (command.equalsIgnoreCase(CONTINUE_WITH_OLD_PORTFOLIO)) {
            messages = handleContinue(user);
        } else if (command.equalsIgnoreCase(CREATE_NEW_PORTFOLIO))
            messages = handleCreateNewPortfolio(user);
        else if (command.equalsIgnoreCase(USD))
            messages = handleAddCurrency(user, wrapper);
        else if (command.equalsIgnoreCase(ACCEPT))
            messages = handleAccept(user);

        user.setLastQueryTime();
        return messages;
    }

    private List<ResponseMessage> handleContinue(User user) {
        List<ResponseMessage> messages = new ArrayList<>();

        messages.add(new SimpleMessageResponse(user.getChatId(),
                "Вы работаете со старым портфелем", Keyboard.getMenuKeyboard()));
        user.setState(State.MAIN_MENU);
        return messages;
    }

    private List<ResponseMessage> handleCreateNewPortfolio(User user) {
        List<ResponseMessage> messages = new ArrayList<>();
        messages.add(new SimpleMessageResponse(user.getChatId(),
                "Добавьте валюту или подтвердите создание портфеля", Keyboard.getAddCurrencyKeyboard()));
        return messages;
    }

    private List<ResponseMessage> handleAddCurrency(User user, UpdateWrapper wrapper) {
        user.increaseUSDAmount(addUSDStep);

        String text = String.format("Количество валюты обновлено \nUSD: %s\n\n" +
                        "Добавьте валюту или подтвердите создание портфеля",
                user.getStartUSDAmount());
        return List.of(new EditMessageResponse(text,
                user.getChatId(),
                wrapper.getMessageId(),
                Keyboard.getAddCurrencyKeyboard()));
    }

    private List<ResponseMessage> handleAccept(User user) {
        CurrencyBalance currencyBalanceUSD = new CurrencyBalance(
                Currency.USD, user.getStartUSDAmount());

        SandboxContext context = user.getApi().getSandboxContext();
        context.clearAll(null).join();
        context.setCurrencyBalance(currencyBalanceUSD, null).join();

        user.setState(State.MAIN_MENU);
        return List.of(new SimpleMessageResponse(user.getChatId(),
                "Новый портфель успешно создан", Keyboard.getMenuKeyboard()));
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
