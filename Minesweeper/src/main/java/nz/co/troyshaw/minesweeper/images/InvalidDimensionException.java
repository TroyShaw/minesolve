package nz.co.troyshaw.minesweeper.images;

public class InvalidDimensionException extends Exception {

	public InvalidDimensionException() {
		super();
	}
	
	public InvalidDimensionException(String reason) {
		super(reason);
	}
}
