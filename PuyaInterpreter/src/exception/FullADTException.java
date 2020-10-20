package exception;

public class FullADTException extends Exception {
	public FullADTException() {
		super("Cannot insert into full container");
	}
	
	public FullADTException(String message) {
		super(message);
	}
}
