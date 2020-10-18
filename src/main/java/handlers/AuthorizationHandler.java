package handlers;

import models.Handler;
import models.State;
import models.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tinkoff.invest.openapi.SandboxOpenApi;
import ru.tinkoff.invest.openapi.exceptions.WrongTokenException;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApiFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class AuthorizationHandler implements Handler {
    @Override
    public List<SendMessage> handleMessage(User user, Message message) {
        String text = message.getText();
        final Logger logger;
        try {
            logger = initLogger();
        } catch (IOException ex) {
            System.err.println("При инициализации логгера произошла ошибка: " + ex.getLocalizedMessage());
            return Collections.emptyList();
        }

        OkHttpOpenApiFactory factory = new OkHttpOpenApiFactory(text, logger);
        SandboxOpenApi api = factory.createSandboxOpenApiClient(Executors.newSingleThreadExecutor());

        boolean isValidToken = checkTokenValidity(api);
        List<SendMessage> messages = addAuthorisationResultMessages(
                user.getChatId(), isValidToken, text);

        if (isValidToken) {
            user.setApi(api);
            user.setState(State.CHOOSE_PORTFOLIO);
        }

        user.setLastQueryTime();
        return messages;
    }

    @Override
    public List<SendMessage> handleCallbackQuery(User user, CallbackQuery callbackQuery) {
        return Collections.emptyList();
    }

    private List<SendMessage> addAuthorisationResultMessages(long chatId, boolean isAuth, String token) {
        List<SendMessage> messages = new ArrayList<>();
        if (isAuth) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                    createInlineKeyboardButton(
                            "Продолжить со старым портфелем", "/continue"));
            List<InlineKeyboardButton> inlineKeyboardButtonsRowTwo = List.of(
                    createInlineKeyboardButton(
                            "Создать новый", "/create_new"));
            inlineKeyboardMarkup.setKeyboard(
                    List.of(inlineKeyboardButtonsRowOne, inlineKeyboardButtonsRowTwo));

            messages.add(new SendMessage(chatId, "Успешная авторизация")
                    .setReplyMarkup(inlineKeyboardMarkup));
        } else {
            messages.add(new SendMessage(chatId,
                    String.format("Невалидный токен: %s\n", token)));
            messages.add(new SendMessage(
                    chatId, "Введите свой токен"));
        }
        return messages;
    }

    private static InlineKeyboardButton createInlineKeyboardButton(String text, String command) {
        return new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(command);
    }

    private boolean checkTokenValidity(SandboxOpenApi api) {
        boolean isValidToken = false;
        try {
            api.getSandboxContext().performRegistration(null).get();
            isValidToken = true;
        } catch (ExecutionException e) {
            if (e.getCause() instanceof WrongTokenException) {
                try {
                    api.close();
                } catch (Exception ex) {
                    e.printStackTrace();
                }
            } else
                e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isValidToken;
    }

    private static Logger initLogger() throws IOException {
        final LogManager logManager = LogManager.getLogManager();
        final ClassLoader classLoader = AuthorizationHandler.class.getClassLoader();

        try (final InputStream input = classLoader.getResourceAsStream("logging.properties")) {

            if (input == null) {
                throw new FileNotFoundException();
            }

            Files.createDirectories(Paths.get("./logs"));
            logManager.readConfiguration(input);
        }

        return Logger.getLogger(AuthorizationHandler.class.getName());
    }

    @Override
    public State handledState() {
        return State.NON_AUTHORIZED;
    }

    @Override
    public List<String> handledCallBackQuery() {
        return Collections.emptyList();
    }
}
