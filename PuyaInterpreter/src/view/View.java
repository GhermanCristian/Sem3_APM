package view;

import model.ADT.MyList;
import model.expression.ArithmeticExpression;
import model.expression.HeapReadingExpression;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.AssignmentStatement;
import model.statement.CloseReadFileStatement;
import model.statement.CompoundStatement;
import model.statement.EmptyStatement;
import model.statement.ForkStatement;
import model.statement.HeapAllocationStatement;
import model.statement.HeapWritingStatement;
import model.statement.IfStatement;
import model.statement.OpenReadFileStatement;
import model.statement.PrintStatement;
import model.statement.ReadFileStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.statement.WhileStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;

public class View {
	private final String SRC_FOLDER_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter";
	
	private StatementInterface composeStatement(MyList<StatementInterface> crtList){
		// pop never actually throws an exception here (I use it when size() >= 1, and the exception occurs when size() = 0)
		// but if I didn't add it, the compiler would complain
		if (crtList.size() == 0) {
			return new EmptyStatement();
		}
		
		if (crtList.size() == 1) {
			try {
				return crtList.pop();
			} 
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		StatementInterface lastStatement = null;
		try {
			lastStatement = crtList.pop();
			return new CompoundStatement(composeStatement(crtList), lastStatement);
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return lastStatement;
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
		
		//bool a; int v; a=true; (If a Then v=2 Else v=3); print(v);
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
		ValueExpression val = new ValueExpression(new StringValue(this.SRC_FOLDER_PATH + "\\log1.in"));
		statementList.addLast(new OpenReadFileStatement(val));
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new ReadFileStatement(val, "a"));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		statementList.addLast(new ReadFileStatement(val, "a"));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		statementList.addLast(new CloseReadFileStatement(val));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getFifthExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(v); print(a);
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))));
		statementList.addLast(new HeapAllocationStatement("a", new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getSixthExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(rH(v)); print(rH(rH(a)) + 5);
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))));
		statementList.addLast(new HeapAllocationStatement("a", new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v"))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(
													new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a"))), 
													new ValueExpression(new IntValue(5)), 
													"+")));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getSeventhExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// Ref int v; new(v, 23); print(rH(v)); wH(v, 24); print(rH(v) + 5);
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v"))));
		statementList.addLast(new HeapWritingStatement("v", new ValueExpression(new IntValue(24))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(
													new HeapReadingExpression(new VariableExpression("v")), 
													new ValueExpression(new IntValue(5)), 
													"+")));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getEighthExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// int v; v=4; (while (v>0) print(v); v = v - 1); print(v)
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(4))));
		statementList.addLast(new WhileStatement(
								new RelationalExpression(
										new VariableExpression("v"), 
										new ValueExpression(new IntValue(0)), 
										">"), 
								new CompoundStatement(
									new PrintStatement(new VariableExpression("v")),
									new AssignmentStatement("v", new ArithmeticExpression(
											new VariableExpression("v"), 
											new ValueExpression(new IntValue(1)), 
											"-")))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getNinthExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// Ref int v; new(v, 23); Ref Ref int a; new(a, v); new(v, 24); print(rH(rH(a)));
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))));
		statementList.addLast(new HeapAllocationStatement("a", new VariableExpression("v")));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(24))));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a")))));
		
		return statementList;
	}
	
	private MyList<StatementInterface> getTenthExample() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		//int v; Ref int a; v=10; new(a,22); fork(wH(a,30); v=32; print(v); print(rH(a))); print(v); print(rH(a));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))));
		
		MyList<StatementInterface> threadStatementList = new MyList<StatementInterface>();
		threadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))));
		threadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(32))));
		threadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		statementList.addLast(new ForkStatement(this.composeStatement(threadStatementList)));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		return statementList;
	}
	
	public void start() {
		TextMenu textMenu = new TextMenu();
		
		try {
			textMenu.addCommand(new ExitCommand("0", "Exit program"));
			textMenu.addCommand(new RunExampleCommand("1", "int a; a = 23; print(a);", this.composeStatement(this.getFirstExample()), this.SRC_FOLDER_PATH + "\\log1.in"));
			textMenu.addCommand(new RunExampleCommand("2", "int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);", this.composeStatement(this.getSecondExample()), this.SRC_FOLDER_PATH + "\\log2.in"));
			textMenu.addCommand(new RunExampleCommand("3", "bool a; int v; a=true; (If a Then v=2 Else v=3); print(v);", this.composeStatement(this.getThirdExample()), this.SRC_FOLDER_PATH + "\\log3.in"));
			textMenu.addCommand(new RunExampleCommand("4", "openReadFile(str); int var; readFile(str); print(var); readFile(str); print(var); closeReadFile();", this.composeStatement(this.getFourthExample()), this.SRC_FOLDER_PATH + "\\log4.in"));
			textMenu.addCommand(new RunExampleCommand("5", "Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(v); print(a);", this.composeStatement(this.getFifthExample()), this.SRC_FOLDER_PATH + "\\log5.in"));
			textMenu.addCommand(new RunExampleCommand("6", "Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(rH(v)); print(rH(rH(a)) + 5);", this.composeStatement(this.getSixthExample()), this.SRC_FOLDER_PATH + "\\log6.in"));
			textMenu.addCommand(new RunExampleCommand("7", "Ref int v; new(v, 23); print(rH(v)); wH(v, 24); print(rH(v) + 5);", this.composeStatement(this.getSeventhExample()), this.SRC_FOLDER_PATH + "\\log7.in"));
			textMenu.addCommand(new RunExampleCommand("8", "int v; v=4; (while (v>0) print(v); v = v - 1); print(v)", this.composeStatement(this.getEighthExample()), this.SRC_FOLDER_PATH + "\\log8.in"));
			textMenu.addCommand(new RunExampleCommand("9", "Ref int v; new(v, 23); Ref Ref int a; new(a, v); new(v, 24); print(rH(rH(a)));", this.composeStatement(this.getNinthExample()), this.SRC_FOLDER_PATH + "\\log9.in"));
			textMenu.addCommand(new RunExampleCommand("10", "int v; Ref int a; v=10; new(a,22); fork(wH(a,30); v=32; print(v); print(rH(a))); print(v); print(rH(a));", this.composeStatement(this.getTenthExample()), this.SRC_FOLDER_PATH + "\\log10.in"));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		textMenu.show();
	}
}
