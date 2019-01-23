package fall2018.csc2017.slidingtiles;

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
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import generalactivities.ScoreBoardActivity;
import generalclasses.GameScoreboards;
import generalclasses.ScoreBoard;
import generalclasses.User;

//View class for ScoreBoard, not tested.
/**
 * The Activity for the Scoreboard.
 */
public class SlidingTilesScoreBoardActivity extends ScoreBoardActivity {
    /**
     * The current user
     */
    private User user;
    private TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_sliding_tiles_score_board);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        int bg = sharedPref.getInt("background_resources", android.R.color.white); // the second parameter will be fallback if the preference is not found
        getWindow().setBackgroundDrawableResource(bg);
        // Getting the values
        user = (User) getIntent().getSerializableExtra("user");
        String complexity = getIntent().getStringExtra("complexity");

        // load the scoreboards from save file//
        loadScoreboards("SAVED_SCOREBOARDS");
        if (scoreboards == null) {
            scoreboards = new SlidingTileScoreboards();
        }
        final ScoreBoard scoreboard = scoreboards.getScoreboard(complexity);
        if (scoreboard != null) {
            scores = scoreboard.getScoreMap();
        }


        // Getting the Buttons and display
        Button globalButton = findViewById(R.id.globalButton);
        Button localButton = findViewById(R.id.localButton);
        display = findViewById(R.id.display);

        // Assigning the button to display global rankings (all users scores and rankings)
        globalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scoreboard != null) {
                    displayGlobalRankings();
                }
                display.setText("Global Rankings");
            }
        });

        // Assigning the button to display local rankings depending on which user is logged in
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scoreboard != null) {
                    if (scores.containsKey(user.getName())) {
                        displayLocalRankings(user.getName());
                    } else {
                        displayBlankRankings();
                    }
                }
                display.setText("Local Rankings");
            }
        });
    }

}
