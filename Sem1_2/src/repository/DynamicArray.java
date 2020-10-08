package repository;

import model.Product;

public class DynamicArray {
	final int INITIAL_ARRAY_SIZE = 10;
	final int RESIZE_MULTIPLYING_FACTOR = 2;
	int currentSize;
	int currentCapacity;
	Product elements[];
	
	public DynamicArray() {
		this.elements = new Product[INITIAL_ARRAY_SIZE];
		this.currentCapacity = INITIAL_ARRAY_SIZE;
		this.currentSize = 0;
	}
	
	private void resizeArray() {
		Product tempArray[] = new Product[this.currentCapacity * RESIZE_MULTIPLYING_FACTOR];
		for(int pos = 0; pos < this.currentSize; pos++) {
			tempArray[pos] = this.elements[pos];
		}
		this.elements = tempArray;
		this.currentCapacity *= RESIZE_MULTIPLYING_FACTOR;
	}
	
	public void insert(Product newElement) {
		if (this.currentSize + 1 >= this.currentCapacity) {
			this.resizeArray();
		}
		
		this.elements[this.currentSize++] = newElement;
	}
	
	public int getSize() {
		return this.currentSize;
	}
	
	public Product getElementAtPosition(int pos) throws Exception {
		if (pos >= this.currentSize || pos < 0) {
			throw new Exception("Invalid position");
		}
		
		return this.elements[pos];
	}
}
