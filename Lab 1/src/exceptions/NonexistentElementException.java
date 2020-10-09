package exceptions;

public class NonexistentElementException extends Exception{
	public NonexistentElementException() {
		super ("Element doesn't exist");
	}
	
	public NonexistentElementException(String message) {
		super (message);
	}
}
