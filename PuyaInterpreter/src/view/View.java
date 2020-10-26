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
import model.expression.ArithmeticExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.AssignmentStatement;
import model.statement.CompoundStatement;
import model.statement.IfStatement;
import model.statement.PrintStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
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
				/*// int a; a = 23; print(a);
				StatementInterface originalProgram = new CompoundStatement(
					new CompoundStatement(
						new VariableDeclarationStatement("a", new IntType()), 
						new AssignmentStatement("a", new ValueExpression(new IntValue(5)))
					),
					new PrintStatement(new VariableExpression("a"))
				);*/
				
				// int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);
				/*StatementInterface originalProgram = new CompoundStatement(
					new VariableDeclarationStatement("a", new IntType()), 
					new CompoundStatement(
						new VariableDeclarationStatement("b", new IntType()), 
						new CompoundStatement(
							new AssignmentStatement("a", new ArithmeticExpression(
									new ArithmeticExpression(
											new ValueExpression(new IntValue(3)), 
											new ValueExpression(new IntValue(5)), 
											2), // 3 * 5 
									new ValueExpression(new IntValue(2)), 
									0)), // 3 * 5 + 2 
							new CompoundStatement(
								new AssignmentStatement("b", new ArithmeticExpression(
									new VariableExpression("a"), 
									new ValueExpression(new IntValue(1)), 
									0)), // b = a + 1
								new PrintStatement(new VariableExpression("b")) // print(b)
							)
						)
					)
				);*/
				
				//bool a; int v; a=true;(If a Then v=2 Else v=3);Print(v)
				StatementInterface originalProgram = new CompoundStatement(
					new VariableDeclarationStatement("a", new BoolType()), 
					new CompoundStatement(
						new VariableDeclarationStatement("v", new IntType()), 
						new CompoundStatement(
							new AssignmentStatement("a", new ValueExpression(new BoolValue(true))),
							new CompoundStatement(
								new IfStatement(
									new VariableExpression("a"), 
									new AssignmentStatement("v", new ValueExpression(new IntValue(2))), 
									new AssignmentStatement("v", new ValueExpression(new IntValue(3)))
								),
								new PrintStatement(new VariableExpression("v"))
							)
						)
					)
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
