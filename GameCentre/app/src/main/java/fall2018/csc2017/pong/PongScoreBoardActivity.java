package fall2018.csc2017.pong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import fall2018.csc2017.slidingtiles.R;
import generalclasses.GameScoreboards;
import generalclasses.ScoreBoard;
import generalclasses.User;

/**
 *  Activity of the displaying Scoreboard of Pong game.
 */
public class PongScoreBoardActivity extends AppCompatActivity {

    /**
     * A HashMap of all the scores, where the key is the User and value is his/her score.
     */
    private LinkedHashMap<String, ArrayList<Integer>> scores;
    /**
     * A HashMap of the top 9 scores in 'scores', where the key is the User and value is his/her
     * score.
     */
    private LinkedHashMap<String, Integer> highScores = new LinkedHashMap<>();

    /**
     * The current user
     */
    private User user;
    private TextView display;
    GameScoreboards scoreboards;
    private Button globalButton;
    private Button localButton;
    private ScoreBoard scoreboard;
    private PongScoreBoardManager scoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pong_scoreboard_activity);
        setTheme();
        // Getting the values
        user = (User) getIntent().getSerializableExtra("user");
        // load the scoreboards from save file
        loadScoreboards();
        if (scoreboards == null) {
            scoreboards = new PongGameScoreBoards();
        }
        scoreboard = scoreboards.getScoreboard("Pong");
        if (scoreboard != null) {
            scores = scoreboard.getScoreMap();
        }

        scoreManager = new PongScoreBoardManager(this, scores, highScores);

        // Setting the Buttons and display
        globalButton = findViewById(R.id.pong_globalButton);
        localButton = findViewById(R.id.pong_localButton);
        display = findViewById(R.id.pong_display);

        addGlobalButtonListener();
        addLocalButtonListener();
    }

    /**
     * If there was a specific theme chosen by the user, set the background
     * into that theme
     */
    private void setTheme() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        // the second parameter will be fallback if the preference is not found
        int bg = sharedPref.getInt("background_resources", android.R.color.white);
        getWindow().setBackgroundDrawableResource(bg);
    }

    private void addGlobalButtonListener() {
        globalButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (scoreboard != null) {
                    scoreManager.displayGlobalRankings();
                } else {
                    scoreManager.displayBlankRankings();
                }
                display.setText("Global Rankings");
            }
        });
    }

    private void addLocalButtonListener() {
        localButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (scoreboard != null) {
                    if (scores.containsKey(user.getName())) {
                        scoreManager.displayLocalRankings(user.getName());
                    } else {
                        scoreManager.displayBlankRankings();
                    }
                }
                display.setText("Local Rankings");
            }
        });
    }

    /**
     * Load the scoreboard saved previously (If exists)
     */
    private void loadScoreboards() {
        try {
            InputStream inputStream = this.openFileInput("SAVED_PONG_SCOREBOARDS");
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                scoreboards = (GameScoreboards) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

}
