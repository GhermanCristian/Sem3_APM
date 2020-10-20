package exception;

public class EmptyADTException extends Exception{
	public EmptyADTException() {
		super("Cannot remove from empty container");
	}
	
	public EmptyADTException(String message) {
		super(message);
	}
}
