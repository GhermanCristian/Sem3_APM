package view;

import model.Example;

public class View {
	private int exampleCount = 1;
	
	private void insertExampleCommand(TextMenu textMenu, Example crtExample) {
		// I needed to create a separate method to insert examples because in the forEach I couldn't have a function with 2 arguments
		// (my forEach is defined for simple Consumers, I would've needed to use BiConsumer - however, that would've required knowing
		// the type of the second argument when writing the forEach implementation (which I don't know)
		// ex: I pass as arg to the forEach implementation a Consumer<? super TElem>, if I wanted a BiConsumer I would've needed
		// another type, such as UElem, which MyList knows nothing about
		textMenu.addCommand(new RunExampleCommand(Integer.toString(this.exampleCount), crtExample));
		this.exampleCount ++;
	}
	
	public void start() {
		AllExamples allExamples = new AllExamples();
		TextMenu textMenu = new TextMenu();
		
		textMenu.addCommand(new ExitCommand("0", "Exit program"));
		allExamples.getAllExamples().forEach(example -> this.insertExampleCommand(textMenu, example));
		textMenu.show();
	}
}
