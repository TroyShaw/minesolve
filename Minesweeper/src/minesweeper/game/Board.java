package minesweeper.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {

	private int width, height, numMines;

	private Square[][] board;

	private boolean populated;

	/**
	 * Creates a new unpopulated minesweeper board. 
	 * 
	 * @param width the width of the board
	 * @param height the height of the board
	 * @param numMines the number of mines
	 */
	public Board(int width, int height, int numMines) {
		this.width = width;
		this.height = height;
		this.numMines = numMines;

		board = new Square[width][height];

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				board[i][j] = new Square(i, j);
			}
		}
	}

	/**
	 * Populates the board with the given square guaranteed to be a non-mine.
	 * @param xSafe the x coordinate that should not contain a mine
	 * @param ySafe the y coordinate that should not contain a mine
	 */
	public void populateBoard(int xSafe, int ySafe) {
		if (populated) return;

		Point safeSquare = new Point(xSafe, ySafe);

		List<Point> points = new ArrayList<Point>();
		boolean[][] squares = new boolean[width][height];

		//all the points that might be mines
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				points.add(new Point(i, j));
			}
		}

		//remove our safe square then shuffle so we are random
		points.remove(safeSquare);
		Collections.shuffle(points);

		//get the first 0-numMines points (that are random) and set them as our mine
		for (int i = 0; i < numMines; i++) {
			Point p = points.get(i);

			squares[p.x][p.y] = true;
		}

		//now iterate over the array of mine/ non mine and set our board appropriately
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (squares[i][j]) {
					//the square was a mine
					board[i][j].setMine();
				} else {
					//the square wasn't a mine, so count adjacent squares and set as non-mine
					int numMines = 0;

					for (int x = i - 1; x <= i + 1; x++) {
						for (int y = j - 1; y <= j + 1; y++) {
							if (!positionExists(x, y)) continue;

							if (squares[x][y]) numMines++;
						}
					}
					board[i][j].setNonMine(numMines);
				}
			}
		}

		populated = true;
	}

	/**
	 * Returns if this board has been populated.
	 *
	 * @return true if populated, false otherwise
	 */
	public boolean isPopulated() {
		return populated;
	}

	public Square getSquare(int x, int y) {
		return board[x][y];
	}

	public Square[][] getBoard() {
		return board;
	}

	/**
	 * Returns true if the square is a valid position on the board, that is within the bounds of the board.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return true if the coordinate is in the bounds of the board, false otherwise
	 */
	public boolean positionExists(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	public boolean isSatisfied(int x, int y) {
		//first check the square is actually non-mine
		if (board[x][y].isMine()) return false;

		//the square must also have at least 1 adjacent mine
		if (board[x][y].numMines() == 0) return false;

		int surrounding = 0;

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				//position must exist, and we don't count centre square
				if (!positionExists(i, j) || (i == x && j == y)) continue;
				if (board[i][j].isFlagged()) surrounding++;
			}
		}

		return surrounding == board[x][y].numMines();
	}

	public boolean isSurroundClicked(int x, int y) {
		//first check the square is actually non-mine
		if (board[x][y].isMine()) return false;

		//the square must also have at least 1 adjacent mine
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				//position must exist, and we don't count centre square
				if (!positionExists(i, j) || (i == x && j == y)) continue;
				if (!board[i][j].isRevealed()) return false;
			}
		}

		return true;
	}
	
	public List<Square> getClickableSurround(int i, int j) {
		List<Square> squares = new ArrayList<Square>();
		
		for (int x = i - 1; x <= i + 1; x++) {
			for (int y = j - 1; y <= j + 1; y++) {
				if (positionExists(x, y) || (x == i && y == j)) continue;

				Square s = board[x][y];

				if (!s.isFlagged() && !s.isRevealed()) squares.add(s);
			}
		}
		return squares;
	}
	
	/**
	 * Returns the number of unsatisfied mines left for this square.
	 * The number of unsatisfied mines is numMines - adjacentFlags.
	 *
	 * @param i
	 * @return
	 */
	public int minesNeeded(int i, int j) {
		int flags = 0;
		
		for (int x = i - 1; x <= i + 1; x++) {
			for (int y = j - 1; y <= j + 1; y++) {
				if (positionExists(x, y) || (x == i && y == j)) continue;

				if (board[x][y].isFlagged()) flags++;
			}
		}
		
		return board[i][j].numMines() - flags;
	}
	
	public boolean hasClickableSurround(int x, int y) {
		//first check the square is actually non-mine
		if (board[x][y].isMine()) return false;

		//the square must also have at least 1 adjacent mine
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				//position must exist, and we don't count centre square
				if (!positionExists(i, j) || (i == x && j == y)) continue;
				if (!board[i][j].isRevealed() && !board[i][j].isFlagged()) return true;
			}
		}

		return false;
	}
}
