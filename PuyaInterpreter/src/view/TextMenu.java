package view;

import java.util.Scanner;

import model.ADT.MyDictionary;

public class TextMenu {
	private MyDictionary<String, Command> commands;
	
	public TextMenu() {
		AllExamples allExamples = new AllExamples();
		this.commands = new MyDictionary<String, Command>();
		
		this.addCommand(new ExitCommand("0", "Exit program"));
		allExamples.getAllExamples().forEach(example -> this.addCommand(new RunExampleCommand(Integer.toString(this.commands.size()), example)));
	}
	
	private void addCommand(Command newCommand) {
		this.commands.insert(newCommand.getKey(), newCommand);
	}
	
	private void printMenu() {
		this.commands.forEachValue(command -> System.out.println(command.getKey() + ". " + command.getDescription()));
	}
	
	public void show() {
		Scanner inputScanner = new Scanner(System.in);
		boolean finishedProgram = false;
		
		while (finishedProgram == false) {
			this.printMenu();
			String option = inputScanner.nextLine();
			Command crtCommand = this.commands.getValue(option);
			
			if (crtCommand == null) {
				System.out.println("Invalid option");
			}
			else {
				if (option == "0") {
					finishedProgram = true;
				}
				try {
					crtCommand.execute();
				}
				catch (Exception e) {
					System.out.println(e);
				}
			}
		}
		
		inputScanner.close();
	}
}
