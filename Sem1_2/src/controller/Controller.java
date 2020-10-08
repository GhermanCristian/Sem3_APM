package controller;

import model.Apple;
import model.Book;
import model.Cake;
import model.Product;
import repository.DynamicArray;
import repository.MemoryRepository;
import repository.Repository;

public class Controller {
	private Repository repo;
	
	public Controller() {
		this.repo = new MemoryRepository();
	}
	
	public void addElement(String type, int weight) throws Exception {
		Product newProduct;
		
		if (type.equals("apple")) {
			newProduct = new Apple(weight);
		}
		else if(type.equals("book")) {
			newProduct = new Book(weight);
		}
		else if(type.equals("cake")) {
			newProduct = new Cake(weight);
		}
		else {
			throw new Exception("Invalid product type");
		}
		
		this.repo.addElement(newProduct);
	}
	
	public DynamicArray getAllHeavierThan() throws Exception{
		DynamicArray allElements = this.repo.getAllElements();
		DynamicArray filteredList = new DynamicArray();
		
		for(int pos = 0; pos < allElements.getSize(); pos++) {
			if (allElements.getElementAtPosition(pos).getWeight() > Product.MINIMUM_WEIGHT) {
				filteredList.insert(allElements.getElementAtPosition(pos));
			}
		}
		
		return filteredList;
	}
}
