package view;

import controller.Controller;
import repository.DynamicArray;

public class UI {
	private Controller controller;
	
	private void printArray(DynamicArray crtArray) {
		for(int pos = 0; pos < crtArray.getSize(); pos++) {
			try {
				System.out.println(crtArray.getElementAtPosition(pos).getStringRepresentation());
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public UI() {
		this.controller = new Controller();
	}
	
	public void start() {
		try {
			this.controller.addElement("book", 240);
			this.controller.addElement("cake", 5);
			this.controller.addElement("cake", 240);
			this.controller.addElement("cake", 250);
			this.controller.addElement("apple", 140);
			this.controller.addElement("apple", 340);
			this.controller.addElement("book", 200);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			this.printArray(this.controller.getAllHeavierThan());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
