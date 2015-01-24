package minesweeper.game;

import java.awt.Point;

/**
 * Class represents a minesweeper square on the board
 * @author Troy Shaw
 *
 */
public class Square {

	//the x, y coordinate of this square on the board
	private int x, y;
	private Point position;
	
	//the number of mines that surround this square on the board
	private int numMines;
	
	//true if this square has been clicked during the current game
	private boolean revealed;
	
	//null if not question or flagged
	private State state = State.unmarked;
	
	//true if this square is a mine
	private boolean isMine;
	
	/**
	 * Creates a default square.
	 * @param x
	 * @param y
	 */
	public Square(int x, int y) {
		this.x = x;
		this.y = y;
		
		position = new Point(x, y);
	}
	
	/**
	 * Sets this square to be a mine.
	 */
	public void setMine() {
		isMine = true;
	}
	
	/**
	 * Sets this square to be a non-mine with adjacent number of adjacent mines.
	 * @param adjacent
	 */
	public void setNonMine(int adjacent) {
		numMines = adjacent;
	}
	
	public void setClicked() {
		revealed = true;
	}
	
	/**
	 * Toggles flag/ questioned/ none state, then returns the state it turned to.
	 * If the square has previously been revealed this method returns null.
	 * 
	 * If none, turns to flag. If flag turns to questioned. If questioned turns to none.
	 * Will also only turn question mark if question mark is enabled.
	 */
	public State toggleState() {
		if (revealed) return null;
		
		switch(state) {
		case flagged:
			state = Minesweeper.questionMode ? state = State.questioned : State.unmarked;
			break;
		case unmarked:
			state = State.flagged;
			break;
		case questioned:
			state = State.unmarked;
			break;
		default:
			//do nothing
			break;
		}
		
		return state;
	}
	
	public void setFlagged() {
		if (revealed) return;
		
		state = State.flagged;
	}
	
	
	/**
	 * Returns true if this square is a mine.
	 * @return
	 */
	public boolean isMine() {
		return isMine;
	}
	
	/**
	 * Returns true if this square has been revealed.
	 * @return true if this square has been revealed
	 */
	public boolean isRevealed() {
		return revealed;
	}
		
	/**
	 * Returns true if this square is flagged.
	 * @return true if this square is flagged
	 */
	public boolean isFlagged() {
		return state == State.flagged;
	}
	
	/**
	 * Returns true if this square is questioned.
	 * @return true if this square is questioned
	 */
	public boolean isQuestioned() {
		return state == State.questioned;
	}
	
	/**
	 * Returns the number of mines that surround this square.
	 * @return the number of mines around this square
	 */
	public int numMines() {
		return numMines;
	}
	
	public Piece getPiece() {
		if (revealed) {
			if (isMine) return Piece.mine;
			else return Piece.getPiece(numMines);
		} else {
			switch(state){ 
			case unmarked: return Piece.blank;
			case flagged: return Piece.flag;
			case questioned: return Piece.question;
			default: return null;
			}
		}
	}
	
	public Point getPosition() {
		return position;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public enum State {unmarked, flagged, questioned}
}
