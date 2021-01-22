package view;

import java.util.ArrayList;
import java.util.Arrays;
import model.Example;
import model.Procedure;
import model.ADT.MyList;
import model.expression.ArithmeticExpression;
import model.expression.ExpressionInterface;
import model.expression.HeapReadingExpression;
import model.expression.RelationalExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.AcquirePermitStatement;
import model.statement.AssignmentStatement;
import model.statement.AwaitBarrierStatement;
import model.statement.AwaitLatchStatement;
import model.statement.CallProcedureStatement;
import model.statement.CloseReadFileStatement;
import model.statement.CompoundStatement;
import model.statement.ConditionalAssignmentStatement;
import model.statement.CountDownLatchStatement;
import model.statement.CreateBarrierStatement;
import model.statement.CreateLatchStatement;
import model.statement.CreateLockStatement;
import model.statement.CreateProcedureStatement;
import model.statement.CreateSemaphoreStatement;
import model.statement.EmptyStatement;
import model.statement.ForStatement;
import model.statement.ForkStatement;
import model.statement.HeapAllocationStatement;
import model.statement.HeapWritingStatement;
import model.statement.IfStatement;
import model.statement.IncrementStatement;
import model.statement.LockStatement;
import model.statement.OpenReadFileStatement;
import model.statement.PrintStatement;
import model.statement.ReadFileStatement;
import model.statement.ReleasePermitStatement;
import model.statement.RepeatUntilStatement;
import model.statement.SleepStatement;
import model.statement.StatementInterface;
import model.statement.UnlockStatement;
import model.statement.VariableDeclarationStatement;
import model.statement.WaitStatement;
import model.statement.WhileStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.StringValue;

public class AllExamples {
	private final String SRC_FOLDER_PATH = "C:\\Users\\gherm\\Documents\\EclipseWorkspace\\APM\\PuyaInterpreter\\logs";
	
	private StatementInterface composeStatement(MyList<StatementInterface> crtList){
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
	
	public Example getExample1() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// int a; a = 23; print(a);
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new AssignmentStatement("a", new ValueExpression(new IntValue(23))));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		
		return new Example(this.composeStatement(statementList), "int a; a = 23; print(a);", this.SRC_FOLDER_PATH + "\\log1.in");
	}
	
	public Example getExample2() {
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
		
		return new Example(this.composeStatement(statementList), "int a; int b; a = 2 + 3 * 5; b = a + 1; print(b);", this.SRC_FOLDER_PATH + "\\log2.in");
	}
	
	public Example getExample3() {
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
		
		return new Example(this.composeStatement(statementList), "bool a; int v; a=true; (If a Then v=2 Else v=3); print(v);", this.SRC_FOLDER_PATH + "\\log3.in");
	}
	
	public Example getExample4() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// openReadFile(str); int a; readFile(str); print(a); readFile(str); print(a); closeReadFile();
		ValueExpression val = new ValueExpression(new StringValue(this.SRC_FOLDER_PATH + "\\example4.in"));
		statementList.addLast(new OpenReadFileStatement(val));
		statementList.addLast(new VariableDeclarationStatement("a", new IntType()));
		statementList.addLast(new ReadFileStatement(val, "a"));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		statementList.addLast(new ReadFileStatement(val, "a"));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		statementList.addLast(new CloseReadFileStatement(val));
		
		return new Example(this.composeStatement(statementList), "openReadFile(str); int a; readFile(str); print(a); readFile(str); print(a); closeReadFile();", this.SRC_FOLDER_PATH + "\\log4.in");
	}
	
	public Example getExample5() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(v); print(a);
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))));
		statementList.addLast(new HeapAllocationStatement("a", new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new VariableExpression("a")));
		
		return new Example(this.composeStatement(statementList), "Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(v); print(a);", this.SRC_FOLDER_PATH + "\\log5.in");
	}
	
	public Example getExample6() {
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
		
		return new Example(this.composeStatement(statementList), "Ref int v; new(v, 23); Ref Ref int a; new(a, v); print(rH(v)); print(rH(rH(a)) + 5);", this.SRC_FOLDER_PATH + "\\log6.in");
	}
	
	public Example getExample7() {
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
		
		return new Example(this.composeStatement(statementList), "Ref int v; new(v, 23); print(rH(v)); wH(v, 24); print(rH(v) + 5);", this.SRC_FOLDER_PATH + "\\log7.in");
	}
	
	public Example getExample8() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// int v; v=4; while (v>0) {print(v); v--;} print(v);
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(4))));
		statementList.addLast(new WhileStatement(
								new RelationalExpression(
										new VariableExpression("v"), 
										new ValueExpression(new IntValue(0)), 
										">"), 
								new CompoundStatement(
									new PrintStatement(new VariableExpression("v")),
									new IncrementStatement("v", "-"))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return new Example(this.composeStatement(statementList), "int v; v=4; while (v>0) {print(v); v--;} print(v);", this.SRC_FOLDER_PATH + "\\log8.in");
	}
	
	public Example getExample9() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// Ref int v; new(v, 23); Ref Ref int a; new(a, v); new(v, 24); print(rH(rH(a)));
		statementList.addLast(new VariableDeclarationStatement("v", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(23))));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))));
		statementList.addLast(new HeapAllocationStatement("a", new VariableExpression("v")));
		statementList.addLast(new HeapAllocationStatement("v", new ValueExpression(new IntValue(24))));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a")))));
		
		return new Example(this.composeStatement(statementList), "Ref int v; new(v, 23); Ref Ref int a; new(a, v); new(v, 24); print(rH(rH(a)));", this.SRC_FOLDER_PATH + "\\log9.in");
	}
	
	public Example getExample10() {
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
		
		return new Example(this.composeStatement(statementList), "int v; Ref int a; v=10; new(a,22); fork(wH(a,30); v=32; print(v); print(rH(a))); print(v); print(rH(a));", this.SRC_FOLDER_PATH + "\\log10.in");
	}
	
	public Example getExample11() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		//int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; print(v)); v=32; print(v); print(rH(a))); print(v); print(rH(a));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))));
		
		MyList<StatementInterface> threadStatementList = new MyList<StatementInterface>();
		threadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))));
		
		MyList<StatementInterface> innerThreadStatementList = new MyList<StatementInterface>();
		innerThreadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(33))));
		innerThreadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		threadStatementList.addLast(new ForkStatement(this.composeStatement(innerThreadStatementList)));
		threadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(32))));
		threadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		statementList.addLast(new ForkStatement(this.composeStatement(threadStatementList)));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		return new Example(this.composeStatement(statementList), "int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; print(v)); v=32; print(v); print(rH(a))); print(v); print(rH(a));", this.SRC_FOLDER_PATH + "\\log11.in");
	}
	
	public Example getExample12() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		//int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; wH(a,24);); print(rH(a)); v=32; print(rH(a))); print(v); print(rH(a));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))));
		
		MyList<StatementInterface> threadStatementList = new MyList<StatementInterface>();
		threadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))));
		
		MyList<StatementInterface> innerThreadStatementList = new MyList<StatementInterface>();
		innerThreadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(33))));
		innerThreadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(24))));
		
		threadStatementList.addLast(new ForkStatement(this.composeStatement(innerThreadStatementList)));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		threadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(32))));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		statementList.addLast(new ForkStatement(this.composeStatement(threadStatementList)));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		return new Example(this.composeStatement(statementList), "int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; wH(a,24);); print(rH(a)); v=32; print(rH(a))); print(v); print(rH(a));", this.SRC_FOLDER_PATH + "\\log12.in");
	}
	
	public Example getExample13() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		//int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; fork(v=34; print(v);) print(v); ); print(rH(a)); v=32; print(rH(a))); print(v); print(rH(a));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))));
		
		MyList<StatementInterface> threadStatementList = new MyList<StatementInterface>();
		threadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))));
		
		MyList<StatementInterface> innerThreadStatementList = new MyList<StatementInterface>();
		innerThreadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(33))));
		
		MyList<StatementInterface> innerInnerThreadStatementList = new MyList<StatementInterface>();
		innerInnerThreadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(34))));
		innerInnerThreadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		innerThreadStatementList.addLast(new ForkStatement(this.composeStatement(innerInnerThreadStatementList)));
		innerThreadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		threadStatementList.addLast(new ForkStatement(this.composeStatement(innerThreadStatementList)));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		threadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(32))));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		statementList.addLast(new ForkStatement(this.composeStatement(threadStatementList)));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		return new Example(this.composeStatement(statementList), "int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(v=33; fork(v=34; print(v);) print(v); ); print(rH(a)); v=32; print(rH(a))); print(v); print(rH(a));", this.SRC_FOLDER_PATH + "\\log13.in");
	}
	
	public Example getExample14() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		//int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(fork(wH(a,35); print(v);); v=33; print(v); ); print(rH(a)); v=32; print(rH(a))); print(v); print(rH(a));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(22))));
		
		MyList<StatementInterface> threadStatementList = new MyList<StatementInterface>();
		threadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))));
		
		MyList<StatementInterface> innerThreadStatementList = new MyList<StatementInterface>();
		MyList<StatementInterface> innerInnerThreadStatementList = new MyList<StatementInterface>();
		innerInnerThreadStatementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(35))));
		innerInnerThreadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		innerThreadStatementList.addLast(new ForkStatement(this.composeStatement(innerInnerThreadStatementList)));
		innerThreadStatementList.addLast(new PrintStatement(new VariableExpression("v")));
		innerThreadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(33))));
		
		threadStatementList.addLast(new ForkStatement(this.composeStatement(innerThreadStatementList)));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		threadStatementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(32))));
		threadStatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		statementList.addLast(new ForkStatement(this.composeStatement(threadStatementList)));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))));
		
		return new Example(this.composeStatement(statementList), "int v; Ref int a; v=10; new(a,22); fork(wH(a,30); fork(fork(wH(a,35); print(v);); v=33; print(v); ); print(rH(a)); v=32; print(rH(a))); print(v); print(rH(a));", this.SRC_FOLDER_PATH + "\\log14.in");
	}
	
	public Example getExample15() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// int v; v=2; while (v>0) {fork(print(v + 23);); v--;} print(v);
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(2))));
		statementList.addLast(new WhileStatement(
								new RelationalExpression(
										new VariableExpression("v"), 
										new ValueExpression(new IntValue(0)), 
										">"), 
								new CompoundStatement(
									new ForkStatement(
											new PrintStatement(
													new ArithmeticExpression(
															new VariableExpression("v"), 
															new ValueExpression(new IntValue(23)), 
															"+"))),
									new IncrementStatement("v", "-"))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return new Example(this.composeStatement(statementList), "int v; v=2; while (v>0) {fork(print(v + 23);); v--;} print(v);", this.SRC_FOLDER_PATH + "\\log15.in");
	}
	
	public Example getExample16() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// for(int v = 4; v > 0; v--) {print(v);} print(v);
		statementList.addLast(new ForStatement(
								"v",
								new ValueExpression(new IntValue(4)), 
								new RelationalExpression(
										new VariableExpression("v"), 
										new ValueExpression(new IntValue(0)), 
										">"), 
								new IncrementStatement("v", "-"), 
								new PrintStatement(new VariableExpression("v"))
							));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return new Example(this.composeStatement(statementList), "for(int v = 4; v > 0; v--) {print(v);} print(v);", this.SRC_FOLDER_PATH + "\\log16.in");
	}
	
	public Example getExample17() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// for(int v = 2; v > 0; v--) {fork(print(v + 23);} print(v);
		statementList.addLast(new ForStatement(
								"v",
								new ValueExpression(new IntValue(2)), 
								new RelationalExpression(
										new VariableExpression("v"), 
										new ValueExpression(new IntValue(0)), 
										">"), 
								new IncrementStatement("v", "-"), 
								new ForkStatement(new PrintStatement(
													new ArithmeticExpression(
														new VariableExpression("v"), 
														new ValueExpression(new IntValue(23)), 
														"+")))
							));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return new Example(this.composeStatement(statementList), "for(int v = 2; v > 0; v--) {fork(print(v + 23);} print(v);", this.SRC_FOLDER_PATH + "\\log17.in");
	}
	
	public Example getExample18() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		// int v; v = 0; repeat {fork(print(v); v--;); v++;} until (v == 3); int x; x = 1; print(v * 10);
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(0))));
		statementList.addLast(new RepeatUntilStatement(
								new CompoundStatement(
									new ForkStatement(new CompoundStatement(
														new PrintStatement(new VariableExpression("v")), 
														new IncrementStatement("v", "-"))), 
									new IncrementStatement("v", "+")), 
								new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(3)), "==")));
		statementList.addLast(new VariableDeclarationStatement("x", new IntType()));
		statementList.addLast(new AssignmentStatement("x", new ValueExpression(new IntValue(1))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), "*")));
		
		return new Example(this.composeStatement(statementList), "int v; v = 0; repeat {fork(print(v); v--;); v++;} until (v == 3); int x; x = 1; print(v * 10);", this.SRC_FOLDER_PATH + "\\log18.in");
	}
	
	public Example getExample19() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
	
		statementList.addLast(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("cnt", new IntType()));
		statementList.addLast(new HeapAllocationStatement("v1", new ValueExpression(new IntValue(1))));
		statementList.addLast(new CreateSemaphoreStatement("cnt", new HeapReadingExpression(new VariableExpression("v1"))));
		
		MyList<StatementInterface> thread2StatementList = new MyList<StatementInterface>();
		thread2StatementList.addLast(new AcquirePermitStatement("cnt"));
		thread2StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		thread2StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		thread2StatementList.addLast(new ReleasePermitStatement("cnt"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));
		
		MyList<StatementInterface> thread3StatementList = new MyList<StatementInterface>();
		thread3StatementList.addLast(new AcquirePermitStatement("cnt"));
		thread3StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		thread3StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(2)), "*")));
		thread3StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		thread3StatementList.addLast(new ReleasePermitStatement("cnt"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));
		
		statementList.addLast(new AcquirePermitStatement("cnt"));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), "-")));
		statementList.addLast(new ReleasePermitStatement("cnt"));
		
		return new Example(this.composeStatement(statementList), "Ref int v1; int cnt; new(v1,1); createSemaphore(cnt,rH(v1)); " + 
				"fork(acquire(cnt); wh(v1,rh(v1)*10)); print(rh(v1)); release(cnt)); " + 
				"fork(acquire(cnt); wh(v1,rh(v1)*10)); wh(v1,rh(v1)*2)); print(rh(v1)); release(cnt)); " + 
				"acquire(cnt); print(rh(v1)-1); release(cnt);", this.SRC_FOLDER_PATH + "\\log19.in");
	}
	
	public Example getExample20() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
	
		statementList.addLast(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v3", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("cnt", new IntType()));
		statementList.addLast(new HeapAllocationStatement("v1", new ValueExpression(new IntValue(2))));
		statementList.addLast(new HeapAllocationStatement("v2", new ValueExpression(new IntValue(3))));
		statementList.addLast(new HeapAllocationStatement("v3", new ValueExpression(new IntValue(4))));
		statementList.addLast(new CreateLatchStatement("cnt", new HeapReadingExpression(new VariableExpression("v2"))));
		
		MyList<StatementInterface> thread2StatementList = new MyList<StatementInterface>();
		thread2StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		thread2StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		thread2StatementList.addLast(new CountDownLatchStatement("cnt"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));
		
		MyList<StatementInterface> thread3StatementList = new MyList<StatementInterface>();
		thread3StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), "*")));
		thread3StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v2"))));
		thread3StatementList.addLast(new CountDownLatchStatement("cnt"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));
		
		MyList<StatementInterface> thread4StatementList = new MyList<StatementInterface>();
		thread4StatementList.addLast(new HeapWritingStatement("v3", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v3")), new ValueExpression(new IntValue(10)), "*")));
		thread4StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v3"))));
		thread4StatementList.addLast(new CountDownLatchStatement("cnt"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread4StatementList)));
		
		statementList.addLast(new AwaitLatchStatement("cnt"));
		statementList.addLast(new PrintStatement(new ValueExpression(new IntValue(100))));
		statementList.addLast(new CountDownLatchStatement("cnt"));
		statementList.addLast(new PrintStatement(new ValueExpression(new IntValue(100))));
		
		return new Example(this.composeStatement(statementList), "Ref int v1; Ref int v2; Ref int v3; int cnt; " + 
				"new(v1,2); new(v2,3); new(v3,4); newLatch(cnt,rH(v2)); fork(wh(v1,rh(v1)*10); print(rh(v1)); countDown(cnt);); " + 
				"fork(wh(v2,rh(v2)*10); print(rh(v2)); countDown(cnt);); fork(wh(v3,rh(v3)*10); print(rh(v3)); countDown(cnt);); " + 
				"await(cnt); print(100); countDown(cnt); print(100);", this.SRC_FOLDER_PATH + "\\log20.in");
	}
	
	public Example getExample21() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		MyList<StatementInterface> procedure1StatementList = new MyList<StatementInterface>();
		procedure1StatementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		procedure1StatementList.addLast(new AssignmentStatement("v", new ArithmeticExpression(new VariableExpression("a"), new VariableExpression("b"), "+")));
		procedure1StatementList.addLast(new PrintStatement(new VariableExpression("v")));
		ArrayList<TypeInterface> procedure1Types = new ArrayList<TypeInterface>(Arrays.asList(new IntType(), new IntType()));
		ArrayList<String> procedure1Names = new ArrayList<String>(Arrays.asList("a", "b"));
		statementList.addLast(new CreateProcedureStatement("sum", new Procedure(procedure1Types, procedure1Names, this.composeStatement(procedure1StatementList))));
	
		MyList<StatementInterface> procedure2StatementList = new MyList<StatementInterface>();
		procedure2StatementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		procedure2StatementList.addLast(new AssignmentStatement("v", new ArithmeticExpression(new VariableExpression("a"), new VariableExpression("b"), "*")));
		procedure2StatementList.addLast(new PrintStatement(new VariableExpression("v")));
		ArrayList<TypeInterface> procedure2Types = new ArrayList<TypeInterface>(Arrays.asList(new IntType(), new IntType()));
		ArrayList<String> procedure2Names = new ArrayList<String>(Arrays.asList("a", "b"));
		statementList.addLast(new CreateProcedureStatement("product", new Procedure(procedure2Types, procedure2Names, this.composeStatement(procedure2StatementList))));
		
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(2))));
		statementList.addLast(new VariableDeclarationStatement("w", new IntType()));
		statementList.addLast(new AssignmentStatement("w", new ValueExpression(new IntValue(5))));
		ArrayList<ExpressionInterface> procedure1Call1Values = new ArrayList<ExpressionInterface>(Arrays.asList(
				new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), "*"),
				new VariableExpression("w")
		));
		statementList.addLast(new CallProcedureStatement("sum", procedure1Call1Values));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		MyList<StatementInterface> thread2StatementList = new MyList<StatementInterface>();
		ArrayList<ExpressionInterface> procedure2Call1Values = new ArrayList<ExpressionInterface>(Arrays.asList(
			new VariableExpression("v"), new VariableExpression("w")));
		thread2StatementList.addLast(new CallProcedureStatement("product", procedure2Call1Values));
		
		MyList<StatementInterface> thread3StatementList = new MyList<StatementInterface>();
		ArrayList<ExpressionInterface> procedure1Call2Values = new ArrayList<ExpressionInterface>(Arrays.asList(
			new VariableExpression("v"), new VariableExpression("w")));
		thread3StatementList.addLast(new CallProcedureStatement("sum", procedure1Call2Values));
		thread2StatementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));
		
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));
		
		return new Example(this.composeStatement(statementList), "procedure sum(int a, int b) {int v = a + b; print(v);} procedure product(int a, int b) {int v = a * b; print(v);} int v = 2; int w = 5; sum(v * 10, w); print(v);", this.SRC_FOLDER_PATH + "\\log21.in");
	}
	
	public Example getExample22() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
		
		statementList.addLast(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v3", new ReferenceType(new IntType())));
		statementList.addLast(new HeapAllocationStatement("v1", new ValueExpression(new IntValue(2))));
		statementList.addLast(new HeapAllocationStatement("v2", new ValueExpression(new IntValue(3))));
		statementList.addLast(new HeapAllocationStatement("v3", new ValueExpression(new IntValue(4))));
		statementList.addLast(new CreateBarrierStatement("cnt", new HeapReadingExpression(new VariableExpression("v2"))));
		
		MyList<StatementInterface> thread2StatementList = new MyList<StatementInterface>();
		thread2StatementList.addLast(new AwaitBarrierStatement("cnt"));
		thread2StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		thread2StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));
		
		MyList<StatementInterface> thread3StatementList = new MyList<StatementInterface>();
		thread3StatementList.addLast(new AwaitBarrierStatement("cnt"));
		thread3StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), "*")));
		thread3StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), "*")));
		thread3StatementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v2"))));
		statementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));
		
		statementList.addLast(new AwaitBarrierStatement("cnt"));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v3"))));
		
		return new Example(this.composeStatement(statementList), "the one with the barrier", this.SRC_FOLDER_PATH + "\\log22.in");
	}
	
	public Example getExample23() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
	
		statementList.addLast(new VariableDeclarationStatement("v1", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v2", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("x", new IntType()));
		statementList.addLast(new VariableDeclarationStatement("q", new IntType()));
		statementList.addLast(new HeapAllocationStatement("v1", new ValueExpression(new IntValue(20))));
		statementList.addLast(new HeapAllocationStatement("v2", new ValueExpression(new IntValue(30))));
		statementList.addLast(new CreateLockStatement("x"));
		
		MyList<StatementInterface> thread2StatementList = new MyList<StatementInterface>();
		MyList<StatementInterface> thread3StatementList = new MyList<StatementInterface>();
		thread3StatementList.addLast(new LockStatement("x"));
		thread3StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), "-")));
		thread3StatementList.addLast(new UnlockStatement("x"));
		thread2StatementList.addLast(new ForkStatement(this.composeStatement(thread3StatementList)));
		thread2StatementList.addLast(new LockStatement("x"));
		thread2StatementList.addLast(new HeapWritingStatement("v1", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), "*")));
		thread2StatementList.addLast(new UnlockStatement("x"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));
		
		statementList.addLast(new CreateLockStatement("q"));
		
		MyList<StatementInterface> thread4StatementList = new MyList<StatementInterface>();
		MyList<StatementInterface> thread5StatementList = new MyList<StatementInterface>();
		thread5StatementList.addLast(new LockStatement("q"));
		thread5StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(5)), "+")));
		thread5StatementList.addLast(new UnlockStatement("q"));
		thread4StatementList.addLast(new ForkStatement(this.composeStatement(thread5StatementList)));
		thread4StatementList.addLast(new LockStatement("q"));
		thread4StatementList.addLast(new HeapWritingStatement("v2", new ArithmeticExpression(new HeapReadingExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), "*")));
		thread4StatementList.addLast(new UnlockStatement("q"));
		statementList.addLast(new ForkStatement(this.composeStatement(thread4StatementList)));
		
		statementList.addLast(new EmptyStatement());
		statementList.addLast(new EmptyStatement());
		statementList.addLast(new EmptyStatement());
		statementList.addLast(new EmptyStatement());
		statementList.addLast(new LockStatement("x"));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v1"))));
		statementList.addLast(new UnlockStatement("x"));
		statementList.addLast(new LockStatement("q"));
		statementList.addLast(new PrintStatement(new HeapReadingExpression(new VariableExpression("v2"))));
		statementList.addLast(new UnlockStatement("q"));
		
		return new Example(this.composeStatement(statementList), "normal lock", this.SRC_FOLDER_PATH + "\\log23.in");
	}
	
	public Example getExample24() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
	
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(10))));
		
		MyList<StatementInterface> thread2StatementList = new MyList<StatementInterface>();
		thread2StatementList.addLast(new IncrementStatement("v", "-"));
		thread2StatementList.addLast(new IncrementStatement("v", "-"));
		thread2StatementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new ForkStatement(this.composeStatement(thread2StatementList)));
		
		statementList.addLast(new SleepStatement(new ValueExpression(new IntValue(10))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), "*")));
		
		return new Example(this.composeStatement(statementList), "sleep", this.SRC_FOLDER_PATH + "\\log24.in");
	}
	
	public Example getExample25() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
	
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new AssignmentStatement("v", new ValueExpression(new IntValue(20))));
		statementList.addLast(new WaitStatement(new ValueExpression(new IntValue(10))));
		statementList.addLast(new PrintStatement(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), "*")));
		
		return new Example(this.composeStatement(statementList), "wait", this.SRC_FOLDER_PATH + "\\log25.in");
	}
	
	public Example getExample26() {
		MyList<StatementInterface> statementList = new MyList<StatementInterface>();
	
		statementList.addLast(new VariableDeclarationStatement("a", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("b", new ReferenceType(new IntType())));
		statementList.addLast(new VariableDeclarationStatement("v", new IntType()));
		statementList.addLast(new HeapAllocationStatement("a", new ValueExpression(new IntValue(0))));
		statementList.addLast(new HeapAllocationStatement("b", new ValueExpression(new IntValue(0))));
		statementList.addLast(new HeapWritingStatement("a", new ValueExpression(new IntValue(1))));
		statementList.addLast(new HeapWritingStatement("b", new ValueExpression(new IntValue(2))));
		statementList.addLast(new ConditionalAssignmentStatement("v", 
								new RelationalExpression(
									new HeapReadingExpression(new VariableExpression("a")), 
									new HeapReadingExpression(new VariableExpression("b")), 
									"<"), 
								new ValueExpression(new IntValue(100)), 
								new ValueExpression(new IntValue(200))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		statementList.addLast(new ConditionalAssignmentStatement("v", 
								new RelationalExpression(
									new ArithmeticExpression(
										new HeapReadingExpression(new VariableExpression("b")), 
										new ValueExpression(new IntValue(2)), 
										"-"), 
									new HeapReadingExpression(new VariableExpression("a")), 
									">"), 
								new ValueExpression(new IntValue(100)), 
								new ValueExpression(new IntValue(200))));
		statementList.addLast(new PrintStatement(new VariableExpression("v")));
		
		return new Example(this.composeStatement(statementList), "conditional assignment", this.SRC_FOLDER_PATH + "\\log26.in");
	}
	
	public MyList<Example> getAllExamples() {
		MyList<Example> exampleList = new MyList<Example>();
		
		exampleList.addLast(this.getExample1());
		/*exampleList.addLast(this.getExample2());
		exampleList.addLast(this.getExample3());
		exampleList.addLast(this.getExample4());
		exampleList.addLast(this.getExample5());
		exampleList.addLast(this.getExample6());
		exampleList.addLast(this.getExample7());
		exampleList.addLast(this.getExample8());
		exampleList.addLast(this.getExample9());
		exampleList.addLast(this.getExample10());
		exampleList.addLast(this.getExample11());
		exampleList.addLast(this.getExample12());
		exampleList.addLast(this.getExample13());
		exampleList.addLast(this.getExample14());
		exampleList.addLast(this.getExample15());
		exampleList.addLast(this.getExample16());
		exampleList.addLast(this.getExample17());
		exampleList.addLast(this.getExample18());
		exampleList.addLast(this.getExample19());
		exampleList.addLast(this.getExample20());
		exampleList.addLast(this.getExample21());
		exampleList.addLast(this.getExample22());
		exampleList.addLast(this.getExample23());
		exampleList.addLast(this.getExample24());
		exampleList.addLast(this.getExample25());*/
		exampleList.addLast(this.getExample26());
		
		return exampleList;
	}
}
