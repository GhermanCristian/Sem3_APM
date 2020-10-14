package repository;

import exceptions.NonexistentElementException;
import model.Tree;

public class MemoryRepository implements Repository {
	private DynamicArray elements;
	
	public MemoryRepository() {
		this.elements = new DynamicArray();
	}
	
	@Override
	public void addElement(Tree newElement) {
		this.elements.insert(newElement);
	}
	
	@Override
	public void removeElement(Tree element) throws NonexistentElementException {
		this.elements.remove(element);
	}
	
	@Override
	public DynamicArray getAllElements() {
		return this.elements;
	}
}
