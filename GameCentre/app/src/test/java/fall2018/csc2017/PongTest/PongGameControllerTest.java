package fall2018.csc2017.PongTest;

import android.graphics.RectF;
import android.os.PowerManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import fall2018.csc2017.pong.Ball;
import fall2018.csc2017.pong.PongFileSaverModel;
import fall2018.csc2017.pong.PongGameActivity;
import fall2018.csc2017.pong.PongGameController;
import fall2018.csc2017.pong.PongGameInfo;
import fall2018.csc2017.pong.Racket;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class PongGameControllerTest {
    PongGameController testController;
    PongFileSaverModel testSaver;
    PongGameActivity testGameActivity;
    private PongGameInfo testInfo;
    private Racket racket;
    private Ball ball;
    private RectF rectr;
    private RectF rectb;

    @Before
    public void setUp(){
        testInfo = mock(PongGameInfo.class);
        testSaver = mock(PongFileSaverModel.class);
        testGameActivity = mock(PongGameActivity.class);
        testInfo.lives = 2;
        racket = mock(Racket.class);
        ball = mock(Ball.class);
        rectr = mock(RectF.class);
        rectb = mock(RectF.class);
        testController = new PongGameController(testInfo, testGameActivity.getApplicationContext());
        testController.setPongSaver(testSaver);
        testController.setGameInfo(testInfo);
        when(testInfo.getFps()).thenReturn(1L);
        when(testInfo.getBall()).thenReturn(ball);
        when(testInfo.getRacket()).thenReturn(racket);
        when(racket.getRectF()).thenReturn(rectr);
        when(ball.getRectF()).thenReturn(rectb);
        doNothing().when(ball).update(isA(Long.class));
        doNothing().when(racket).update(isA(Long.class));
        when(testInfo.getScreenHeight()).thenReturn(30);
        when(testInfo.getScreenWidth()).thenReturn(110);
        doNothing().when(testSaver).updateAndSaveScoreboardIfGameOver(testController);

    }

    @Test
    public void updateRacketCollision() {
        rectr.left = 5;
        rectr.right = 30;
        rectr.bottom = 90;
        rectr.top = 10;
        rectb.left = 20;
        rectb.right = 100;
        rectb.top = 30;
        rectb.bottom = 20;
        doNothing().when(ball).setRandomXVelocity();
        doNothing().when(ball).reverseYVelocity();
        doNothing().when(ball).clearObstacleY(isA(Float.class));
        doNothing().when(testInfo).updateScore();
        doNothing().when(ball).increaseVelocity();

        testController.update();

        verify(ball).setRandomXVelocity();
        verify(ball).reverseYVelocity();
        verify(ball).clearObstacleY(8);
        verify(testInfo).updateScore();
        verify(ball).increaseVelocity();
    }

    @Test
    public void testBottomScreenCollision() {
        rectr.left = 110;
        rectr.right = 30;
        rectr.bottom = 90;
        rectr.top = 10;
        rectb.left = 20;
        rectb.right = 100;
        rectb.top = 30;
        rectb.bottom = 40;
        doNothing().when(ball).reverseYVelocity();
        doNothing().when(ball).clearObstacleY(isA(Float.class));
        doNothing().when(testInfo).updateLife();

        testController.update();
        verify(ball).reverseYVelocity();
        verify(ball).clearObstacleY(30 - 2);
        verify(ball, times(0)).reverseXVelocity();
        verify(ball, times(0)).clearObstacleX(isA(Float.class));

        verify(testInfo).updateLife();
        verify(testInfo, times(0)).updateScore();
    }

    @Test
    public void testTopScreenCollision() {
        rectr.left = 110;
        rectr.right = 30;
        rectr.bottom = 90;
        rectr.top = 10;
        rectb.left = 20;
        rectb.right = 100;
        rectb.top = -1;
        rectb.bottom = 20;
        doNothing().when(ball).reverseYVelocity();
        doNothing().when(ball).clearObstacleY(isA(Float.class));

        testController.update();
        verify(ball).reverseYVelocity();
        verify(ball).clearObstacleY(12);
        verify(ball, times(0)).reverseXVelocity();
        verify(ball, times(0)).clearObstacleX(isA(Float.class));
        assertFalse(testController.isOver());
    }

    @Test
    public void testLeftScreenCollision() {
        rectr.left = 110;
        rectr.right = 30;
        rectr.bottom = 90;
        rectr.top = 10;
        rectb.left = -1;
        rectb.right = 100;
        rectb.top = 15;
        rectb.bottom = 20;
        doNothing().when(ball).reverseXVelocity();
        doNothing().when(ball).clearObstacleX(isA(Float.class));

        testController.update();
        verify(ball).reverseXVelocity();
        verify(ball).clearObstacleX(2);
        verify(ball,times(0)).reverseYVelocity();
        verify(ball, times(0)).clearObstacleY(isA(Float.class));
        assertFalse(testController.isOver());
    }

    @Test
    public void testRightScreenCollision() {
        rectr.left = 150;
        rectr.right = 30;
        rectr.bottom = 90;
        rectr.top = 10;
        rectb.left = 20;
        rectb.right = 120;
        rectb.top = 15;
        rectb.bottom = 20;
        doNothing().when(ball).reverseXVelocity();
        doNothing().when(ball).clearObstacleX(isA(Float.class));

        testController.update();
        verify(ball).reverseXVelocity();
        verify(ball).clearObstacleX(110-22);
        verify(ball,times(0)).reverseYVelocity();
        verify(ball, times(0)).clearObstacleY(isA(Float.class));
        assertFalse(testController.isOver());
    }

    @Test
    public void testSetUpAndRestartAndZeroLives () {
        doNothing().when(ball).reset(110, 30);
        testInfo.lives = 0;
        when(testInfo.getLives()).thenReturn(testInfo.lives);
        doNothing().when(testInfo).setScore(isA(Integer.class));
        doNothing().when(testInfo).setLives(isA(Integer.class));

        testController.setupAndRestart();

        verify(testInfo).setScore(0);
        verify(testInfo).setLives(3);
    }

    @Test
    public void testSetUpAndRestartAndLivesRemain () {
        doNothing().when(ball).reset(110, 30);
        when(testInfo.getLives()).thenReturn(testInfo.lives);

        testController.setupAndRestart();

        verify(testInfo, times(0)).setScore(0);
        verify(testInfo, times(0)).setLives(3);
    }

    @Test
    public void testIsOVer(){
        testInfo.lives = 0;
        testController.update();

        assertFalse(testController.playing);
        assertTrue(testController.paused);
    }

    @Test
    public void testGetGameInfo(){
        PongGameInfo temp = testController.getGameInfo();
        assertEquals(true, temp == testInfo);
    }

    @Test
    public void testPausedAdnPlaying(){
        testController.setPaused(true);
        testController.setPlaying(true);

        assertTrue(testController.isPaused());
        assertTrue(testController.isPlaying());
    }

    @Test
    public void setupAndRestart() {
    }


}