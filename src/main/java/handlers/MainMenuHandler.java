package handlers;

import models.Handler;
import models.State;
import models.User;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainMenuHandler implements Handler {
    public static final String SHOW_PORTFOLIO = "/show";
    public static final String FIND_ASSET = "/find";
    public static final String RESET_PORTFOLIO = "/reset";

    private static final HashMap<String, String> replyButtonsToCommands =
            new HashMap<>();

    static {
        replyButtonsToCommands.put("\uD83D\uDCBCПосмотреть портфель\uD83D\uDCBC", SHOW_PORTFOLIO);
        replyButtonsToCommands.put("❌Сбросить портфель❌", RESET_PORTFOLIO);
        replyButtonsToCommands.put("\uD83D\uDD0EНайти актив\uD83D\uDD0D", FIND_ASSET);
    }

    @Override
    public List<BotApiMethod> handleMessage(User user, Message message) {
        String text = message.getText();
        List<BotApiMethod> messages = new ArrayList<>();

        if (replyButtonsToCommands.containsKey(text)) {
            String command = replyButtonsToCommands.get(text);
            if (command.equalsIgnoreCase(SHOW_PORTFOLIO))
                messages = handleShow(user);
            else if (command.equalsIgnoreCase(FIND_ASSET))
                throw new UnsupportedOperationException();
            else if (command.equalsIgnoreCase(RESET_PORTFOLIO))
                throw new UnsupportedOperationException();
            user.setLastQueryTime();
        }

        return messages;
    }

    @Override
    public List<BotApiMethod> handleCallbackQuery(User user, CallbackQuery callbackQuery) {
        return Collections.emptyList();
    }


    private List<BotApiMethod> handleShow(User user) {
        List<BotApiMethod> messages = new ArrayList<>();

        SandboxOpenApi api = user.getApi();
        Portfolio portfolio = api.getPortfolioContext().getPortfolio(null).join();

        StringBuilder positions = new StringBuilder("*Position  Balance*\n\n");
        for (Portfolio.PortfolioPosition position :
                portfolio.positions) {
            positions
                    .append(String.format("%-20s", position.name))
                    .append(position.balance.intValue())
                    .append("\n");
        }
        messages.add(
                new SendMessage(user.getChatId(), positions.toString())
                        .enableMarkdown(true));

        return messages;
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