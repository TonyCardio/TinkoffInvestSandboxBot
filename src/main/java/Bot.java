import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private final String token;
    private ConsoleHandler consoleHandler;
    private ClientsInfo clientsInfo;

    public Bot(String token) {
        consoleHandler = new ConsoleHandler();
        clientsInfo = new ClientsInfo();
        this.token = token;
    }

    private synchronized void sendMessage(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String chatId = Long.toString(message.getChatId());
        if (message.isCommand()) {
            String text = consoleHandler.getCommandText(message.getText());

            if (text.equals("Удачи!")) {
                sendMessage(chatId, text);
                text = clientsInfo.generateExample(chatId).getExample();
            }
            sendMessage(chatId, text);
        } else if (message.hasText()) {
            String userAnswer = message.getText();
            if (clientsInfo.isCorrectAnswer(userAnswer, chatId)) {
                sendMessage(chatId, "Молодец!!!");
                String example = clientsInfo.generateExample(chatId).getExample();
                sendMessage(chatId, example);
            } else
                sendMessage(chatId, "Неправильно, попробуй еще раз!!!");
        }
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
