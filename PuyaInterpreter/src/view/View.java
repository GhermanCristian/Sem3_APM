package view;

import java.util.Scanner;
import controller.Controller;
import controller.ControllerInterface;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.AssignmentStatement;
import model.statement.CompoundStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.value.IntValue;
import model.value.ValueInterface;

public class View {
	private ControllerInterface controller;
	
	public View() {
		this.controller = new Controller();
	}
	
	public void start() {
		int choice;
		Scanner consoleScanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("0. Exit");
			System.out.println("1. Input program");
			System.out.println("2. Full program execution + print output");
			
			choice = consoleScanner.nextInt();
			
			if (choice == 0) {
				System.out.println("Program has ended");
				break;
			}
			
			if (choice == 1) {
				// int a; a = 23; print(a);
				StatementInterface originalProgram = new CompoundStatement(
					new CompoundStatement(
						new VariableDeclarationStatement("a", new IntType()), 
						new AssignmentStatement("a", new ValueExpression(new IntValue(5)))
					),
					new PrintStatement(new VariableExpression("a"))
				);
				
				StackInterface<StatementInterface> stack = new MyStack<StatementInterface>();
				DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<String, ValueInterface>();
				ListInterface<ValueInterface> output = new MyList<ValueInterface>();
				ProgramState crtProgramState = new ProgramState(stack, symbolTable, output, originalProgram);
				
				this.controller.addProgramState(crtProgramState);
			}
			
			else if(choice == 2) {
				try {
					ProgramState finishedProgramState = this.controller.fullProgramExecution();
					System.out.println(finishedProgramState.getOutput());
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			
			else {
				System.out.println("Invalid choice");
			}
		}
		
		consoleScanner.close();
	}
}
