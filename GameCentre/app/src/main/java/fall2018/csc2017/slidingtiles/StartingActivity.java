package fall2018.csc2017.slidingtiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import generalclasses.GameInfo;
import generalclasses.GameScoreboards;
import generalclasses.ScoreBoard;
import generalclasses.User;

//View Class for choosing game difficulty, not tested.
/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivity extends AppCompatActivity {

    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "master_save_file.ser";

    /**
     * The board manager.
     */

    private String username;
    private User user;
    private String game = "Sliding Tiles";
    private String complexity;
    private GameScoreboards scoreboards;
    private SlidingTilesFileSaverModel mSaver;
    private GameActivityController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        username = getIntent().getStringExtra("username");

        super.onCreate(savedInstanceState);

        mSaver = new SlidingTilesFileSaverModel(this);
        mController = new GameActivityController(this);
        complexity = getIntent().getStringExtra("complexity");
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int bg = sharedPref.getInt("background_resources", android.R.color.white); // the second parameter will be fallback if the preference is not found
        getWindow().setBackgroundDrawableResource(bg);
        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addScoreBoardButtonListener();
    }

    /**
     * Added a function for the scoreboard
     */
    private void addScoreBoardButtonListener() {
        Button scoreboard = findViewById(R.id.scoreboard);
        scoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingActivity.this,
                        SlidingTilesScoreBoardActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("complexity", complexity);
                startActivity(intent);
            }
        });
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instantiateGameandBegin();
            }
        });
    }


    /**
     * Make a new game, then begin a game activity with the new game.
     */
    private void instantiateGameandBegin() {
        SlidingTilesGameInfo newGameInfo = new SlidingTilesGameInfo();
        newGameInfo.setComplexity(complexity);
        newGameInfo.setUserName(username);

        mController.instantiateGameandBegin(complexity);

        Intent intent = new Intent(StartingActivity.this, GameActivity.class);
        intent.putExtra("saveToLoad", newGameInfo);
        startActivity(intent);
    }


    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {

        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(StartingActivity.this);
                alert.setTitle("Select your save");
                alert.setIcon(android.R.drawable.ic_dialog_alert);

                final HashMap<String, GameInfo> saves = user.getSavesForGame(game);
                final String[] saveNamesWithCorrectComplexity = getSaveNamesComplexity(complexity,
                        saves);
                alert.setItems(saveNamesWithCorrectComplexity, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        loadSaveAndBegin(saves, saveNamesWithCorrectComplexity, i);
                    }
                });
                alert.show();
            }
        });
    }


    /**
     * Get the save that corresponds to index i in saveNamesWithCorrectComplexity, and load that
     * save to begin GameActivity.
     *
     * @param saves                          the hash map that details all the user's saves.
     * @param saveNamesWithCorrectComplexity string list of all save names with correct complexity.
     * @param i                              the index used to get the desired save in
     *                                       saveNamesWithCorrectComplexity.
     */
    private void loadSaveAndBegin(HashMap<String, GameInfo> saves,
                                  String[] saveNamesWithCorrectComplexity, int i) {
        // get the corresponding save
        String saveName = saveNamesWithCorrectComplexity[i];
        GameInfo saveToLoad = (GameInfo) saves.get(saveName);
        makeToastLoadedText();

        // start the game with the correct GameInfo
        Intent intent = new Intent(StartingActivity.this, GameActivity.class);
        intent.putExtra("saveToLoad", saveToLoad);
        startActivity(intent);
    }


    /**
     * Get all the save names that have a given complexity.
     *
     * @param complexity the complexity to search for
     * @param saves      the hash map of all the existing saves
     * @return a string array of save names that have complexity complexity
     */
    private String[] getSaveNamesComplexity(String complexity, HashMap<String, GameInfo> saves) {

        return mSaver.getSaveNamesComplexity(complexity, saves);
    }


    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }


    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // load most updated copy of user
        mSaver.startingResume(username);
        user = mSaver.getUser();
        // also load the most recent copy of scores
        scoreboards = mSaver.getScoreboards();

    }
}

