import handlers.*;
import models.State;
import models.User;
import org.junit.Assert;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.mockito.Mockito.*;


public class HandlersTest {
    private final User user = mock(User.class);
    private final Message message = mock(Message.class);
    @Test
    public void test_ChoosePortfolioHandler_handleUSDCommand() {
        ChoosePortfolioHandler handler = new ChoosePortfolioHandler();
        when(message.getChatId()).thenReturn(Long.valueOf(0));

        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        when(callbackQuery.getData()).thenReturn("/USD");
        when(callbackQuery.getMessage()).thenReturn(message);

        handler.handleCallbackQuery(user, callbackQuery);

        verify(user).increaseUSDAmount(any());
    }

    @Test
    public void test_StartHandler_changeUserState() {
        StartHandler handler = new StartHandler();
        User user = mock(User.class);
        when(user.getChatId()).thenReturn(Long.valueOf(0));

        handler.handleMessage(user, message);

        verify(user).setState(State.NON_AUTHORIZED);
        verify(user).setLastQueryTime();
    }
}
