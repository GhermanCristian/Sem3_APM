package repository;

import model.Product;

public interface Repository {
	public void addElement(Product newElement);
	public DynamicArray getAllElements();
}
