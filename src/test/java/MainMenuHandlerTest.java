import handlers.MainMenuHandler;
import models.State;
import models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import wrappers.SimpleMessageResponse;
import wrappers.UpdateWrapper;


import static org.mockito.Mockito.*;


public class MainMenuHandlerTest {
    private User user;
    private UpdateWrapper update;
    private MainMenuHandler handler;

    @Before
    public void setUp() {
        user = new User(321);
        update = mock(UpdateWrapper.class);
        handler = new MainMenuHandler();
    }

    @Test
    public void handleFindAsset_ShouldResponse_OnValidRequest() {
        when(update.getMessageData()).thenReturn("\uD83D\uDD0EНайти актив\uD83D\uDD0D");

        SimpleMessageResponse simpleMessageResponse = (SimpleMessageResponse) handler.handleMessage(user, update).get(0);

        Assert.assertTrue(simpleMessageResponse.getMessage().contains("Введите тикер инструмента:"));
    }

    @Test
    public void handleFindAsset_ShouldChangeUserState() {
        when(update.getMessageData()).thenReturn("\uD83D\uDD0EНайти актив\uD83D\uDD0D");
        State before = user.getState();

        handler.handleMessage(user, update);

        Assert.assertNotSame(before, user.getState());
    }
}