package model.ADT;

public class MyHeap<TKey, TValue> extends MyDictionary<TKey, TValue>{
	private int firstAvailablePosition = 1;
	
	public MyHeap() {
		super();
	}
	
	private int setNextAvailablePosition() {
		// normally I would search for available positions that have previously been occupied and are now free due to the GC
		// but for now I'll just add them one after the other
		return this.firstAvailablePosition + 1;
	}
	
	public int getFirstAvailablePosition() {
		int positionCopy = this.firstAvailablePosition;
		this.firstAvailablePosition = setNextAvailablePosition();
		return positionCopy;
	}
	
	@Override
	public void clear() {
		this.dictionary.clear();
		this.firstAvailablePosition = 1;
	}
}
