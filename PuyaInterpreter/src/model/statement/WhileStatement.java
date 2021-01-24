package model.statement;

import exception.InvalidTypeException;
import exception.StackOverflowException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.type.BoolType;
import model.type.TypeInterface;
import model.value.BoolValue;
import model.value.ValueInterface;

public class WhileStatement implements StatementInterface {
	private final ExpressionInterface conditionalExpression;
	private final StatementInterface statement;
	private final boolean expectedLogicalValue; // this is used so that we can have conditions like 'while (x == false)'
	
	public WhileStatement(ExpressionInterface conditionalExpression, StatementInterface statement) {
		this.conditionalExpression = conditionalExpression;
		this.statement = statement;
		this.expectedLogicalValue = true;
	}
	
	public WhileStatement(ExpressionInterface conditionalExpression, StatementInterface statement, boolean expectedLogicalValue) {
		this.conditionalExpression = conditionalExpression;
		this.statement = statement;
		this.expectedLogicalValue = expectedLogicalValue;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		
		ValueInterface conditionalExpressionValue = this.conditionalExpression.evaluate(symbolTable, heap);
		if (((BoolValue)conditionalExpressionValue).getValue() == expectedLogicalValue) {
			if (stack.size() >= CallProcedureStatement.STACK_PROCEDURE_LIMIT) {
				throw new StackOverflowException("WhileStatement: stack overflow");
			}
			
			stack.push(this);
			// for the while statement, we always have at least 1 last step (sees that the condition is false and exits, without creating those variables)
			// so variables defined inside the scope of the while will not be visible in the outer symbol table, regardless of whether
			// there is anything to do after it
			stack.push(new ClearOutOfScopeVariablesStatement(symbolTable.clone()));
			return this.statement.execute(crtState);
		}
		
		return null; // if the condition is not met, obviously no new threads can be created
	}
	
	public String toString() {
		String representation = "";
		String negationSymbolString = "";
		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the statement
		if (this.expectedLogicalValue == false) {
			negationSymbolString += "! ";
		}
		representation += ("while (" + negationSymbolString + this.conditionalExpression.toString() + ") {\n\t");
		representation += (this.statement.toString() + "}\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (this.conditionalExpression.typeCheck(initialTypeEnvironment).equals(new BoolType()) == false) {
			throw new InvalidTypeException("WhileStatement: Conditional expression is not boolean");
		}
		this.statement.getTypeEnvironment(initialTypeEnvironment.clone());
		return initialTypeEnvironment;
	}
}
