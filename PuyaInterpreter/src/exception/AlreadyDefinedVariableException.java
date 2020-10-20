package exception;

public class AlreadyDefinedVariableException extends Exception{
	public AlreadyDefinedVariableException() {
		super("Variable is already defined");
	}
	
	public AlreadyDefinedVariableException(String message) {
		super(message);
	}
}
