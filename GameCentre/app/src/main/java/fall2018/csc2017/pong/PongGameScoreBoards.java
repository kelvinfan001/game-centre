package fall2018.csc2017.pong;

import java.io.Serializable;
import java.util.HashMap;

import generalclasses.GameScoreboards;
import generalclasses.ScoreBoard;

/**
 * Holds the entire ScoreBoard of the Pong game itself.
 * Including each user's scoreboard, and global ranking.
 * Excluded from Unit test since this is a Model.
 */
public class PongGameScoreBoards extends GameScoreboards implements Serializable {

    /**
     * Keeps the Scoreboards of all pong game.
     */
    private HashMap<String, ScoreBoard> scoreboardsForGame = new HashMap<>();

    /**
     * Name of the game
     */
    public String game;

    /**
     * Constructor of the PongScoreBoards
     */
    PongGameScoreBoards() {
        this.game = "Pong";
    }

    /**
     * Returns all the PongScoreBoard when it's called
     *
     * @param gameName name of the game
     * @return PongScoreBoard
     */
    public ScoreBoard getScoreboard(String gameName) {
        if (!scoreboardsForGame.keySet().contains(gameName)) {
            scoreboardsForGame.put(gameName, new ScoreBoard());
        }
        return scoreboardsForGame.get(gameName);
    }
}
