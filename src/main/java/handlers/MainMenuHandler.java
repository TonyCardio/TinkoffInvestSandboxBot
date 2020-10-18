package handlers;

import models.Handler;
import models.State;
import models.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.models.portfolio.Portfolio;

import java.util.ArrayList;
import java.util.List;

public class MainMenuHandler implements Handler {
    public static final String ADD_CURRENCY = "/add";
    public static final String SHOW_PORTFOLIO = "/show";
    public static final String FIND_ASSET = "/find";
    public static final String CLEAR_PORTFOLIO = "/clear";


    @Override
    public List<SendMessage> handle(User user, String message) {
        List<SendMessage> messages = new ArrayList<>();

        if (message.equalsIgnoreCase(SHOW_PORTFOLIO))
            messages = handleShow(user);
        else if (message.equalsIgnoreCase(ADD_CURRENCY))
            messages = handleChooseCurrency(user);

        user.setLastQueryTime();
        return messages;
    }

    private List<SendMessage> handleChooseCurrency(User user) {
        //TODO: Keyboards.getCreateNewPortfolioKeyboard
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("добавить 50$", "/USD"));
        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(new SendMessage(user.getChatId(),
                "Добавьте валюту")
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    private static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        return new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(command);
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
        return List.of(ADD_CURRENCY, SHOW_PORTFOLIO, FIND_ASSET, CLEAR_PORTFOLIO);
    }
}
