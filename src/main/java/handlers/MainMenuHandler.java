package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;
import wrappers.ResponseMessage;
import wrappers.SimpleMessageResponse;
import wrappers.WrappedUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainMenuHandler implements Handler {
    public static final String SHOW_PORTFOLIO = "/show";
    public static final String FIND_ASSET = "/find";
    public static final String RESET_PORTFOLIO = "/reset";
    public static final String SHOW_STATISTIC = "/stat";

    private static final HashMap<String, String> replyButtonsToCommands =
            new HashMap<>();

    static {
        replyButtonsToCommands.put("\uD83D\uDCBCПосмотреть портфель\uD83D\uDCBC", SHOW_PORTFOLIO);
        replyButtonsToCommands.put("❌Сбросить портфель❌", RESET_PORTFOLIO);
        replyButtonsToCommands.put("\uD83D\uDD0EНайти актив\uD83D\uDD0D", FIND_ASSET);
        replyButtonsToCommands.put("\uD83D\uDCC8Показать статистику\uD83D\uDCC8", SHOW_STATISTIC);
    }

    @Override
    public List<ResponseMessage> handleMessage(User user, WrappedUpdate update) {
        String text = update.getMessageData();
        List<ResponseMessage> messages = new ArrayList<>();

        if (replyButtonsToCommands.containsKey(text)) {
            String command = replyButtonsToCommands.get(text);
            if (command.equalsIgnoreCase(SHOW_PORTFOLIO))
                messages = handleShowPortfolio(user);
            else if (command.equalsIgnoreCase(FIND_ASSET))
                messages = handleFindAsset(user);
            else if (command.equalsIgnoreCase(SHOW_STATISTIC))
                messages = handleShowStatistic(user);
            else if (command.equalsIgnoreCase(RESET_PORTFOLIO))
                throw new UnsupportedOperationException();
            user.setLastQueryTime();
        }

        return messages;
    }

    @Override
    public List<ResponseMessage> handleCallbackQuery(User user, WrappedUpdate wrapper) {
        return Collections.emptyList();
    }

    private List<ResponseMessage> handleShowPortfolio(User user) {
        SandboxOpenApi api = user.getApi();
        Portfolio portfolio = api.getPortfolioContext().getPortfolio(null).join();

        StringBuilder positions = new StringBuilder("*Position  Balance*\n\n");
        for (Portfolio.PortfolioPosition position : portfolio.positions) {
            positions
                    .append(String.format("%-20s", position.name))
                    .append(position.balance.intValue())
                    .append("\n");
        }
        SimpleMessageResponse simpleMessageResponse = new SimpleMessageResponse(user.getChatId(), positions.toString());
        simpleMessageResponse.enableMarkdown();
        return List.of(simpleMessageResponse);
    }

    private List<ResponseMessage> handleFindAsset(User user) {
        user.setState(State.SEARCH_ASSET);
        return List.of(new SimpleMessageResponse(
                user.getChatId(), "Введите тикер инструмента:", Keyboard.getToMenuKeyboard()));
    }

    private List<ResponseMessage> handleShowStatistic(User user) {
        user.setState(State.CHECK_STATISTIC);
        return List.of(new SimpleMessageResponse(
                user.getChatId(), "Подтвердите отправку статистики", Keyboard.getToMenuKeyboard()));
    }

    @Override
    public State handledState() {
        return State.MAIN_MENU;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return Collections.emptyList();
    }
}