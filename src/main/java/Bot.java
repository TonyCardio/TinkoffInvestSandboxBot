import com.fasterxml.jackson.databind.ObjectMapper;
import handlers.*;
import models.Handler;
import models.UpdateReceiver;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.StringWriter;
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

    private synchronized void sendMessages(List<BotApiMethod> messages) {
//        List<BotApiMethod> a = new ArrayList<>();
//        messages.add(new EditMessageText());
//        messages.add(new SendMessage());

        for (BotApiMethod message : messages) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        /*
        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(writer, update);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = writer.toString();
        System.out.println(result);
        */

        if (!update.hasCallbackQuery() &&
                update.getMessage().getText().equals("/help")) {
            sendMessages(UpdateReceiver.handleHelp(update));
            return;
        }

        List<BotApiMethod> responseMessages = updateReceiver.handle(update);
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
