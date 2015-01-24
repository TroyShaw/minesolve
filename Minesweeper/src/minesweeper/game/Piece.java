package minesweeper.game;

public enum Piece {
	zero,
	one,
	two,
	three,
	four,
	five,
	six,
	seven,
	eight,

	blank,

	mine,
	redMine,

	flag,
	incorrectFlag,

	question;

	/**
	 * Returns a non-mine square with the given number of surrounding pieces.
	 * Returns null if out of range of 0-8.
	 *
	 * @param numMines the number of mines
	 * @return the appropriate enum value, or null
	 */
	public static Piece getPiece(int numMines) {
		switch (numMines) {
		case 0: return zero;
		case 1: return one;
		case 2: return two;
		case 3: return three;
		case 4: return four;
		case 5: return five;
		case 6: return six;
		case 7: return seven;
		case 8: return eight;
		default: return null;
		}
	}
}