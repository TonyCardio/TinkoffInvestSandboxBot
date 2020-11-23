import handlers.StartHandler;
import models.State;
import models.User;
import org.junit.Before;
import org.junit.Test;
import wrappers.WrappedUpdate;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class StartHandlerTests {
    private StartHandler handler;
    private User user;
    private WrappedUpdate update;

    @Before
    public void setUp() {
        update = mock(WrappedUpdate.class);
        user = new User(123);
        handler = new StartHandler();
    }

    @Test
    public void handleMessage_ShouldChangeUserState() {
        handler.handleMessage(user, update);

        assertSame(user.getState(), State.NON_AUTHORIZED);
    }

    @Test
    public void handleMessage_ShouldChangeUserLastQueryTime() {
        long preQueryTime = user.getLastQueryTime();
        handler.handleMessage(user, update);

        assertNotSame(preQueryTime, user.getLastQueryTime());
    }
}
