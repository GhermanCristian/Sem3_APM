package repository;

import model.Product;

public class MemoryRepository implements Repository {
	private DynamicArray elements;
	
	public MemoryRepository() {
		this.elements = new DynamicArray();
	}
	
	public void addElement(Product newElement) {
		this.elements.insert(newElement);
	}
	
	public DynamicArray getAllElements() {
		return this.elements;
	}
}
