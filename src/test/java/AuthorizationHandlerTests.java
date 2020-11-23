import handlers.AuthorizationHandler;
import models.User;
import org.junit.Before;
import org.junit.Test;
import wrappers.WrappedUpdate;

import static junit.framework.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthorizationHandlerTests {
    private User user;
    private WrappedUpdate update;
    private AuthorizationHandler handler;

    @Before
    public void setUp() {
        user = new User(123);
        update = mock(WrappedUpdate.class);
        handler = mock(AuthorizationHandler.class);
    }

    @Test
    public void handleCallbackQuery_ShouldChangeUserLastQueryTime() {
        when(update.getMessageData()).thenReturn("some token");
        long preQueryTime = user.getLastQueryTime();
        handler.handleCallbackQuery(user, update);

        assertNotSame(preQueryTime, user.getLastQueryTime());
    }
}
