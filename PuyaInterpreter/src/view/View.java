package view;

import java.util.ListIterator;
import model.Example;

public class View {
	public void start() {
		AllExamples allExamples = new AllExamples();
		TextMenu textMenu = new TextMenu();
		
		// TO-DO: override forEach for MyList
		textMenu.addCommand(new ExitCommand("0", "Exit program"));
		int position = 1;
		ListIterator<Example> exampleIterator = allExamples.getAllExamples().getIterator();
		while (exampleIterator.hasNext()) {
			textMenu.addCommand(new RunExampleCommand(Integer.toString(position), exampleIterator.next()));
			position++;
		}
		
		textMenu.show();
	}
}
