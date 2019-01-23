package fall2018.csc2017.slidingtiles;

import android.support.annotation.NonNull;

import java.util.Observable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The sliding tiles board.
 */
public class Board extends Observable implements Serializable, Iterable<Tile> {

    /**
     * The number of rows.
     */
    static int NUM_ROWS;

    /**
     * The number of rows.
     */
    static int NUM_COLS;

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles = new Tile[NUM_ROWS][NUM_COLS];

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    Board(List<Tile> tiles) {
        Iterator<Tile> iter = tiles.iterator();

        for (int row = 0; row != Board.NUM_ROWS; row++) {
            for (int col = 0; col != Board.NUM_COLS; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        Tile one = tiles[row1][col1];
        Tile two = tiles[row2][col2];
        tiles[row1][col1] = two;
        tiles[row2][col2] = one;
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return "Board{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    @Override
    @NonNull
    public Iterator<Tile> iterator() {
        return new BoardIterator();
    }

    /**
     * The iterator for Board Class.
     */
    private class BoardIterator implements Iterator<Tile> {

        /**
         * The current index of the row for tiles.
         */
        private int row = 0;
        /**
         * The current index of the column for tiles.
         */
        private int column = 0;

        @Override
        public boolean hasNext() {
            // will return false when row is 4 because tiles[4][0] will be an index error.
            return (row != Board.NUM_ROWS);
        }

        @Override
        public Tile next() {
            Tile result = tiles[row][column]; // remembering the value.
            // updating the value.
            if (column != Board.NUM_COLS - 1) {
                column++;
            } else {
                row++;
                column = 0;
            }
            return result; // returns the remembered value.
        }
    }
}
