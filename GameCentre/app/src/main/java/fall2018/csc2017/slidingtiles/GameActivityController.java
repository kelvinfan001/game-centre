package fall2018.csc2017.slidingtiles;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;

import java.util.ArrayList;

import generalclasses.ScoreBoard;

/**
 * Controller for GameActivity
 */
class GameActivityController {
    private Context context;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    private SlidingTilesFileSaverModel mSaver;

    /**
     * Setting a SaverModel responsible for saving/loading files.
     * @param mSaver a SaverModel object.
     */
    void setmSaver(SlidingTilesFileSaverModel mSaver) {
        this.mSaver = mSaver;
    }

    /**
     * Setting an array of Tile Buttons for the sliding tiles game.
     * @param tileButtons an array of Tile Buttons.
     */
    void setTileButtons(ArrayList<Button> tileButtons) {
        this.tileButtons = tileButtons;
    }

    /**
     * A controller responsible for the logic of sliding tiles game activity.
     * @param context the game activity.
     */
    GameActivityController(Context context) {
        this.context = context;
        SlidingTilesFileSaverModel saver = new SlidingTilesFileSaverModel(context);
        setmSaver(saver);
    }

    /**
     * Create the Tile Buttons of the grid view for the sliding game.
     * @param image_game Whether the game is swapping images/tiles
     * @param board A board object containing all the Tiles.
     * @return an array of TileButtons.
     */
    ArrayList<Button> createTileButtons(boolean image_game, Board board) {
        tileButtons = new ArrayList<>();

        if (image_game) { // If the complexity is image, creates buttons differently
            int num;
            for (int row = 0; row != Board.NUM_ROWS; row++) {
                for (int col = 0; col != Board.NUM_COLS; col++) {
                    num = board.getTile(row, col).getId() - 1;
                    helperCreatingButton(num, row, col, board);
                }
            }
            return tileButtons;
        } else {
            for (int row = 0; row != Board.NUM_ROWS; row++) {
                for (int col = 0; col != Board.NUM_COLS; col++) {
                    Button tmp = new Button(context);
                    tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                    this.tileButtons.add(tmp);
                }
            }
            return tileButtons;
        }
    }

    /**
     * Helper function for creating a button when complexity is image
     *
     * @param num id of the tile
     * @param row row of the creating tile
     * @param col col of the creating tile
     */
    void helperCreatingButton(int num, int row, int col, Board board) {
        Button tmp = new Button(context);
        if (num != 24) {
            BitmapDrawable bmp = new BitmapDrawable(GameActivity.backgrounds[num]);
            tmp.setBackground(bmp);
            this.tileButtons.add(tmp);
        } else {
            tmp.setBackgroundResource(board.getTile(row, col).getBackground());
            this.tileButtons.add(tmp);
        }
    }

    /**
     * Update the button's background
     *
     * @param tilesbtn   List of buttons that we assign the background
     * @param image_game If game is Image game or not
     * @param board      The board of this game that we pug the buttons
     */
    void updateTileButtons(ArrayList<Button> tilesbtn, boolean image_game, Board board) {

        if (image_game) { // If the complexity is image, updates buttons differently
            int next = 0;
            for (Button b : tilesbtn) {
                helperUpdate(b, next, board);
                next++;
            }
        } else {
            int nextPos = 0;
            for (Button b : tilesbtn) {
                int row = nextPos / Board.NUM_ROWS;
                int col = nextPos % Board.NUM_COLS;
                b.setBackgroundResource(board.getTile(row, col).getBackground());
                nextPos++;
            }
        }
    }

    /**
     * Helper function for updating the background of the tile when complexity is image
     *
     * @param b    current button that is being updated
     * @param next the position of the tile that is being updated
     */
    void helperUpdate(Button b, int next, Board board) {
        int row = next / Board.NUM_ROWS;
        int col = next % Board.NUM_COLS;
        int num = board.getTile(row, col).getId() - 1;
        if (num != 24) {
            BitmapDrawable bmp = new BitmapDrawable(GameActivity.backgrounds[num]);
            b.setBackground(bmp);
        } else {
            b.setBackgroundResource(board.getTile(row, col).getBackground());
        }
    }

    /**
     * Undo functionality that allows user to go back to their previous position.
     * @param numPreviousMoves number of undos a user can make.
     * @param slidingTilesManager a SlidingTileManager helps to undo a move.
     */
    void undoBtn(int numPreviousMoves, SlidingTilesManager slidingTilesManager) {
        if ((numPreviousMoves) != 0) { // check whether any moves were made
            int previousMove = slidingTilesManager.returnPreviousMove();
            slidingTilesManager.makeMove(previousMove);
        }
    }

    /**
     * Update and save a user's score once they win.
     * @param slidingTilesManager a manager that provides this user's score and complexity of the game.
     */
    void updateAndSaveScoreboardIfGameOver(SlidingTilesManager slidingTilesManager) {

        if (slidingTilesManager.isOver()) {
            // Getting the info needed to display on scoreboard
            String username = slidingTilesManager.getInfo().getUserName();
            int score = slidingTilesManager.getInfo().getScore();
            String complexity = slidingTilesManager.getInfo().getComplexity();

            // assume we have loaded scoreboards and have the correct scoreboard
            mSaver.loadScoreboards("SAVED_SCOREBOARDS");
            ScoreBoard scoreboard = mSaver.scoreboards.getScoreboard(complexity);

            if (scoreboard.getScoreMap().containsKey(username)) {
                // if user already has a score
                scoreboard.addScore(username, score);
            } else { // if user doesn't have a score
                scoreboard.addUserAndScore(username, score);
            }
            // save scoreboard
            mSaver.scoreboards.addScoreboard(complexity, scoreboard);
            mSaver.saveScoreboards(mSaver.scoreboards, "SAVED_SCOREBOARDS");
        }
    }

    /**
     * Beginning of a new game, create a new user scoreboard.
     * @param complexity the difficulty of the game.
     */
    void instantiateGameandBegin(String complexity) {
        mSaver.loadScoreboards("SAVED_SCOREBOARDS");
        if (mSaver.scoreboards == null) {
            mSaver.scoreboards = new SlidingTileScoreboards();
        }
        if (mSaver.scoreboards.getScoreboard(complexity) == null) { // if no one has won a game
            mSaver.scoreboards.addScoreboard(complexity, new ScoreBoard());
            mSaver.saveScoreboards(mSaver.scoreboards, "SAVED_SCOREBOARDS");
            // in subsequent games, however, there is no need for this
        }
    }
}
