import handlers.ChoosePortfolioHandler;
import handlers.StartHandler;
import models.State;
import models.UpdateReceiver;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import wrappers.WrappedUpdate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

public class UpdateReceiverTests {
    private UpdateReceiver receiver;
    private StartHandler startHandler;
    private ChoosePortfolioHandler choosePortfolioHandler;
    private WrappedUpdate withoutCallbackUpdate;
    private WrappedUpdate withCallbackUpdate;
    private WrappedUpdate withUnknownCommandUpdate;

    @Before
    public void setUp() {
        startHandler = mock(StartHandler.class);
        when(startHandler.handledState()).thenReturn(State.NONE);

        choosePortfolioHandler = mock(ChoosePortfolioHandler.class);
        when(choosePortfolioHandler.handledState()).thenReturn(State.CHOOSE_PORTFOLIO);
        when(choosePortfolioHandler.handledCallBackQuery()).thenReturn(List.of("/command"));

        receiver = new UpdateReceiver(List.of(choosePortfolioHandler, startHandler));
        initUpdates();
    }

    @Ignore
    private void initUpdates() {
        withoutCallbackUpdate = mock(WrappedUpdate.class);
        when(withoutCallbackUpdate.hasHasCallBackQuery()).thenReturn(false);
        when(withoutCallbackUpdate.getMessageData()).thenReturn("data");

        withCallbackUpdate = mock(WrappedUpdate.class);
        when(withCallbackUpdate.hasHasCallBackQuery()).thenReturn(true);
        when(withCallbackUpdate.getMessageData()).thenReturn("/command");

        withUnknownCommandUpdate = mock(WrappedUpdate.class);
        when(withUnknownCommandUpdate.hasHasCallBackQuery()).thenReturn(true);
        when(withUnknownCommandUpdate.getMessageData()).thenReturn("/unknown_command");
    }

    @Test
    public void test_getHandlerByState_CallHandler() {
        receiver.handle(withoutCallbackUpdate);

        verify(startHandler).handleMessage(any(), any());
    }

    @Test
    public void test_getHandlerByCallBackQuery_CallHandler() {
        receiver.handle(withCallbackUpdate);

        verify(choosePortfolioHandler).handleCallbackQuery(any(), any());
    }

    @Test
    public void test_getHandlerByCallBackQuery_ReturnEmptyCollection() {
        List result = receiver.handle(withUnknownCommandUpdate);

        assertEquals(Collections.emptyList(), result);
    }
}
