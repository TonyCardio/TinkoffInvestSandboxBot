package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.models.portfolio.InstrumentType;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;
import wrappers.EditMessageResponse;
import wrappers.ResponseMessage;
import wrappers.SimpleMessageResponse;
import wrappers.WrappedUpdate;

import java.util.HashMap;
import java.util.List;
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
        List<Portfolio.PortfolioPosition> positions = portfolio.positions;
        Integer page = getPage(user, STAY, positions.size());
        String statistic = getStatistics(positions.get(page));

        page += 1;
        String oneOf = page + "/" + positions.size();
        return List.of(new SimpleMessageResponse(user.getChatId(), statistic, Keyboard.getPaginationKeyboard(oneOf)));
    }

    private String getStatistics(Portfolio.PortfolioPosition position) {
        StringBuilder statistic = new StringBuilder("Statistic\n\n");

        if (position.instrumentType == InstrumentType.Currency)
            statistic.append(getStatisticsForPartPosition(position));
        else
            statistic.append(getStatisticsForPosition(position));
        return statistic.toString();
    }

    private String getStatisticsForPosition(Portfolio.PortfolioPosition position) {
        String partStatistics = getStatisticsForPartPosition(position);
        return partStatistics +
                String.format("\nОжидаемая доходность: %d %s\nСредняя цена позиции: %d %s",
                        position.expectedYield.value.intValue(), position.expectedYield.currency.toString(),
                        position.averagePositionPrice.value.intValue(), position.averagePositionPrice.currency.toString());
    }

    private String getStatisticsForPartPosition(Portfolio.PortfolioPosition position) {
        return String.format("%s\nОбъём позиции: %d",
                position.name,
                position.balance.intValue());
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
        return page % pageCount;
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
        String statistic = getStatistics(positions.get(page));

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
