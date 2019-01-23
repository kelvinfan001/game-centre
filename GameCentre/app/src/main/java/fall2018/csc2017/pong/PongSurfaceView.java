package fall2018.csc2017.pong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * SurfaceView of Pong game where ball and racket moves.
 * Since this only contains the method that keep calling update() and draw() from controller,
 * and onTouchEvent which is a user input, this class is excluded from Unit Test
 */
@SuppressLint("ViewConstructor")
public class PongSurfaceView extends SurfaceView implements Runnable {

    /**
     * A Thread that we will start and stop from the pause and resume methods.
     */
    Thread thread = null;

    /**
     * Size of vertical screen in pixels.
     */
    public int screenWidth;

    /**
     * Size of horizontal screen in pixels.
     */
    public int screenHeight;

    /**
     * Controller for this view
     */
    public PongGameController controller;

    public SurfaceHolder surfaceHolder;

    /**
     * GameInfo for the game
     */
    public PongGameInfo gameInfo;

    Canvas canvas;
    public Paint paint;

    /**
     * The current context
     */
    public Context context;

    /**
     * Constructor for PongSurfaceView.
     *
     * @param context      context of the PongGameActivity
     * @param screenWidth  width of screen
     * @param screenHeight height of screen
     * @param gameInfo     the PongGameInfo for Pong
     */
    public PongSurfaceView(Context context, int screenWidth, int screenHeight, PongGameInfo gameInfo) {
        super(context);
        this.context = context;

        // Initialize the width and height of the screen.
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Initialize the gameInfo
        this.gameInfo = gameInfo;
        this.surfaceHolder = getHolder();
        this.paint = new Paint();
        //Initialize the controller
        this.controller = new PongGameController(gameInfo, context);
    }

    /**
     * Returns gameInfo
     *
     * @return the gameInfo containing all the info about the game.
     */
    public PongGameInfo getGameInfo() {
        return controller.gameInfo;
    }

    @Override
    public void run() {
        while (controller.isPlaying()) {
            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // If not paused update the frame
            if (!controller.isPaused()) {
                controller.update();
            }
            // Draw the frame
            draw();

            // time it took in millisecond to update and draw
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;

            // updating fps
            if (timeThisFrame >= 1) { // so it doesn't divide by 0
                controller.gameInfo.setFps(1000 / timeThisFrame);
            }
        }

    }

    /**
     * displaying the objects in the screen by using Paint and Canvas.
     * We do this by making sure surfaceHolder and canvas is valid
     */
    public void draw() {
        if (this.surfaceHolder.getSurface().isValid()) {
            if (!controller.isPlaying()) {
                drawWhenGameOver();
            } else {
                drawDuringGame();
            }
        }
    }

    private void drawWhenGameOver() {
        this.canvas = this.surfaceHolder.lockCanvas();
        this.paint.setColor(Color.argb(255, 255, 255, 255));
        this.paint.setTextSize(60);
        this.canvas.drawText("Game Over! Score: " + gameInfo.getScore(), gameInfo.screenWidth / 4, gameInfo.screenHeight / 2, this.paint);
        this.paint.setTextSize(40);
        this.canvas.drawText("Tap to play one more time!", gameInfo.screenWidth / 4 + 10, gameInfo.screenHeight / 2 + 50, this.paint);
        this.surfaceHolder.unlockCanvasAndPost(this.canvas);
    }

    private void drawDuringGame() {
        this.canvas = this.surfaceHolder.lockCanvas();
        this.canvas.drawColor(Color.argb(255, 120, 197, 87));
        this.paint.setColor(Color.argb(255, 255, 255, 255));
        this.canvas.drawRect(gameInfo.getRacket().getRectF(), this.paint);
        this.canvas.drawRect(gameInfo.getBall().getRectF(), this.paint);
        this.paint.setColor(Color.argb(255, 255, 255, 255));
        this.paint.setTextSize(40);
        this.canvas.drawText("Score: " + gameInfo.getScore() + "  Lives: " + gameInfo.getLives(),
                10, 50, this.paint);
        this.surfaceHolder.unlockCanvasAndPost(this.canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                controller.setPaused(false);
                if (controller.isOver()) {
                    controller.setupAndRestart();
                    resume();
                }
                // Is the touch on the right or left?
                if (motionEvent.getX() > screenWidth / 2) {
                    controller.gameInfo.getRacket().setMovementState(controller.gameInfo.getRacket().RIGHT);
                } else {
                    controller.gameInfo.getRacket().setMovementState(controller.gameInfo.getRacket().LEFT);
                }

                break;
            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                controller.gameInfo.getRacket().setMovementState(controller.gameInfo.getRacket().STOPPED);
                break;
        }

        return true;
    }

    /**
     * When the game is paused, change 'playing' into false so the loop stops
     */
    public void pause() {
        controller.setPlaying(false);
        controller.setPaused(true);
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e("Error: ", "Joining Thread");
        }
    }

    /**
     * Set the playing into true and start updating the view
     */
    public void resume() {
        controller.setPlaying(true);
        thread = new Thread(this);
        thread.start();
    }

    public PongGameController getController(){
        return controller;
    }

    public void setController(PongGameController controller){
        this.controller = controller;
    }
}
