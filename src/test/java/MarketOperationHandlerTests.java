import handlers.MarketOperationHandler;
import models.User;
import org.junit.Before;
import org.junit.Test;

import wrappers.ResponseMessage;
import wrappers.WrappedUpdate;

import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MarketOperationHandlerTests {
    private User user;
    private WrappedUpdate update;
    private MarketOperationHandler handler;

    @Before
    public void setUp() {
        user = new User(321);
        update = mock(WrappedUpdate.class);
        handler = new MarketOperationHandler();
    }

    @Test
    public void handleCallbackQuery_ShouldChangeUserLastQueryTime() {
        when(update.getMessageData()).thenReturn(
                MarketOperationHandler.BUY);
        long preQueryTime = user.getLastQueryTime();
        handler.handleCallbackQuery(user, update);

        assertNotSame(preQueryTime, user.getLastQueryTime());
    }

    @Test
    public void handleMessage_ShouldNotReturnMessages() {
        when(update.getMessageData()).thenReturn("aaa");

        List<ResponseMessage> messages = handler.handleMessage(user, update);

        assertEquals(messages.size(), 0);
    }

    @Test
    public void handleMessage_ShouldNotChangeUserLastQueryTime() {
        when(update.getMessageData()).thenReturn("aaa");
        long preQueryTime = user.getLastQueryTime();

        handler.handleMessage(user, update);

        assertTrue(Math.abs(preQueryTime - user.getLastQueryTime()) < 1E-6);
    }
}
