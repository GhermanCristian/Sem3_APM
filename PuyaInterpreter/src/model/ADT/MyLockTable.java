package model.ADT;

public class MyLockTable<TKey, TValue> extends MyDictionary<TKey, TValue> {
	private int firstAvailablePosition = 1;
	
	public MyLockTable() {
		super();
	}
	
	// I'm not sure this had to be synchronized as well, bc it's only called inside the other, already synchronized, method
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
		this.firstAvailablePosition = 1;
	}
}
