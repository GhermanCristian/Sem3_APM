package exception;

public class StackOverflowException extends Exception {
	public StackOverflowException() {
		super("Stack overflow: too many function calls");
	}
	
	public StackOverflowException(String message) {
		super(message);
	}
}
