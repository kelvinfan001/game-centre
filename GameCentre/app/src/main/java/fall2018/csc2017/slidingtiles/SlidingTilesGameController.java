package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.widget.Toast;

import generalclasses.GameController;
import generalclasses.User;

/**
 * A controller in charge of processing an action of a user.
 */
class SlidingTilesGameController implements GameController {

    private SlidingTilesManager slidingTilesManager = null;

    /**
     * Creates an instance of the controller.
     */
    SlidingTilesGameController() {
    }

    /**
     * Sets a slidingTilesManager for this controller.
     *
     * @param slidingTilesManager manager responsible for the game grid.
     */
    void setSlidingTilesManager(SlidingTilesManager slidingTilesManager) {
        this.slidingTilesManager = slidingTilesManager;
    }

    /**
     * Checks if a move is valid, and then proceeds to do so. If not, an invalid move message is shown.
     *
     * @param context  GameActivity context.
     * @param position the position the user wants to swap the blank tile with.
     */
    void processTapMovement(Context context, int position) {
        if (slidingTilesManager.isValidMove(position)) {

            // save the previous move into previousMovesList
            slidingTilesManager.savePreviousMovePosition(this.getPositionToMoveTo());

            // Make move
            slidingTilesManager.makeMove(position);


            if (slidingTilesManager.isOver()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else { // if tap is not valid
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Determine the position the current tap moves to, if valid, and return it.
     *
     * @return the position to move to.
     */
    private int getPositionToMoveTo() {
        int[] newCoordinatePosition;
        int newPosition;

        // get coordinate position of blank tile
        newCoordinatePosition = slidingTilesManager.findBlankTile(25);

        // get standard position of blank tile
        newPosition = SlidingTilesManager.convertCoordinateToPosition(newCoordinatePosition);

        return newPosition;
    }
}