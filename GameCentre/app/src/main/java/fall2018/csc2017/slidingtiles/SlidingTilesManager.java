package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

import generalclasses.Manager;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class SlidingTilesManager extends Manager implements Serializable {

    /**
     * The info being managed.
     */
    private SlidingTilesGameInfo info;

    /**
     * Return the current board.
     */
    public Board getBoard() {
        return info.getBoard();
    }


    /**
     * Sets a SlidingTilesGameInfo object.
     *
     * @param info the manager's game information.
     */
    public void setInfo(SlidingTilesGameInfo info) {
        this.info = info;
    }


    public SlidingTilesGameInfo getInfo() {
        return this.info;
    }

    /**
     * Manage a new shuffled board.
     */
    SlidingTilesManager() {
        info = new SlidingTilesGameInfo();
    }


    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    public boolean isOver() {
        boolean solved = true;
        Tile previousTile = info.getBoard().getTile(0, 0); // starting tile.
        for (Tile currentTile : info.getBoard()) {
            if (currentTile.compareTo(previousTile) > 0) { // if previousTile's id is bigger.
                solved = false;
            }
            previousTile = currentTile; // update the tile.

        }
        return solved;
    }


    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidMove(Object position) {

        int row = (Integer) position / Board.NUM_COLS;
        int col = (Integer) position % Board.NUM_COLS;
        int blankId = 25; // Modified the blankID to be 25
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : info.getBoard().getTile(row - 1, col);
        Tile below = row == Board.NUM_ROWS - 1 ? null : info.getBoard().getTile(row + 1, col);
        Tile left = col == 0 ? null : info.getBoard().getTile(row, col - 1);
        Tile right = col == Board.NUM_COLS - 1 ? null : info.getBoard().getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }


    /**
     * Finds the index for the row and col of the Blank Tile in board.
     *
     * @param blankId is the id of the blank tile.
     * @return int[] where the row is at 0th index and col is at 1st index.
     */
    int[] findBlankTile(int blankId) {

        int[] ret = new int[2];
        // looping through all of the tiles in board to find an id matching with blankId.
        for (int row = 0; row != Board.NUM_ROWS; row++) {
            for (int col = 0; col != Board.NUM_COLS; col++) {
                if (info.getBoard().getTile(row, col).getId() == blankId) {
                    ret[0] = row;
                    ret[1] = col;
                }
            }
        }
        return ret;
    }


    /**
     * Process a touch (move) at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    public void makeMove(Object position) {

        int row = (Integer) position / Board.NUM_ROWS;
        int col = (Integer) position % Board.NUM_COLS;
        int blankId = 25; // Modified the blankID to be 25
        int[] blankTile = findBlankTile(blankId);
        int blankRow = blankTile[0]; // look at javadoc for findBlankTile.
        int blankCol = blankTile[1]; // look at javadoc for findBlankTile.
        if (this.isValidMove(position)) {
            info.getBoard().swapTiles(row, col, blankRow, blankCol);
            info.updateScore();
        }
    }


    /**
     * Save the current state of GameInfo into its previousGameInfo list. This is necessary for the
     * undo functionality.
     */
    void savePreviousMovePosition(int position) {
        info.previousMovesList.add(position);
    }

    /**
     * Get the previous GameInfo. The undo function should make use of this method.
     *
     * @return last index of info.previousGameInfosList.
     */
    Integer returnPreviousMove() {
        int numPreviousGames = info.previousMovesList.size();
        if (numPreviousGames != 0) {
            int ret = info.previousMovesList.get(numPreviousGames - 1); // save the last element
            info.previousMovesList.remove(numPreviousGames - 1); // remove the last element from list
            return ret; // return the last element
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }


    /**
     * Convert the position in coordinate form to standard form.
     *
     * @param coordinate the position in coordinate form, represented in an integer array.
     * @return the position in standard form represent by an Integer.
     */
    public static Integer convertCoordinateToPosition(int[] coordinate) {
        return coordinate[0] * Board.NUM_COLS + coordinate[1];
    }
}
