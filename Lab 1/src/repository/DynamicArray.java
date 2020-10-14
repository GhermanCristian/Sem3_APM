package repository;

import exceptions.InvalidPositionException;
import exceptions.NonexistentElementException;
import model.Tree;

public class DynamicArray {
	final int INITIAL_ARRAY_SIZE = 10;
	final int RESIZE_MULTIPLYING_FACTOR = 2;
	int currentSize;
	int currentCapacity;
	Tree elements[];
	
	public DynamicArray() {
		this.elements = new Tree[INITIAL_ARRAY_SIZE];
		this.currentCapacity = INITIAL_ARRAY_SIZE;
		this.currentSize = 0;
	}
	
	private void resizeArray() {
		Tree tempArray[] = new Tree[this.currentCapacity * RESIZE_MULTIPLYING_FACTOR];
		for(int pos = 0; pos < this.currentSize; pos++) {
			tempArray[pos] = this.elements[pos];
		}
		this.elements = tempArray;
		this.currentCapacity *= RESIZE_MULTIPLYING_FACTOR;
	}
	
	public void insert(Tree newElement) {
		if (this.currentSize + 1 >= this.currentCapacity) {
			this.resizeArray();
		}
		
		this.elements[this.currentSize++] = newElement;
	}
	
	// removes first occurence of an element
	public void remove(Tree element) throws NonexistentElementException {
		for(int pos = 0; pos < this.currentSize; pos++) {
			if (this.elements[pos].getAge() == element.getAge() && this.elements[pos].getType().equals(element.getType())) {
				// shift all the elements by one position to the left
				for (int newPos = pos + 1; newPos < this.currentSize; newPos++) {
					this.elements[newPos - 1] = this.elements[newPos];
				}
				this.currentSize--;
				return;
			}
		}
		// element was not found => it doesn't exist
		throw new NonexistentElementException();
	}
	
	public int getSize() {
		return this.currentSize;
	}
	
	public Tree getElementAtPosition(int pos) throws InvalidPositionException {
		if (pos >= this.currentSize || pos < 0) {
			throw new InvalidPositionException();
		}
		
		return this.elements[pos];
	}
}
