package nz.co.troyshaw.minesweeper.game;

import nz.co.troyshaw.minesweeper.controller.MinesweeperTimer;
import nz.co.troyshaw.minesweeper.solver.Solver;

/**
 * Represents a single game of Minesweeper. 
 * The game begins in a non-populated state until a first click is made.
 * The first square clicked is guaranteed to be non-mine.
 * The game then continues until either all non-mines are revealed, or a single mine is revealed.
 *
 * @author Troy Shaw
 */
public class Minesweeper {

	/**
	 * If the game is currently in question-mark mode. 
	 * Setting this true will cause squares to be toggled to question-mark after right clicking a flagged square.
	 */
	public static boolean questionMode = false;

	/**
	 * Object that maintains the data of the board.
	 */
	private Board board;

	//maintain hasWon and hasLost since returning !hasWon for hasLost could be misleading if used inappropriately.
	private boolean gameFinished, hasWon, hasLost;

	/**
	 * The object that is notified as squares are revealed, when turns are finished, 
	 * and the post-status of a move (win/ lose/ continue).
	 */
	private MinesweeperListener listener;

	private MinesweeperTimer timer;
	
	private int minesUnflagged, unclickedNonmines;

	/**
	 * Creates a new minesweeper game with the given parameters. <p>
	 * Width and height must be greater than 0.<br>
	 * Mines must be greater than 0 and less then width * height.
	 * 
	 * @param width the width of the board
	 * @param height the height of the board
	 * @param numMines number of mines
	 * @param listener the listener object notified during gameplay
	 * @throws IllegalArgumentException if mines < 0 or mines > width * height
	 */
	public Minesweeper(int width, int height, int numMines, MinesweeperListener listener){
		if (width < 0 || height < 0) throw new IllegalArgumentException("Board dimension must be greater than 0");
		else if (numMines < 0 || numMines > width * height) throw new IllegalArgumentException("Invalid mines number");

		minesUnflagged = numMines;
		unclickedNonmines = width * height - numMines;
		
		board = new Board(width, height, numMines);
		
		this.listener = listener;

		timer = new MinesweeperTimer(listener);
	}

	/**
	 * Creates and returns a solver object for this game.
	 * @return a solver
	 */
	public Solver getSolver() {
		return new Solver(board, listener, this);
	}

	/**
	 * Attempts to click the square with the intention that is not a mine.<p>
	 * Clicking an already clicked square does nothing.<br>
	 * Clicking a mine ends the game.<br>
	 * Clicking a non-clicked, non-mine square displays that square (and potentially others).
	 * 
	 * @param x x coordinate of square clicked
	 * @param y y coordinate of square clicked
	 */
	public void revealSquare(int x, int y) {
		//first check click is on board
		if (!board.positionExists(x, y)) return;

		//if the board hasn't been populated, populate it and start game timer
		if (!board.isPopulated()) startSequence(x, y);

		//now do the click
		internalReveal(x, y);

		//notify the listener the move is finished
		listener.moveFinished();
	}

	private void internalReveal(int x, int y) {
		//first check error conditions
		if (!checkValidMove(x, y)) return;

		Square square = board.getSquare(x, y);

		//if square already clicked
		if (square.isRevealed()) {
			return;
		}

		//if square is flagged, do nothing
		if (square.isFlagged()) {
			return;
		}

		//check if we are on a mine, if so end the game
		if (square.isMine()) {
			listener.mineRevealed(x, y);
			initiateLoseSequence(x, y);
			return;
		}

		//we must be on a square we haven't clicked yet
		if (square.numMines() > 0) {
			//if square has any number of mines surrounding it, we can simply display that square
			square.setClicked();
			listener.squareRevealed(x, y, square.numMines());
			unclickedNonmines--;
		} else {
			//otherwise we must recursively 'click' squares until we click one that has a mine around it
			recurseClick(x, y);
		}

		//check if we have won
		if (unclickedNonmines == 0) {
			initiateWinSequence();
		}
	}

	/**
	 * Does a "double-click" move.
	 * A square must be revealed, be a non-mine, and be surrounded by the same number of flags as it has adjacent mines.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void revealSurrounding(int x, int y) {
		if (!board.positionExists(x, y)) return;

		Square s = board.getSquare(x, y);

		if (!s.isRevealed() || s.isMine() || !board.isSatisfied(x, y)) return;

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (!board.positionExists(i, j)) continue;
				internalReveal(i, j);
			}
		}

		//notify the listener the move is finished
		listener.moveFinished();
	}

	/**
	 * Toggles the current square as a flag or not.
	 * If the square is flagged, it becomes unflagged.
	 * If the square is unrevealed and not flagged, it becomes flagged.
	 * If the square is revealed, nothing happens.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void toggleSquare(int x, int y) {

		//first check we are are allowed to make a move 
		if (!checkValidMove(x, y)) return;

		Square square = board.getSquare(x, y);

		if (square.isRevealed()) {
			//do nothing
			return;
		}
		if (square.isFlagged()) 
			minesUnflagged++;
		
		//toggle flag and notify listener
		switch(square.toggleState()) {
		case flagged:
			listener.squareFlagged(x, y);
			minesUnflagged--;
			break;
		case unmarked:
			listener.squareUnmarked(x, y);
			break;
		case questioned:
			listener.squareQuestioned(x, y);
			break;
		default:
			break;
		}
		
		listener.totalFlagsChanged(minesUnflagged);
	}

	/**
	 * Sets the current square as a flag.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void setFlagged(int x, int y) {
		//first check we are are allowed to make a move 
		if (!checkValidMove(x, y)) return;

		Square square = board.getSquare(x, y);

		if (square.isRevealed()) {
			//do nothing
			return;
		}

		//toggle flag and notify listener
		square.setFlagged();

		minesUnflagged--;
		listener.totalFlagsChanged(minesUnflagged);
		listener.squareFlagged(x, y);
	}

	/**
	 * Returns true if the position exists and the game isn't over.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return true if the move is valid, false otherwise
	 */
	private boolean checkValidMove(int x, int y) {
		return board.positionExists(x, y) && !gameFinished;
	}

	/**
	 * Helper method to recursively click squares with no adjacent mines.
	 * If a square has no adjacent mines, it is clicked and all adjacent squares are recursively clicked.
	 * If a square has any number of adjacent mines, it is clicked and then the recursion stops.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void recurseClick(int x, int y) {
		Square square = board.getSquare(x, y);

		if (square.isRevealed() || square.isFlagged() || square.isQuestioned()) return;
		//first click square
		square.setClicked();
		listener.squareRevealed(x, y, square.numMines());
		unclickedNonmines--;

		//then check if it has any adjacent mines. If it doesn't, recurse.
		if (square.numMines() == 0 && square.isRevealed()) {
			for (int i = x - 1; i <= x + 1; i++) {
				for (int j = y - 1; j <= y + 1; j++) {
					if (!board.positionExists(i, j)) continue;
					recurseClick(i, j);
				}
			}
		}
	}


	/**
	 * Causes the screen to display the mines and incorrect flagged squares.
	 * Will not redraw the given square.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void initiateLoseSequence(int x, int y) {
		timer.cancel();
		
		gameFinished = true;
		hasLost = true;

		Square[][] b = board.getBoard();

		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				if (i == x && j == y) continue;

				Square s = b[i][j];

				if (s.isFlagged() && !s.isMine()) listener.incorrectFlag(i, j);
				else if (s.isMine() && !s.isFlagged()) listener.squareBomb(i, j);
			}
		}

		listener.gameLost();
	}

	private void initiateWinSequence() {
		timer.cancel();
		
		hasWon = true;
		gameFinished = true;
		
		Square[][] b = board.getBoard();

		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[i].length; j++) {
				Square s = b[i][j];

				if (!s.isFlagged() && s.isMine()) listener.squareFlagged(i, j);
			}
		}
		
		listener.gameWon();
	}

	/**
	 * Returns the square at the given position, or null if it does not exist.
	 * @param x the x coordinate of the square
	 * @param y the y coordinate of the square
	 * @return the piece at the given coordinates, or null
	 */
	public Square getSquare(int x, int y) {
		return board.positionExists(x, y) ? board.getSquare(x, y) : null;
	}

	public Square[][] getSquares() {
		return board.getBoard();
	}
	
	/**
	 * Returns true if the game has been won (all non-mine squares have been revealed).
	 * @return true if they have won
	 */
	public boolean hasWon() {
		return hasWon;
	}

	/**
	 * Checks if the game has finished.
	 * The game is over if the player has won, or the player clicked a mine.
	 *
	 * @return true if the player has won or died, false otherwise.
	 */
	public boolean isGameFinished() {
		return hasWon() || hasDied();
	}
	
	/**
	 * Returns true if the game has been lost (a mine has been clicked).
	 *
	 * @return true if they have lost
	 */
	public boolean hasDied() {
		return hasLost;
	}

	/**
	 * Populates the board and starts the game timer.
	 */
	private void startSequence(int xSafe, int ySafe) {
		board.populateBoard(xSafe, ySafe);

		//calls event every 1 second, with a 1 second delay 
		timer.startTimer();
	}
	
	public void stopTimer() {
		timer.cancel();
	}
}