package fall2018.csc2017.slidingtiles;

import org.junit.Before;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * UnitTest for SlidingTilesGameController
 */
public class SlidingTilesGameControllerTest {

    private SlidingTilesManager slidingTilesManager;
    private GestureDetectGridView mView;
    private SlidingTilesGameController mController;

    /**
     * Mocking our dependencies(SlidingTilesManager)
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        slidingTilesManager = mock(SlidingTilesManager.class);
        mController = new SlidingTilesGameController();
        mController.setSlidingTilesManager(slidingTilesManager);
        mView = mock(GestureDetectGridView.class);
    }

    /**
     * Testing ProcessTapMovement to see that a movement is properly registered.
     */
    @Test
    public void testProcessTapMovement() {
        when(slidingTilesManager.isValidMove(14)).thenReturn(true);
        doNothing().when(slidingTilesManager).savePreviousMovePosition(isA(Integer.class));
        doNothing().when(slidingTilesManager).makeMove(14);
        when(slidingTilesManager.isOver()).thenReturn(false);
        int[] ret = new int[2];
        ret[0] = 3;
        ret[1] = 3;
        when(slidingTilesManager.findBlankTile(25)).thenReturn(ret);

        mController.processTapMovement(mView.getContext(), 14);
        verify(slidingTilesManager).isValidMove(14);
        verify(slidingTilesManager).savePreviousMovePosition(isA(Integer.class));
        verify(slidingTilesManager).makeMove(14);
        verify(slidingTilesManager).isOver();
    }
}