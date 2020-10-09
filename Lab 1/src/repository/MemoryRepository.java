package repository;

import exceptions.NonexistentElementException;
import model.Tree;

public class MemoryRepository implements Repository {
	private DynamicArray elements;
	
	public MemoryRepository() {
		this.elements = new DynamicArray();
	}
	
	public void addElement(Tree newElement) {
		this.elements.insert(newElement);
	}
	
	public void removeElement(Tree element) throws NonexistentElementException {
		this.elements.remove(element);
	}
	
	public DynamicArray getAllElements() {
		return this.elements;
	}
}
