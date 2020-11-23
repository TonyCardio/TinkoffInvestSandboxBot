package handlers;

import com.google.common.collect.Iterables;
import models.Handler;

import models.State;
import models.User;
import models.keyboards.InlineButtonInfo;
import models.keyboards.InlineKeyboard;
import models.keyboards.Keyboard;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.tinkoff.invest.openapi.models.market.*;
import wrappers.ResponseMessage;
import wrappers.SimpleMessageResponse;
import wrappers.UpdateWrapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SearchAssetHandler implements Handler {
    public static final String TO_MENU = "В главное меню";
    public final List<String> InstrumentsFigi = Collections.synchronizedList(new ArrayList<>());

    @Override
    public List<ResponseMessage> handleMessage(User user, UpdateWrapper wrapper) {
        String text = wrapper.getMessageData();
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
        return List.of(new SimpleMessageResponse(user.getChatId(), "Чем займёмся?", Keyboard.getMenuKeyboard()));
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
    public List<ResponseMessage> handleCallbackQuery(User user, UpdateWrapper wrapper) {
        String assetFigi = wrapper.getMessageData();
        OffsetDateTime currentTime = OffsetDateTime.now();

        Optional<HistoricalCandles> historicalCandles = user.getApi()
                .getMarketContext()
                .getMarketCandles(assetFigi, currentTime.minusHours(1),
                        OffsetDateTime.now(), CandleInterval.ONE_MIN).join();

        List<Candle> candles = new ArrayList<>();
        if (historicalCandles.isPresent())
            candles = historicalCandles.get().candles;

        Candle lastCandle = Iterables.getLast(candles);
        String text = "Последняя цена инструмента: " + lastCandle.openPrice.toString();
        return List.of(new SimpleMessageResponse(user.getChatId(), text));
    }

    @Override
    public State handledState() {
        return State.SEARCH_ASSET;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return InstrumentsFigi;
    }
}
