package controller;

import repository.DynamicArray;
import repository.MemoryRepository;
import repository.Repository;
import exceptions.InvalidPositionException;
import exceptions.InvalidTreeException;
import exceptions.NonexistentElementException;
import model.AppleTree;
import model.CherryTree;
import model.PearTree;
import model.Tree;

public class Controller {
	private Repository repo;
	
	public Controller() {
		repo = new MemoryRepository();
	}
	
	public void addElement(String treeSpecies, int treeAge) throws InvalidTreeException {
		Tree newTree;
		
		if (treeSpecies.equals("apple")) {
			newTree = new AppleTree(treeAge);
		}
		else if(treeSpecies.equals("pear")) {
			newTree = new PearTree(treeAge);
		}
		else if(treeSpecies.equals("cherry")) {
			newTree = new CherryTree(treeAge);
		}
		else {
			throw new InvalidTreeException();
		}
		
		this.repo.addElement(newTree);
	}
	
	// it only removes the first occurence of the tree (if it exists)
	public void removeElement(String treeSpecies, int treeAge) throws NonexistentElementException, InvalidTreeException {
		Tree newTree;
		
		if (treeSpecies.equals("apple")) {
			newTree = new AppleTree(treeAge);
		}
		else if(treeSpecies.equals("pear")) {
			newTree = new PearTree(treeAge);
		}
		else if(treeSpecies.equals("cherry")) {
			newTree = new CherryTree(treeAge);
		}
		else {
			throw new InvalidTreeException();
		}
		
		this.repo.removeElement(newTree);
	}
	
	public DynamicArray getAllTrees() {
		return this.repo.getAllElements();
	}
	
	public DynamicArray getAllTreesOlderThan() throws InvalidPositionException {
		DynamicArray allElements = this.repo.getAllElements();
		DynamicArray filteredList = new DynamicArray();
		
		for(int pos = 0; pos < allElements.getSize(); pos++) {
			Tree crtTree = allElements.getElementAtPosition(pos);
			if (crtTree.getAge() > Tree.TREE_MINIMUM_AGE) {
				filteredList.insert(crtTree);
			}
		}
		
		return filteredList;
	}
}
