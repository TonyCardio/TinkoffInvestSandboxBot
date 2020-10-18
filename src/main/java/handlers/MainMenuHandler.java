package handlers;

import models.Handler;
import models.State;
import models.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainMenuHandler implements Handler {
    public static final String SHOW_PORTFOLIO = "/show";
    public static final String FIND_ASSET = "/find";
    public static final String RESET_PORTFOLIO = "/reset";


    @Override
    public List<SendMessage> handleMessage(User user, Message message) {
        String text = message.getText();
        List<SendMessage> messages = new ArrayList<>();

        if (text.equalsIgnoreCase(SHOW_PORTFOLIO))
            messages = handleShow(user);
        else if (text.equalsIgnoreCase(FIND_ASSET))
            throw new UnsupportedOperationException();
        else if (text.equalsIgnoreCase(RESET_PORTFOLIO))
            throw new UnsupportedOperationException();

        user.setLastQueryTime();
        return messages;
    }

    @Override
    public List<SendMessage> handleCallbackQuery(User user, CallbackQuery callbackQuery) {
        return Collections.emptyList();
    }


    private List<SendMessage> handleShow(User user) {
        List<SendMessage> messages = new ArrayList<>();

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
        messages.add(new SendMessage(user.getChatId(), positions.toString()));

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
