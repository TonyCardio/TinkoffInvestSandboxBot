package handlers;

import com.google.common.collect.Iterables;
import models.Handler;
import models.State;
import models.User;
import models.keyboards.InlineButtonInfo;
import models.keyboards.InlineKeyboard;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.models.market.*;
import wrappers.ResponseMessage;
import wrappers.SimpleMessageResponse;
import wrappers.WrappedUpdate;

import java.time.OffsetDateTime;
import java.util.*;

public class SearchAssetHandler implements Handler {
    public static final String TO_MENU = "В главное меню";
    private final Set<String> InstrumentsFigi = Collections.synchronizedSet(new HashSet<>());

    @Override
    public List<ResponseMessage> handleMessage(User user, WrappedUpdate message) {
        String text = message.getMessageData();
        List<ResponseMessage> messages;

        if (text.equalsIgnoreCase(TO_MENU))
            messages = handleToMenu(user);
        else
            messages = handleSearchAsset(user, text);

        user.setLastQueryTime();

        return messages;
    }

    private List<ResponseMessage> handleToMenu(User user) {
        user.setState(State.MAIN_MENU);
        return List.of(new SimpleMessageResponse(user.getChatId(),
                "Чем займёмся?", Keyboard.getMenuKeyboard()));
    }

    private List<ResponseMessage> handleSearchAsset(User user, String text) {
        InstrumentsList instruments = user
                .getApi()
                .getMarketContext()
                .searchMarketInstrumentsByTicker(text).join();

        List<List<InlineButtonInfo>> keyboardButtons = new ArrayList<>();

        for (Instrument instr : instruments.instruments) {
            keyboardButtons.add(List.of(new InlineButtonInfo(instr.name, instr.figi)));
            InstrumentsFigi.add(instr.figi);
        }

        InlineKeyboard inlineKeyboard = new InlineKeyboard(keyboardButtons);
        return List.of(new SimpleMessageResponse(user.getChatId(), "Выберите инструмент:", inlineKeyboard));
    }

    @Override
    public List<ResponseMessage> handleCallbackQuery(User user, WrappedUpdate update) {
        List<ResponseMessage> messages = new ArrayList<>();
        String message = update.getMessageData();

        if (InstrumentsFigi.contains(message))
            messages = handleChooseAsset(user, update);

        user.setLastQueryTime();

        return messages;
    }

    private List<ResponseMessage> handleChooseAsset(User user, WrappedUpdate update) {
        String assetFigi = update.getMessageData();
        OffsetDateTime currentTime = OffsetDateTime.now();

        Optional<HistoricalCandles> historicalCandles = user.getApi()
                .getMarketContext()
                .getMarketCandles(assetFigi, currentTime.minusWeeks(1),
                        OffsetDateTime.now(), CandleInterval.DAY).join();

        List<Candle> candles = new ArrayList<>();
        if (historicalCandles.isPresent())
            candles = historicalCandles.get().candles;

        Candle lastCandle = Iterables.getLast(candles);
        String text = "FIGI инструмента: " + assetFigi + "\n" +
                "Последняя цена инструмента: " + lastCandle.closePrice.toString();

        user.setState(State.DO_MARKET_OPERATION);

        return List.of(new SimpleMessageResponse(
                user.getChatId(),
                text,
                Keyboard.getBuySellKeyboard()));
    }

    @Override
    public State handledState() {
        return State.SEARCH_ASSET;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return new ArrayList<>(InstrumentsFigi);
    }
}
