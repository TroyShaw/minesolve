package minesweeper.images;

public class NotImageException extends Exception {

	public NotImageException() {
		super();
	}
	
	public NotImageException(String reason) {
		super(reason);
	}
}
