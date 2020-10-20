package exception;

public class UndefinedVariableException extends Exception{
	public UndefinedVariableException() {
		super("Variable is not defined");
	}
	
	public UndefinedVariableException(String message) {
		super(message);
	}
}
