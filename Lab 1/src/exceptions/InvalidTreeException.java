package exceptions;

public class InvalidTreeException extends Exception{
	public InvalidTreeException() {
		super ("Invalid tree type");
	}
	
	public InvalidTreeException(String message) {
		super (message);
	}
}
