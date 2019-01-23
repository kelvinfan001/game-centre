package fall2018.csc2017.pong;
import android.graphics.RectF; // https://developer.android.com/reference/android/graphics/RectF

import java.io.Serializable;
import java.util.Random;

public class Ball implements Serializable {

    /**
     * Coordinates of the ball on the screen
     */
    private SerializableRectF rect;

    /**
     * Velocity in the X direction
     */
    private float XVelocity;

    /**
     * Velocity in the Y direction
     */
    private float YVelocity;

    /**
     * Width of Ball
     */
    private float BallWidth;

    /**
     * Height of Ball
     */
    private float BallHeight;



    /**
     * Height of the screen
     */
    private int screenHeight;

    /**
     * Constructor for Ball
     * @param screenX Width of screen
     * @param screenY Height of screen
     */
    public Ball(int screenX, int screenY) {
        screenHeight = screenY;

        // Make the ball size relative to the screen resolution
        BallHeight = BallWidth = screenX / 100;

        // Start the ball travelling straight up
        // at a quarter of the screen height per second
        YVelocity = screenY / 4;
        XVelocity = screenY / 4;

        // Initialize a SerializableRectF and set it's starting coordinates.
        RectF temp = new RectF();
        rect = new SerializableRectF(temp);
        rect.getRectF().left = screenX / 2;
        rect.getRectF().top = screenY / 10;
        rect.getRectF().right = screenX / 2 + BallWidth;
        rect.getRectF().bottom = screenY / 10 - BallHeight;
    }

    /**
     * Returns the RectF of the SerializableRectF
     * @return rect, the SerializableRectF containing Rect
     */
    public RectF getRectF() {
        return rect.getRectF();
    }

    public float getXVelocity() {
        return XVelocity;
    }

    public float getYVelocity() {
        return YVelocity;
    }

    /**
     * Updates the position of the ball in each frame.
     * @param fps fps of device
     */
    public void update(long fps) {
        // Use fps to calculate so that it moves at consistent speed among all devices.
        rect.getRectF().left = rect.getRectF().left + (XVelocity / fps);
        rect.getRectF().top = rect.getRectF().top + (YVelocity / fps);
        rect.getRectF().right = rect.getRectF().left + BallWidth;
        rect.getRectF().bottom = rect.getRectF().top - BallHeight;
    }

    /**
     * Reverses the YVelocity
     */
    public void reverseYVelocity(){
        YVelocity = -YVelocity;
    }

    /**
     * Reverses the XVelocity
     */
    public void reverseXVelocity(){
        XVelocity = -XVelocity;
    }

    /**
     * Generate a random velocity
     */
    public void setRandomXVelocity(){
        // Generate either 0 or 1
        Random generator = new Random();
        int num = generator.nextInt(2);

        if(num == 0){
            reverseXVelocity();
        }
    }

    /**
     * Increases velocity by 10%
     */
    public void increaseVelocity(){
        XVelocity += XVelocity / 10;
        YVelocity += YVelocity / 10;
    }

    /**
     * Clears an obstacle on vertical axis
     * @param y coordinate
     */
    public void clearObstacleY(float y){
        rect.getRectF().bottom = y;
        rect.getRectF().top = y - BallHeight;
    }

    /**
     * Clears an obstacle on horizontal axis
     * @param x coordinate
     */
    public void clearObstacleX(float x){
        rect.getRectF().left = x;
        rect.getRectF().right = x + BallWidth;
    }

    /**
     * Reset coordinate of ball to the bottom center of the screen
     * @param x coordinate
     * @param y coordinate
     */
    public void reset(int x, int y){
        rect.getRectF().left = x / 2;
        rect.getRectF().top = y / 10;
        rect.getRectF().right = x / 2 + BallWidth;
        rect.getRectF().bottom = y / 10 - BallHeight;
        YVelocity = XVelocity = screenHeight / 4;
    }
}
