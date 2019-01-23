package fall2018.csc2017.PongTest;


import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import fall2018.csc2017.pong.Ball;

import static org.junit.Assert.*;


public class BallTest {

    @Test
    public void update() {
        Ball testBall = new Ball(1080, 1920);
        testBall.update(10);
        assertEquals(true,testBall.getRectF().left == (1080/2) + ((1920/4) / 10));
        assertEquals(true, testBall.getRectF().top == (1920/10) + ((1920/4) / 10));
        assertEquals(true,testBall.getRectF().right == testBall.getRectF().left + (1080 / 100));
        assertEquals(true,testBall.getRectF().bottom == testBall.getRectF().top - (1080 / 100));
    }


    @Test
    public void reset(){
        Ball testBall = new Ball(1080, 1920);
        int x = 100;
        int y = 200;
        testBall.reset(x, y);
        assertEquals(true,testBall.getRectF().left == x / 2);
        assertEquals(true,testBall.getRectF().top == y / 10);
        assertEquals(true,testBall.getRectF().right == x / 2 + (1080/100));
        assertEquals(true,testBall.getRectF().bottom == y / 10 - (1080/100));
    }


    @Test
    public void clearObstacleY() {
        Ball testBall = new Ball(1080, 1920);
        int y = 100;
        testBall.clearObstacleY(y);
        assertEquals(true,testBall.getRectF().bottom == y);
        assertEquals(true,testBall.getRectF().top == y - (1080 / 100));
    }

    @Test
    public void clearObstacleX() {
        Ball testBall = new Ball(1080, 1920);
        int x = 100;
        testBall.clearObstacleX(x);
        assertEquals(true,testBall.getRectF().left == x);
        assertEquals(true,testBall.getRectF().right == x + (1080 / 100));
    }

    @Test
    public void setRandomXVelocity(){
        Ball testBall = new Ball(1080, 1920);
        float originalXVelocity = testBall.getXVelocity();
        testBall.setRandomXVelocity();
        assertEquals(true,testBall.getXVelocity() == -originalXVelocity || testBall.getXVelocity() == originalXVelocity);
    }

    @Test
    public void increaseVelocity(){
        Ball testBall = new Ball(1080, 1920);
        float originalXVelocity = testBall.getXVelocity();
        float originalYVelocity = testBall.getYVelocity();
        testBall.increaseVelocity();
        assertEquals(true,testBall.getXVelocity() == originalXVelocity + originalXVelocity/10);
        assertEquals(true, testBall.getXVelocity() == originalYVelocity + originalYVelocity/10);
    }
}