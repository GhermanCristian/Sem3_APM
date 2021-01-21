package exception;

public class LockNotAcquiredException extends Exception {
	public LockNotAcquiredException() {
		super("The lock has not been acquired by this thread");
	}
	
	public LockNotAcquiredException(String message) {
		super(message);
	}
}
