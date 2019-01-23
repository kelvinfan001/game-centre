package fall2018.csc2017.pong;

import android.content.Context;
import android.graphics.RectF;

import java.io.Serializable;

import generalclasses.GameController;

/**
 * Controller acts on both model and view. It controls the data flow into model object and updates
 * the view whenever data changes. It keeps view and model separate.
 */
public class PongGameController implements GameController, Serializable {

    /**
     * PongGameInfo
     */
    public PongGameInfo gameInfo;

    /**
     * Whether or not game is running (from thread).
     * Volatile because it is accessed from inside and outside the thread
     */
    public volatile boolean playing;

    /**
     * Whether or not the game is paused.
     */
    public boolean paused = true;

    private PongFileSaverModel pongSaver;


//     Sound FX
//    SoundPool sp;
//    int beep1ID = -1;
//    int beep2ID = -1;
//    int beep3ID = -1;
//    int loseLifeID = -1;
    /**
     * Returns true iff the two specified rectangles intersect. In no event are
     * either of the rectangles modified. To record the intersection,
     * use intersect() or setIntersect().
     *
     * @param a The first rectangle being tested for intersection
     * @param b The second rectangle being tested for intersection
     * @return true iff the two specified rectangles intersect. In no event are
     *              either of the rectangles modified.
     */
    private boolean intersects(RectF a, RectF b) {
        return a.left < b.right && b.left < a.right
                && a.top < b.bottom && b.top < a.bottom;
    }

    /**
     * Initializes PongGameController
     */
    public PongGameController(PongGameInfo gameInfo, Context context) {
        // Initialize a PongGameInfo
        this.gameInfo = gameInfo;
        this.pongSaver = new PongFileSaverModel(context);

    }

    /**
     * Updates the rect of the ball and racket (Make move pretty much)
     */
    public void update() {
        // Move the racket if required
        gameInfo.getRacket().update(gameInfo.getFps());
        gameInfo.getBall().update(gameInfo.getFps());

        // If ball colliding with racket
        if (intersects(gameInfo.getRacket().getRectF(), gameInfo.getBall().getRectF())) {
            gameInfo.getBall().setRandomXVelocity();
            gameInfo.getBall().reverseYVelocity();
            gameInfo.getBall().clearObstacleY(gameInfo.getRacket().getRectF().top - 2);

            gameInfo.updateScore();
            gameInfo.getBall().increaseVelocity();

            // sp.play(beep1ID, 1, 1, 0, 0, 1);
        }

        // If ball hits bottom of the screen
        if (gameInfo.getBall().getRectF().bottom > gameInfo.getScreenHeight()) {
            gameInfo.getBall().reverseYVelocity();
            gameInfo.getBall().clearObstacleY(gameInfo.getScreenHeight() - 2);

            // Lose a life
            gameInfo.updateLife();
            // sp.play(loseLifeID, 1, 1, 0, 0, 1);
        }
        // If ball hits top of the screen
        if (gameInfo.getBall().getRectF().top < 0) {
            gameInfo.getBall().reverseYVelocity();
            gameInfo.getBall().clearObstacleY(12);

            // sp.play(beep2ID, 1, 1, 0, 0, 1);
        }

        // If ball hits left of the screen
        if (gameInfo.getBall().getRectF().left < 0) {
            gameInfo.getBall().reverseXVelocity();
            gameInfo.getBall().clearObstacleX(2);

            // sp.play(beep3ID, 1, 1, 0, 0, 1);
        }

        // If ball hits right of the screen
        if (gameInfo.getBall().getRectF().right > gameInfo.getScreenWidth()) {
            gameInfo.getBall().reverseXVelocity();
            gameInfo.getBall().clearObstacleX(gameInfo.getScreenWidth() - 22);

            // sp.play(beep3ID, 1, 1, 0, 0, 1);
        }

        if (isOver()) {
            paused = true;
            playing = false;
            pongSaver.updateAndSaveScoreboardIfGameOver(this);
        }
    }

    /**
     * Restart the game.
     */
    public void setupAndRestart() {
        // Put the ball back to the start
        gameInfo.getBall().reset(gameInfo.getScreenWidth(), gameInfo.getScreenHeight());
        // if game over reset scores and lives
        if (gameInfo.getLives() == 0) {
            gameInfo.setScore(0);
            gameInfo.setLives(3);
        }
    }

    public void setPongSaver(PongFileSaverModel saver){
        this.pongSaver = saver;
    }

    public void setGameInfo(PongGameInfo info){
        this.gameInfo = info;
    }

    public PongGameInfo getGameInfo() {
        return gameInfo;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isPlaying(){
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }

    public boolean isOver() {
        return gameInfo.lives == 0;
    }
}
