package exception;

public class LockAlreadyAcquiredException extends Exception {
	public LockAlreadyAcquiredException() {
		super("The lock is already acquired by this thread");
	}
	
	public LockAlreadyAcquiredException(String message) {
		super(message);
	}
}
