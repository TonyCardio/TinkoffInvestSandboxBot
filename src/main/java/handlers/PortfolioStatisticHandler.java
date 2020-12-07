package handlers;

import com.google.common.collect.Iterables;
import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.models.market.Candle;
import ru.tinkoff.invest.openapi.models.market.CandleInterval;
import ru.tinkoff.invest.openapi.models.market.HistoricalCandles;
import ru.tinkoff.invest.openapi.models.portfolio.InstrumentType;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio.PortfolioPosition;
import wrappers.EditMessageResponse;
import wrappers.ResponseMessage;
import wrappers.SimpleMessageResponse;
import wrappers.WrappedUpdate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PortfolioStatisticHandler implements Handler {
    public static final String TO_MENU = "В главное меню";
    public static final String PREVIOUS = "/prev";
    public static final String NEXT = "/next";
    public static final String STAY = "/stay";
    private final HashMap<String, Integer> pageOffset;
    private final ConcurrentHashMap<Long, Integer> chatIdToPage;

    public PortfolioStatisticHandler() {
        pageOffset = new HashMap<>();
        pageOffset.put(PREVIOUS, -1);
        pageOffset.put(STAY, 0);
        pageOffset.put(NEXT, 1);
        chatIdToPage = new ConcurrentHashMap<>();
    }

    @Override
    public List<ResponseMessage> handleMessage(User user, WrappedUpdate message) {
        Portfolio portfolio = user.getApi().getPortfolioContext().getPortfolio(null).join();

        if (message.getMessageData().equals(TO_MENU))
            return handleToMenu(user);
        List<PortfolioPosition> positions = portfolio.positions;
        Integer page = getPage(user, STAY, positions.size());
        String statistic = getStatistics(user, positions.get(page));

        page += 1;
        String oneOf = page + "/" + positions.size();
        String generalStatistic = getGeneralStatistic(user, positions);
        return List.of(new SimpleMessageResponse(user.getChatId(), generalStatistic),
                new SimpleMessageResponse(user.getChatId(), statistic, Keyboard.getPaginationKeyboard(oneOf)));
    }

    private String getStatistics(User user, PortfolioPosition position) {
        StringBuilder statistic = new StringBuilder("Статистика\n\n");

        statistic.append(getStatisticsForPosition(user, position));

        return statistic.toString();
    }

    private String getStatisticsForPosition(User user, PortfolioPosition position) {
        String partStatistics = getStatisticsForPartPosition(position);
        Candle candle = getFigiCandle(user, position);

        return partStatistics +
                String.format("\nНаибольшая цена:   %d\nНаименьшая цена:   %d\nЦена закрытия:   %d\nОбъём торгов:   %d",
                        candle.highestPrice.intValue(), candle.lowestPrice.intValue(),
                        candle.closePrice.intValue(), candle.tradesValue.intValue());
    }

    private String getStatisticsForPartPosition(PortfolioPosition position) {
        return String.format("Позиция:   %s\nОбъём позиции:   %d",
                position.name,
                position.balance.intValue());
    }

    private Candle getFigiCandle(User user, PortfolioPosition position) {
        String assetFigi = position.figi;
        OffsetDateTime currentTime = OffsetDateTime.now();

        Optional<HistoricalCandles> historicalCandles = user.getApi()
                .getMarketContext()
                .getMarketCandles(assetFigi, currentTime.minusWeeks(1),
                        OffsetDateTime.now(), CandleInterval.DAY).join();

        List<Candle> candles = new ArrayList<>();
        if (historicalCandles.isPresent())
            candles = historicalCandles.get().candles;

        return Iterables.getLast(candles);
    }

    private String getGeneralStatistic(User user, List<PortfolioPosition> positions) {
        BigDecimal startAmount = user.getStartUSDAmount();
        BigDecimal currentAmount = new BigDecimal(0);

        for (PortfolioPosition position : positions) {
            if (position.instrumentType.equals(InstrumentType.Currency))
                currentAmount = currentAmount.add(position.balance);
            else
                currentAmount = currentAmount.add(getFigiCandle(user, position).closePrice.multiply(position.balance));
        }

        Integer growth = currentAmount.subtract(startAmount).intValue();

        return String.format("Общий рост портфеля составил:   %d $", growth);
    }

    private Integer getPage(User user, String command, Integer pageCount) {
        Long chatId = user.getChatId();
        if (!chatIdToPage.containsKey(chatId)) {
            chatIdToPage.put(chatId, 0);
            return 0;
        }
        Integer page = chatIdToPage.get(chatId);
        page += pageOffset.get(command);
        page = Math.abs(page);
        page %= pageCount;
        chatIdToPage.put(chatId, page);
        return page;
    }

    private List<ResponseMessage> handleToMenu(User user) {
        user.setState(State.MAIN_MENU);
        return List.of(new SimpleMessageResponse(user.getChatId(), "Чем займемся?", Keyboard.getMenuKeyboard()));
    }

    @Override
    public List<ResponseMessage> handleCallbackQuery(User user, WrappedUpdate callbackQuery) {
        Portfolio portfolio = user.getApi().getPortfolioContext().getPortfolio(null).join();

        List<Portfolio.PortfolioPosition> positions = portfolio.positions;
        Integer page = getPage(user, callbackQuery.getMessageData(), positions.size());
        String statistic = getStatistics(user, positions.get(page));

        page += 1;
        String oneOf = page + "/" + positions.size();
        return List.of(new EditMessageResponse(
                user.getChatId(),
                statistic,
                callbackQuery.getMessageId(),
                Keyboard.getPaginationKeyboard(oneOf)));
    }

    @Override
    public State handledState() {
        return State.CHECK_STATISTIC;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return List.of(PREVIOUS, NEXT, STAY);
    }
}
