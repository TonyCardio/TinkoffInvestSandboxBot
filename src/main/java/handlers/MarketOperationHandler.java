package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.InlineKeyboard;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.orders.MarketOrder;
import ru.tinkoff.invest.openapi.models.orders.Operation;
import ru.tinkoff.invest.openapi.models.orders.PlacedOrder;
import wrappers.EditMessageResponse;
import wrappers.ResponseMessage;
import wrappers.SimpleMessageResponse;
import wrappers.WrappedUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketOperationHandler implements Handler {
    public static final String BUY = "/buy";
    public static final String SELL = "/sell";
    public static final String INCREASE_BUY = "/increase_buy";
    public static final String INCREASE_SELL = "/increase_sell";
    public static final String ACCEPT_BUY = "/buy_accept";
    public static final String ACCEPT_SELL = "/sell_accept_";


    @Override
    public List<ResponseMessage> handleMessage(User user, WrappedUpdate message) {
        return Collections.emptyList();
    }

    @Override
    public List<ResponseMessage> handleCallbackQuery(User user, WrappedUpdate update) {
        List<ResponseMessage> messages = new ArrayList<>();
        String message = update.getMessageData();

        if (message.equalsIgnoreCase(BUY) || message.equalsIgnoreCase(SELL))
            messages = handleChoseOperation(user, update);
        else if (message.equalsIgnoreCase(INCREASE_BUY) || message.equalsIgnoreCase(INCREASE_SELL))
            messages = handleIncrease(user, update);
        else if (message.equalsIgnoreCase(ACCEPT_BUY) || message.equalsIgnoreCase(ACCEPT_SELL))
            messages = handleAcceptOperation(user, update);

        user.setLastQueryTime();

        return messages;
    }

    private List<ResponseMessage> handleIncrease(User user, WrappedUpdate update) {
        String[] messageText = update.getCommand().split(" ");
        messageText[messageText.length - 1] = String.valueOf(
                Integer.parseInt(messageText[messageText.length - 1]) + 1);
        String text = String.join(" ", messageText);

        InlineKeyboard keyboard = update.getMessageData().equals(INCREASE_BUY)
                ? Keyboard.getBuyKeyboard()
                : Keyboard.getSellKeyboard();

        return List.of(new EditMessageResponse(user.getChatId(), text, update.getMessageId(), keyboard));
    }

    private List<ResponseMessage> handleAcceptOperation(User user, WrappedUpdate update) {
        Operation operation = update.getMessageData().equals(ACCEPT_BUY)
                ? Operation.Buy
                : Operation.Sell;

        return DoMarketOperation(user, update, operation);
    }

    private List<ResponseMessage> DoMarketOperation(User user, WrappedUpdate update, Operation operation) {
        String[] messageText = update.getCommand().split("[ \n]");
        int amount = Integer.parseInt(messageText[messageText.length - 1]);
        String figi = messageText[2];

        PlacedOrder placedOrder = PlaceOrder(user.getApi(), figi, amount, operation);
        String text = String.format("Отправленные лоты: %s\nИсполненные лоты: %s\n%s",
                placedOrder.requestedLots, placedOrder.executedLots, placedOrder.rejectReason);
        user.setState(State.MAIN_MENU);

        return List.of(new SimpleMessageResponse(
                user.getChatId(),
                text,
                Keyboard.getMenuKeyboard()));
    }

    private PlacedOrder PlaceOrder(SandboxOpenApi api, String figi, int amount, Operation operation) {
        MarketOrder order = new MarketOrder(amount, operation);
        return api.getOrdersContext()
                .placeMarketOrder(figi, order, null)
                .join();
    }

    private List<ResponseMessage> handleChoseOperation(User user, WrappedUpdate update) {
        InlineKeyboard keyboard = update.getMessageData().equals(BUY)
                ? Keyboard.getBuyKeyboard()
                : Keyboard.getSellKeyboard();

        return List.of(new EditMessageResponse(
                user.getChatId(),
                update.getCommand() + "\nКоличество: 0",
                update.getMessageId(),
                keyboard));
    }

    @Override
    public State handledState() {
        return State.DO_MARKET_OPERATION;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return List.of(BUY, SELL, INCREASE_BUY, INCREASE_SELL, ACCEPT_BUY, ACCEPT_SELL);
    }
}
