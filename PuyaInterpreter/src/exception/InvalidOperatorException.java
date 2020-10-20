package exception;

public class InvalidOperatorException extends Exception{
	public InvalidOperatorException() {
		super("Invalid operator");
	}
	
	public InvalidOperatorException(String message) {
		super(message);
	}
}
