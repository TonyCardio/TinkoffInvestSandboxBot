import handlers.*;
import models.State;
import models.User;
import org.junit.Before;
import org.junit.Test;
import wrappers.UpdateWrapper;

import java.math.BigDecimal;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;


public class ChoosePortfolioHandlerTests {
    private User user;
    private UpdateWrapper update;
    private ChoosePortfolioHandler handler;

    @Before
    public void setUp() {
        user = new User(123);
        update = mock(UpdateWrapper.class);
        handler = new ChoosePortfolioHandler();
    }

    @Test
    public void handleAddCurrency_ShouldIncreaseUSDAmount() {
        when(update.getMessageData()).thenReturn(ChoosePortfolioHandler.USD);
        BigDecimal amountBeforeAdd = user.getStartUSDAmount();

        handler.handleCallbackQuery(user, update);

        assertNotSame(amountBeforeAdd, user.getStartUSDAmount());
    }

    @Test
    public void handleContinue_ShouldChangeUserState() {
        when(update.getMessageData()).thenReturn(ChoosePortfolioHandler.ACCEPT);

        handler.handleCallbackQuery(user, update);

        assertSame(user.getState(), State.MAIN_MENU);
    }

    @Test
    public void handleAccept_ShouldChangeUserState() {
        when(update.getMessageData()).thenReturn(ChoosePortfolioHandler.ACCEPT);

        handler.handleCallbackQuery(user, update);

        assertSame(user.getState(), State.MAIN_MENU);
    }

    @Test
    public void handleCallbackQuery_ShouldChangeUserLastQueryTime() {
        when(update.getMessageData()).thenReturn(
                ChoosePortfolioHandler.CONTINUE_WITH_OLD_PORTFOLIO);
        long preQueryTime = user.getLastQueryTime();
        handler.handleCallbackQuery(user, update);

        assertNotSame(preQueryTime, user.getLastQueryTime());
    }
}
