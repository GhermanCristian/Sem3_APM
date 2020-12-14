package exception;

public class InvalidPositionException extends Exception {
	public InvalidPositionException() {
		super ("Invalid position");
	}
	
	public InvalidPositionException(String message) {
		super (message);
	}
}
