package fall2018.csc2017.pong;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import generalclasses.GameScoreboards;
import generalclasses.ScoreBoard;

public class PongFileSaverModel {

    /**
     * Context that is passed in
     */
    private Context context;

    /**
     * GameScoreBoards
     */
    private GameScoreboards scoreboards;

    /**
     * Constructor for PongFileSaverModel
     * @param context from the class that calls this constructor
     */
    PongFileSaverModel(Context context) {
        this.context = context;
    }

    public GameScoreboards getScoreboards() {
        return scoreboards;
    }


    /**
     * Updates scoreboard when a game is finished
     * @param controller the controller that is controlling that game.
     */
    public void updateAndSaveScoreboardIfGameOver(PongGameController controller) {

        if (controller.isOver()) {
            // Getting the info needed to display on scoreboard
            String username = controller.getGameInfo().getPongGameInfo().getUserName();
            int score = controller.getGameInfo().getPongGameInfo().getScore();
            String game = controller.getGameInfo().getPongGameInfo().getGame();
            // assume we have loaded scoreboards and have the correct scoreboard
            loadScoreboards();
            if (scoreboards == null) {
                scoreboards = new PongGameScoreBoards();
            }
            ScoreBoard scoreboard = scoreboards.getScoreboard(game);

            // Adding the score to the scoreboard
            if (scoreboard.getScoreMap().containsKey(username)) {
                // if user already has a score
                scoreboard.addScore(username, score);
            } else { // if user doesn't have a score
                scoreboard.addUserAndScore(username, score);
            }
            // save scoreboard
            scoreboards.addScoreboard(game, scoreboard);
            saveScoreboards(scoreboards);
        }
    }

    /**
     * Saves the scoreboard to the save file.
     * @param scoreboards the scoreboards we are trying to save
     */
    private void saveScoreboards(GameScoreboards scoreboards) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("SAVED_PONG_SCOREBOARDS", Context.MODE_PRIVATE));
            outputStream.writeObject(scoreboards);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Loads the scoreboard from the save file.
     */
    public void loadScoreboards() {
        try {
            InputStream inputStream = context.openFileInput("SAVED_PONG_SCOREBOARDS");
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                scoreboards = (PongGameScoreBoards) input.readObject();
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