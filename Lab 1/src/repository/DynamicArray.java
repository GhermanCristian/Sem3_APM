package repository;

import model.TreeInterface;

public class DynamicArray {
	final int INITIAL_ARRAY_SIZE = 10;
	final int RESIZE_MULTIPLYING_FACTOR = 2;
	int currentSize;
	int currentCapacity;
	TreeInterface elements[];
	
	public DynamicArray() {
		this.elements = new TreeInterface[INITIAL_ARRAY_SIZE];
		this.currentCapacity = INITIAL_ARRAY_SIZE;
		this.currentSize = 0;
	}
	
	private void resizeArray() {
		TreeInterface tempArray[] = new TreeInterface[this.currentCapacity * RESIZE_MULTIPLYING_FACTOR];
		for(int pos = 0; pos < this.currentSize; pos++) {
			tempArray[pos] = this.elements[pos];
		}
		this.elements = tempArray;
		this.currentCapacity *= RESIZE_MULTIPLYING_FACTOR;
	}
	
	public void insert(TreeInterface newElement) {
		if (this.currentSize + 1 >= this.currentCapacity) {
			this.resizeArray();
		}
		
		this.elements[this.currentSize++] = newElement;
	}
}
