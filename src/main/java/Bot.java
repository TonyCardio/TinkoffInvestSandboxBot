import handlers.*;
import models.Handler;
import models.UpdateReceiver;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import wrappers.ResponseMessage;
import wrappers.UpdateWrapper;

import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private final String token;
    private final UpdateReceiver updateReceiver;

    public Bot(String token) {
        this.token = token;
        List<Handler> handlers = List.of(
                new StartHandler(),
                new AuthorizationHandler(),
                new MainMenuHandler(),
                new SearchAssetHandler(),
                new ChoosePortfolioHandler());
        updateReceiver = new UpdateReceiver(handlers);
    }

    private synchronized void sendMessages(List<ResponseMessage> messages) {
        for (ResponseMessage message : messages) {
            try {
                execute(message.createMessage());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        UpdateWrapper updateWrapper = new UpdateWrapper(update);
        if (!update.hasCallbackQuery() &&
                update.getMessage().getText().equals("/help")) {
            sendMessages(UpdateReceiver.handleHelp(updateWrapper));
            return;
        }
        List<ResponseMessage> responseMessages = updateReceiver.handle(updateWrapper);
        sendMessages(responseMessages);
    }

    @Override
    public String getBotUsername() {
        return "InvestBot";
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
