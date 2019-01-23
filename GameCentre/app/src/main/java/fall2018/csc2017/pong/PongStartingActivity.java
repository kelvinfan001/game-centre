package fall2018.csc2017.pong;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import fall2018.csc2017.slidingtiles.R;
import generalclasses.GameInfo;
import generalclasses.User;

/**
 * Menu of Pong game.
 * Since this only contains buttons and file methods (Models), this Activity will be excluded
 * from Unit test
 */
public class PongStartingActivity extends AppCompatActivity {

    /**
     * Button to start new game
     */
    private Button pongGame;

    /**
     * Button to go to scoreboards
     */
    private Button pongScore;

    /**
     * Button to load game
     */
    private Button pongLoad;

    /**
     * current user's username.
     */
    private String username;

    /**
     * User class of user.
     */
    private User user;

    public static final String SAVE_FILENAME = "master_save_file.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pong_main_menu);
        setTheme();
        declarations();
        addPongGameButtonListener();
        user = User.usernameToUser.get(username);
        addPongLoadButtonListener();
        addPongScoreButtonListener();
    }

    private void declarations() {
        pongGame = findViewById(R.id.new_pong);
        pongScore = findViewById(R.id.pong_score);
        pongLoad = findViewById(R.id.pong_load);
        username = getIntent().getStringExtra("username");
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

    /**
     * Onclick listener for PongGameButton
     */
    private void addPongGameButtonListener() {
        pongGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PongStartingActivity.this, PongGameActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }


    private void addPongScoreButtonListener() {
        pongScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PongStartingActivity.this, PongScoreBoardActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    /**
     * Get all the save names
     *
     * @param saves the hash map of all the existing saves
     * @return a string array of save names
     */
    private String[] getSaveNames(HashMap<String, GameInfo> saves) {
        ArrayList<String> tempResult = new ArrayList<>(saves.keySet());
        return tempResult.toArray(new String[0]);
    }

    /**
     * Onclick listener for PongLoadButton
     */
    private void addPongLoadButtonListener() {
        pongLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(PongStartingActivity.this);
                alert.setTitle("Select your save");
                alert.setIcon(android.R.drawable.ic_dialog_alert);

                final HashMap<String, GameInfo> saves = user.getSavesForGame("Pong");
                final String[] saveNames = getSaveNames(saves);
                alert.setItems(saveNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        loadSaveAndBegin(saves, saveNames, i);
                    }
                });
                alert.show();
            }
        });
    }

    /**
     * Get the save that corresponds to index i in saveNames, and load that
     * save to begin PongGameActivity.
     *
     * @param saves     the hash map that details all the user's saves.
     * @param saveNames string list of all save names with correct complexity.
     * @param i         the index used to get the desired save in
     *                  saveNamesWithCorrectComplexity.
     */
    private void loadSaveAndBegin(HashMap<String, GameInfo> saves, String[] saveNames, int i) {
        String saveName = saveNames[i];

        // get the corresponding save
        PongGameInfo saveToLoad = (PongGameInfo) saves.get(saveName);

        Intent intent = new Intent(PongStartingActivity.this, PongGameActivity.class);
        intent.putExtra("saveToLoad", saveToLoad);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // load most updated copy of user
        try {
            InputStream inputStream = this.openFileInput(SAVE_FILENAME);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                User.usernameToUser = (HashMap<String, User>) input.readObject();
                user = User.usernameToUser.get(username);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: "
                    + e.toString());
        }
    }
}
