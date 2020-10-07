package repository;

import model.Tree;

public interface Repository {
	public void addElement(Tree newElement);
	public void removeElement(Tree element) throws Exception;
	public DynamicArray getAllElements();
}
