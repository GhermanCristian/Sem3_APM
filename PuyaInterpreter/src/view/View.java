package view;

import java.io.BufferedReader;
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
import model.statement.CloseReadFileStatement;
import model.statement.CompoundStatement;
import model.statement.EmptyStatement;
import model.statement.IfStatement;
import model.statement.OpenReadFileStatement;
import model.statement.PrintStatement;
import model.statement.ReadFileStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;
import model.value.ValueInterface;
import repository.Repository;
import repository.RepositoryInterface;

public class View {
	private ControllerInterface controller;
	
	public View() {
		RepositoryInterface repo = new Repository("C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logFile.txt");
		this.controller = new Controller(repo);
	}
	
	private StatementInterface composeStatement(MyList<StatementInterface> crtList) throws Exception {
		// pop never actually throws an exception here (I use it when size() >= 1, and the exception occurs when size() = 0)
		// but if I didn't add it, the compiler would complain
		if (crtList.size() == 0) {
			return new EmptyStatement();
		}
		
		if (crtList.size() == 1) {
			return crtList.pop();
		}
		
		StatementInterface lastStatement = crtList.pop();
		return new CompoundStatement(composeStatement(crtList), lastStatement);
	}
	
	private MyList<StatementInterface> getFirstExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// int a; a = 23; print(a);
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new AssignmentStatement("a", new ValueExpression(new IntValue(23))));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getSecondExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("b", new IntType()));
		statementList.addLast(new AssignmentStatement("a", new ArithmeticExpression(
								new ArithmeticExpression(
										new ValueExpression(new IntValue(3)), 
										new ValueExpression(new IntValue(5)), 
										"*"), // 3 * 5 
								new ValueExpression(new IntValue(2)), 
								"+"))); // 3 * 5 + 2 );
		statementList.addLast(new AssignmentStatement("b", new ArithmeticExpression(
								new VariableExpression("a"), 
								new ValueExpression(new IntValue(1)), 
								"+"))); // b = a + 1);
		statementList.addLast(new PrintStatement(new VariableExpression("b")));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getThirdExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		//bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)
		statementList.addLast(new VariableDeclarationStatement("a", new BoolType()));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("a", new ValueExpression(new BoolValue(true))));
		statementList.addLast(new IfStatement(
								new VariableExpression("a"), 
								new AssignmentStatement("v", new ValueExpression(new IntValue(2))), 
								new AssignmentStatement("v", new ValueExpression(new IntValue(3)))
							));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getFourthExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// openReadFile(str); int var; readFile(str); print(var); readFile(str); print(var); closeReadFile();
		ValueExpression val = new ValueExpression(new StringValue("C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\log1.in"));
		statementList.addLast(new OpenReadFileStatement(val));
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new ReadFileStatement(val, "a"));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		statementList.addLast(new ReadFileStatement(val, "a"));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		statementList.addLast(new CloseReadFileStatement(val));
		
		return statementList;
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
				MyList<StatementInterface> statementList = new MyList<StatementInterface>();
				
				// int a; a = 23; print(a);
				//statementList = this.getFirstExample();
				// int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);
				//statementList = this.getSecondExample();
				//bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)
				//statementList = this.getThirdExample();
				// openReadFile(str); int var; readFile(str); print(var); readFile(str); print(var); closeReadFile();
				statementList = this.getFourthExample();
				
				StatementInterface originalProgram;
				try {
					originalProgram = this.composeStatement(statementList);
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
						
				StackInterface<StatementInterface> stack = new MyStack<StatementInterface>();
				DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<String, ValueInterface>();
				ListInterface<ValueInterface> output = new MyList<ValueInterface>();
				DictionaryInterface<StringValue, BufferedReader> fileTable = new MyDictionary<StringValue, BufferedReader>();
				ProgramState crtProgramState = new ProgramState(stack, symbolTable, output, fileTable, originalProgram);
				
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
