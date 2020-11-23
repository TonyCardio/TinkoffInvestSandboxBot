package handlers;

import models.Handler;
import models.State;
import models.User;
import models.keyboards.Keyboard;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
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
                messages = handleShowPortfolio(user);
            else if (command.equalsIgnoreCase(FIND_ASSET))
                messages = handleFindAsset(user);
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

    private List<BotApiMethod> handleShowPortfolio(User user) {
        SandboxOpenApi api = user.getApi();
        Portfolio portfolio = api.getPortfolioContext().getPortfolio(null).join();

        StringBuilder positions = new StringBuilder("*Position  Balance*\n\n");
        for (Portfolio.PortfolioPosition position : portfolio.positions) {
            positions
                    .append(String.format("%-20s", position.name))
                    .append(position.balance.intValue())
                    .append("\n");
        }

        return List.of(new SendMessage(user.getChatId(), positions.toString())
                .enableMarkdown(true));
    }

    private List<BotApiMethod> handleFindAsset(User user) {
        user.setState(State.SEARCH_ASSET);
        return List.of(new SendMessage(
                user.getChatId(), "Введите тикер инструмента:")
                .setReplyMarkup(Keyboard.getToMenuKeyboard()));
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