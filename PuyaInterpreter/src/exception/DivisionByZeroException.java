package exception;

public class DivisionByZeroException extends Exception{
	public DivisionByZeroException() {
		super("Cannot divide by zero");
	}
	
	public DivisionByZeroException(String message) {
		super(message);
	}
}
