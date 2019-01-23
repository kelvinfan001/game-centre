package fall2018.csc2017.pong;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.*;


/**
 * Manages the tasks in PongScoreBoardActivity.
 * Helps displaying the scores on scoreboard.
 */
public class PongScoreBoardManager {
    private PongScoreBoardActivity scoreActivity;
    private LinkedHashMap<String, ArrayList<Integer>> scores;
    private LinkedHashMap<String, Integer> highScores;

    /**
     * Constructor of the manager of PongScoreBoard
     * @param activity Activity of ScoreBoard
     * @param scores historical scores of the user
     * @param high_scores high scores from all the players
     */
    public PongScoreBoardManager(PongScoreBoardActivity activity, LinkedHashMap<String, ArrayList<Integer>> scores,
                          LinkedHashMap<String, Integer> high_scores) {
        scoreActivity = activity;
        this.scores = scores;
        this.highScores = high_scores;

    }


    /**
     * Making the scoreboard display the global rankings.
     */
    void displayGlobalRankings() {
        updateHighScores();
        int i = 0;
        for (String user : highScores.keySet()) {
            setTextValues(i, user, String.valueOf(highScores.get(user)));
            i += 1;
        }
        for (; i < 9; i++) {
            setTextValues(i, "N/A", "N/A");
        }
    }

    /**
     * Sets the TextViews for user + i and score + i in content_sliding_tiles_score_board.xml
     *
     * @param i         the num id of the user/score in the XML file. i.e user0 or score0
     * @param userText  the text you want to replace user + i with.
     * @param scoreText the text you want to replace score + i with.
     */
    public void setTextValues(int i, String userText, String scoreText) {
        TextView currentUserText = scoreActivity.findViewById(scoreActivity.getResources().getIdentifier("User" + i,
                "id",
                scoreActivity.getPackageName()));
        System.out.println("The package name is " + scoreActivity.getPackageName());
        currentUserText.setText(userText);

        TextView currentScoreText = scoreActivity.findViewById(scoreActivity.getResources().getIdentifier("Score" + i,
                "id",
                scoreActivity.getPackageName()));
        currentScoreText.setText(scoreText);
    }

    /**
     * Updates 'highScores' LinkedHashMap with the current scores.
     */
    private void updateHighScores() {
        ArrayList<Object[]> highScoreArray = getHighScoreArray();
        for (Object[] set : highScoreArray) {
            highScores.put((String) set[0], (Integer) set[1]); // set[0] = user, set[1] = score
        }
    }

    /**
     * Helper function for updateHighScores. Allows for easier mapping to LinkedHashMap with ArrayList
     * of HighScores.
     *
     * @return a sorted ArrayList<[User, Score]> of the highest scoring players and their scores.
     */
    private ArrayList<Object[]> getHighScoreArray() {

        ArrayList<Object[]> highScoresArray = new ArrayList<>(); // max size: 9, contains [user, score]
        for (String user : scores.keySet()) {
            ArrayList<Integer> value = scores.get(user);
            Object[] set = {user, Collections.max(value)};
            sortHighScoreArray(highScoresArray);
            Collections.reverse(highScoresArray);
            // if its not full
            if (highScoresArray.size() != 9) {
                highScoresArray.add(set);
            }
            // if the lowest score in the scoreboard is greater than current score
            else if ((int) highScoresArray.get(8)[1] < (int) set[1]) {
                highScoresArray.remove(8);
                highScoresArray.add(set);

            }
        }
        sortHighScoreArray(highScoresArray);
        Collections.reverse(highScoresArray);
        return highScoresArray;
    }

    /**
     * Helper function used for getHighScoreArray.
     * It sorts highScoresArray based on the score.
     */
    private void sortHighScoreArray(ArrayList<Object[]> highScoreArray) {
        Collections.sort(highScoreArray, new Comparator<Object[]>() {
            public int compare(Object[] temp1, Object[] temp2) {
                return Integer.compare((int) temp1[1], (int) temp2[1]);
            }
        });
    }
    public void sortHelper(List<Integer> list) {
        Collections.sort(list, Collections.reverseOrder());
    }
    /**
     * Making the scoreboard display local rankings of the current user logged in.
     *
     * @param username the username of the current user logged in.
     */
    public void displayLocalRankings(String username) {
        ArrayList localScores = scores.get(username);
        sortHelper(localScores);
//        Collections.sort(localScores, Collections.reverseOrder());
        if (localScores.size() >= 9) {
            // if >= 9 then you only want the 9 scores.
            localScores = new ArrayList<Object>(localScores.subList(0, 9));
        }
        int i = 0;

        // Setting the text values for all the user's scores
        for (Object score : localScores) {
            setTextValues(i, username, String.valueOf(score));
            i += 1;
        }

        // Setting the text values as N/A and N/A for the rest of the scores.
        for (; i < 9; i++) {
            setTextValues(i, "N/A", "N/A");
        }
    }

    /**
     * Making the scoreboard display blank rankings where user is N/A and score is N/A
     */
    void displayBlankRankings() {
        for (int i = 0; i < 9; i++) {
            setTextValues(i, "N/A", "N/A");
        }
    }

}
