package fall2018.csc2017.slidingtiles;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.ArgumentMatchers;

import static org.junit.Assert.*;

import android.graphics.drawable.Drawable;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import generalclasses.ScoreBoard;


public class GameActivityControllerTest {

    private GameActivityController mController;
    private GameActivity mActivity;
    private SlidingTilesFileSaverModel mSaver;
    private SlidingTilesManager mManager;
    private SlidingTileScoreboards scoreboards;
    private ScoreBoard scoreboard;
    private Tile tile;
    private Board board;


    /**
     * Make a set of tiles that are in order.
     *
     * @return a set of tiles that are in order
     */
    private List<Tile> makeTiles() {
        List<Tile> tiles = new ArrayList<>();
        // Add the tiles from 1 to the NUM_ROWS * NOM_COLS-1
        final int numTiles = Board.NUM_ROWS * Board.NUM_COLS - 1;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        tiles.add(new Tile(24)); //add blank tile

        return tiles;
    }

    @Before
    public void setUpMock() {
        mActivity = mock(GameActivity.class);
        mController = new GameActivityController(mActivity);
        scoreboards = mock(SlidingTileScoreboards.class);
        scoreboard = mock(ScoreBoard.class);
        board = mock(Board.class);
        tile = mock(Tile.class);
        Board.NUM_COLS = 4;
        Board.NUM_ROWS = 4;
        mSaver = mock(SlidingTilesFileSaverModel.class);
        mManager = mock(SlidingTilesManager.class);
        SlidingTilesGameInfo gameinfo = new SlidingTilesGameInfo(makeTiles());
        when(mManager.getInfo()).thenReturn(gameinfo);
        mController.setmSaver(mSaver);
    }

    @Test
    public void testCreateTileButtonsImageFalse() {
        boolean image_game = false;
        Board board = new Board(makeTiles());
        ArrayList<Button> tilebuttons = mController.createTileButtons(image_game, board);
        assertEquals(16, tilebuttons.size());
        Board.NUM_COLS = 5;
        Board.NUM_ROWS = 5;
        Board board1 = new Board(makeTiles());
        ArrayList<Button> tilebuttons1 = mController.createTileButtons(image_game, board1);
        assertEquals(25, tilebuttons1.size());
    }

    @Test
    public void testHelperCreatingButton() {
        mController.setTileButtons(new ArrayList<Button>());
        when(tile.getBackground()).thenReturn(R.drawable.tile_25);
        when(board.getTile(isA(Integer.class), isA(Integer.class))).thenReturn(tile);
        mController.helperCreatingButton(24, 3, 3, board);

        verify(board).getTile(3, 3);
        verify(tile).getBackground();
    }

    @Test
    public void testUpdateTileButtons() {
        Board board = new Board(makeTiles());
        boolean image_game = false;
        ArrayList<Button> tilebuttons = mController.createTileButtons(image_game, board);
        mController.updateTileButtons(tilebuttons, image_game, board);
        assertEquals(16, tilebuttons.size());
    }

    @Test
    public void testHelperUpdate() {
        when(tile.getId()).thenReturn(25);
        when(tile.getBackground()).thenReturn(R.drawable.tile_25);
        when(board.getTile(isA(Integer.class), isA(Integer.class))).thenReturn(tile);
        Button b = mock(Button.class);
        doNothing().when(b).setBackgroundResource(R.drawable.tile_25);

        mController.helperUpdate(b, 2, board);
        verify(b).setBackgroundResource(R.drawable.tile_25);
        verify(b, times(0)).setBackground(isA(Drawable.class));
    }

    @Test
    public void testUndoBtnWithPreviousMoves() {
        when(mManager.returnPreviousMove()).thenReturn(15);
        doNothing().when(mManager).makeMove(isA(Integer.class));

        mController.undoBtn(1, mManager);
        verify(mManager).returnPreviousMove();
        verify(mManager).makeMove(15);
    }

    @Test
    public void testUndoBtnWithNoPreviousMoves() {
        mController.undoBtn(0, mManager);

        verify(mManager, times(0)).returnPreviousMove();
        verify(mManager, times(0)).makeMove(isA(Integer.class));
    }
    @Test
    public void testUpdateAndSaveScoreboardIfGameOver() {
        mManager.getInfo().setUserName("0601");
        mManager.getInfo().setComplexity("3 x 3");
        when(mManager.isOver()).thenReturn(true);
        doNothing().when(mSaver).loadScoreboards(isA(String.class));
        LinkedHashMap<String, ArrayList<Integer>> scores = new LinkedHashMap<>();
        scores.put("0601", new ArrayList<Integer>());
        when(scoreboard.getScoreMap()).thenReturn(scores);
        doNothing().when(scoreboard).addScore(isA(String.class), isA(Integer.class));
        doNothing().when(scoreboard).addUserAndScore(isA(String.class), isA(Integer.class));
        when(scoreboards.getScoreboard("3 x 3")).thenReturn(scoreboard);
        doNothing().when(scoreboards).addScoreboard(isA(String.class), isA(ScoreBoard.class));
        mSaver.scoreboards = scoreboards;
        doNothing().when(mSaver).saveScoreboards(isA(SlidingTileScoreboards.class), isA(String.class));
        mController.updateAndSaveScoreboardIfGameOver(mManager);

        verify(mManager).isOver();
        verify(mSaver).loadScoreboards("SAVED_SCOREBOARDS");
        verify(scoreboards).getScoreboard("3 x 3");
        verify(scoreboard).addScore("0601", 0);
        verify(scoreboard, times(0)).addUserAndScore("0601", 0);
        verify(scoreboards).addScoreboard("3 x 3", scoreboard);
        verify(mSaver).saveScoreboards(scoreboards, "SAVED_SCOREBOARDS");
    }

    @Test
    public void testInstantiateGameAndBegin() {
        doNothing().when(mSaver).loadScoreboards(isA(String.class));

        when(scoreboards.getScoreboard("3 x 3")).thenReturn(null);
        doNothing().when(scoreboards).addScoreboard(isA(String.class), isA(ScoreBoard.class));
        mSaver.scoreboards = scoreboards;
        doNothing().when(mSaver).saveScoreboards(isA(SlidingTileScoreboards.class), isA(String.class));

        mController.instantiateGameandBegin("3 x 3");
        verify(mSaver).loadScoreboards("SAVED_SCOREBOARDS");
        verify(scoreboards).getScoreboard("3 x 3");
        verify(scoreboards).addScoreboard(isA(String.class), isA(ScoreBoard.class));
        verify(mSaver).saveScoreboards(scoreboards, "SAVED_SCOREBOARDS");
    }

    @Test
    public void testInstantiateGameAndBeginScoreBoardsNull() {
        doNothing().when(mSaver).loadScoreboards(isA(String.class));
        mController.instantiateGameandBegin("3 x 3"); //mSaver.scoreboard == null
        assertTrue(mSaver.scoreboards instanceof SlidingTileScoreboards);
    }
}