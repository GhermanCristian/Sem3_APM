package model.ADT;

public class MyLockTable<TKey, TValue> extends MyDictionary<TKey, TValue> {
	private int firstAvailablePosition = 1;
	
	public MyLockTable() {
		super();
	}
	
	private synchronized int setNextAvailablePosition() {
		return this.firstAvailablePosition + 1;
	}
	
	public synchronized int getFirstAvailablePosition() {
		int positionCopy = this.firstAvailablePosition;
		this.firstAvailablePosition = this.setNextAvailablePosition();
		return positionCopy;
	}
	
	@Override
	public void clear() {
		this.dictionary.clear();
		// TO-DO: go through all semaphores currently in the table and release them
		this.firstAvailablePosition = 1;
	}
}
