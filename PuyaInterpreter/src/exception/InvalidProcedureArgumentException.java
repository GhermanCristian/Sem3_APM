package exception;

public class InvalidProcedureArgumentException extends Exception {
	public InvalidProcedureArgumentException() {
		super("The procedure arguments are invalid");
	}
	
	public InvalidProcedureArgumentException(String message) {
		super(message);
	}
}
