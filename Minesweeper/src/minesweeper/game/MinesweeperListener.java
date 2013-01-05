package minesweeper.game;

public interface MinesweeperListener {

	public void alreadyClickedEvent();

	/**
	 * Signals that a square was revealed, and has x number of adjacent mines.
	 * 
	 * @param x
	 * @param y
	 * @param numMines
	 */
	public void squareRevealed(int x, int y, int numMines);
	
	/**
	 * Signals that the given square was flagged as a mine.
	 * 
	 * @param x
	 * @param y
	 */
	public void squareFlagged(int x, int y);
	
	/**
	 * Signals that a mine was revealed at the given location.
	 * The game is also over when this happens.
	 * 
	 * @param x
	 * @param y
	 */
	public void mineRevealed(int x, int y);
	
	/**
	 * Signals that a square was set as a question-mark.
	 * @param x
	 * @param y
	 */
	public void squareQuestioned(int x, int y);
	
	/**
	 * Signals that a square was unmarked (went from flag or ? to unmarked).
	 * @param x
	 * @param y
	 */
	public void squareUnmarked(int x, int y);
	
	/**
	 * Signals that at the end of a game the square was a bomb.
	 * @param x
	 * @param y
	 */
	public void squareBomb(int x, int y);
	
	/**
	 * Signals that at the end of a game the square was incorrectly flagged.
	 * @param x
	 * @param y
	 */
	public void incorrectFlag(int x, int y);
	
	/**
	 * Signals that the game has been won.
	 */
	public void gameWon();
	
	/**
	 * Signals that the game was lost.
	 */
	public void gameLost();
	
	/**
	 * Called every second of gameplay. To get a more accurate time, call timeSinceStart() of Minesweeper.
	 */
	public void tick();
	
	/**
	 * Called after a click has been fully resolved (e.g when recursing).
	 */
	public void moveFinished();
	
	public void totalFlagsChanged(int numFlags);
}
