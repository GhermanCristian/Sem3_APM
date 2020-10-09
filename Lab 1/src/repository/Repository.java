package repository;

import exceptions.NonexistentElementException;
import model.Tree;

public interface Repository {
	public void addElement(Tree newElement);
	public void removeElement(Tree element) throws NonexistentElementException;
	public DynamicArray getAllElements();
}
