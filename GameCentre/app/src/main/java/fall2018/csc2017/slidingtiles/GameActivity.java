package fall2018.csc2017.slidingtiles;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import generalclasses.User;

import static fall2018.csc2017.slidingtiles.StartingActivity.SAVE_FILENAME;

//View Class for the main sliding tiles game, not tested.
/**
 * The game activity, also the View.
 */
public class GameActivity extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private SlidingTilesManager slidingTilesManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * List of split image given by users.
     */
    public static Bitmap[] backgrounds;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    /**
     * boolean whether the complexity is Image or not.
     */
    public static boolean image_game = false;
    protected static SlidingTilesGameInfo gameInfo;

    private User user;

    private GameActivityController mController;
    private SlidingTilesFileSaverModel mSaver;

    /**
     * Returns User.
     * @return User
     */
    public User getUser() {
        return user;
    }
    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mController = new GameActivityController(this);
        mSaver = new SlidingTilesFileSaverModel(this);
        gameInfo = (SlidingTilesGameInfo) getIntent().getSerializableExtra("saveToLoad");
        // make manager, then set game info
        slidingTilesManager = new SlidingTilesManager();
        slidingTilesManager.setInfo(gameInfo);

        // determine current user
        user = User.usernameToUser.get(gameInfo.getUserName());

        createTileButtons();
        setContentView(R.layout.activity_main);
        addUndoButtonListener();
        addSaveButtonListener();

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(Board.NUM_COLS);
        gridView.setSlidingTilesManager(slidingTilesManager);
        slidingTilesManager.getBoard().addObserver(this);

        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / Board.NUM_COLS;
                        columnHeight = displayHeight / Board.NUM_ROWS;

                        display();
                    }
                });
    }

    /**
     * Create the buttons for displaying the tiles.
     */
    private void createTileButtons() {
        Board board = slidingTilesManager.getBoard();
        tileButtons = mController.createTileButtons(image_game, board);

    }
    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = slidingTilesManager.getBoard();
        mController.updateTileButtons(tileButtons, image_game, board);
        autosave();
        updateAndSaveScoreboardIfGameOver();
    }

    /**
     * Update and save the user's score to a scoreboard if the game is over.
     */
    private void updateAndSaveScoreboardIfGameOver() {
        mController.updateAndSaveScoreboardIfGameOver(slidingTilesManager);
    }


    /**
     * Create new or replace a save named "Autosave".
     */
    private void autosave() {
        String game = slidingTilesManager.getInfo().getGame();
        user.addSave(game, "Autosave", gameInfo); //gameInfo contains the state of sliding tiles.
        mSaver.saveToFile(SAVE_FILENAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void update(Observable o, Object arg) {
        display();
    }

    /**
     * Add undo button.
     */
    public void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.UndoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numPreviousMoves = gameInfo.previousMovesList.size(); // get number of moves made
                mController.undoBtn(numPreviousMoves, slidingTilesManager);
            }
        });
    }

    /**
     * Add save button
     */
    public void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // look at the current game info from GameActivity
                SlidingTilesGameInfo gameInfo = GameActivity.gameInfo;
                mSaver.saveButtonListener(gameInfo, getUser());
            }
        });
    }

}

//