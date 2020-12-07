import handlers.MarketOperationHandler;
import handlers.SearchAssetHandler;
import models.State;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wrappers.SimpleMessageResponse;
import wrappers.WrappedUpdate;

import static junit.framework.Assert.assertNotSame;
import static org.mockito.Mockito.*;

public class SearchAssertHandlerTest {
    private User user;
    private WrappedUpdate update;
    private SearchAssetHandler handler;

    @Before
    public void setUp() {
        user = new User(321);
        update = mock(WrappedUpdate.class);
        handler = new SearchAssetHandler();
    }

    @Test
    public void handleToMenu_ShouldResponse_OnValidRequest() {
        when(update.getMessageData()).thenReturn(SearchAssetHandler.TO_MENU);

        SimpleMessageResponse simpleMessageResponse = (SimpleMessageResponse) handler.handleMessage(user, update).get(0);

        Assert.assertTrue(simpleMessageResponse.getMessage().contains("Чем займёмся?"));
    }

    @Test
    public void handleToMenu_ShouldChangeUserState() {
        when(update.getMessageData()).thenReturn(SearchAssetHandler.TO_MENU);
        State before = user.getState();

        handler.handleMessage(user, update);

        Assert.assertNotSame(before, user.getState());
    }

    @Test
    public void handleCallbackQuery_ShouldChangeUserLastQueryTime() {
        when(update.getMessageData()).thenReturn("/aapl");
        long preQueryTime = user.getLastQueryTime();
        handler.handleCallbackQuery(user, update);

        assertNotSame(preQueryTime, user.getLastQueryTime());
    }
}
