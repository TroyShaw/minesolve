package minesweeper.Gui;

/**
 * Signals that this object is capable of starting a new Minesweeper game.
 * @author Troy Shaw
 *
 */
public interface Initiable {

	/**
	 * Signals to this object to initiate a new game.
	 */
	public void startNewGame();
}
