package controller;

import repository.DynamicArray;
import repository.MemoryRepository;
import repository.Repository;
import model.AppleTree;
import model.CherryTree;
import model.PearTree;
import model.Tree;

public class Controller {
	private Repository repo;
	
	public Controller() {
		repo = new MemoryRepository();
	}
	
	public void addElement(String treeSpecies, int treeAge) throws Exception{
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
			throw new Exception("Invalid tree type");
		}
		
		this.repo.addElement(newTree);
	}
	
	// it only removes the first occurence of the tree (if it exists)
	public void removeElement(String treeSpecies, int treeAge) throws Exception {
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
			throw new Exception("Invalid tree type");
		}
		
		this.repo.removeElement(newTree);
	}
	
	public DynamicArray getAllTrees() {
		return this.repo.getAllElements();
	}
	
	public DynamicArray getAllTreesOlderThan() {
		DynamicArray allElements = this.repo.getAllElements();
		DynamicArray filteredList = new DynamicArray();
		
		for(int pos = 0; pos < allElements.getSize(); pos++) {
			try {
				Tree crtTree = allElements.getElementAtPosition(pos);
				if (crtTree.getAge() > Tree.TREE_AGE) {
					filteredList.insert(crtTree);
				}
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		return filteredList;
	}
}
