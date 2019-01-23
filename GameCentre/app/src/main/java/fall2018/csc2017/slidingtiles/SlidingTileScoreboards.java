package fall2018.csc2017.slidingtiles;

import java.io.Serializable;

import generalclasses.GameScoreboards;

/**
 * A sliding tile scoreboard that saves user's score to their corresponding game difficulty.
 */
public class SlidingTileScoreboards extends GameScoreboards implements Serializable {

    /**
     * The name of the game.
     */
    public String game;

    SlidingTileScoreboards() {
        this.game = "Sliding Tiles";
    }
}
