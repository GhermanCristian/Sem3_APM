package view;

import java.util.Scanner;
import controller.Controller;
import repository.DynamicArray;

public class MenuUI {
	private Controller controller;
	
	private void printArray(DynamicArray crtArray) {
		if (crtArray.getSize() == 0) {
			System.out.println("empty");
			return;
		}
		for(int pos = 0; pos < crtArray.getSize(); pos++) {
			try {
				System.out.println(crtArray.getElementAtPosition(pos).getStringRepresentation());
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public MenuUI() {
		this.controller = new Controller();
	}
	
	public void start() {
		int choice;
		String choiceString;
		Scanner myInput = new Scanner(System.in);
		
		while (true) {
			System.out.println("0. Exit");
			System.out.println("1. Add new tree");
			System.out.println("2. Delete tree");
			System.out.println("3. Show all trees");
			System.out.println("4. Show all trees older than 3 years");
			
			choice = -1; // a default value which will only remain if the input is invalid
			choiceString = myInput.next();
			try {
				choice = Integer.parseInt(choiceString);
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
			
			if (choice == 0) {
				System.out.println("Program execution has ended");
				break;
			}
			
			if (choice == 1) {
				System.out.print("Type (apple, pear, cherry): ");
				String treeType = myInput.next();
				System.out.print("Age: ");
				try {
					int treeAge = myInput.nextInt();
					this.controller.addElement(treeType, treeAge);
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			else if (choice == 2) {
				System.out.print("Type (apple, pear, cherry): ");
				String treeType = myInput.next();
				System.out.print("Age: ");
				try {
					int treeAge = myInput.nextInt();
					this.controller.removeElement(treeType, treeAge);
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			else if (choice == 3) {
				this.printArray(this.controller.getAllTrees());
			}
			
			else if (choice == 4) {
				try {
					this.printArray(this.controller.getAllTreesOlderThan());
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			else {
				System.out.println("Invalid choice");
			}
		}
		
		myInput.close();
	}
}
