package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.value.BoolValue;
import model.value.ValueInterface;

public class WhileStatement implements StatementInterface {
	private final ExpressionInterface conditionalExpression;
	private final StatementInterface statement;
	
	public WhileStatement(ExpressionInterface conditionalExpression, StatementInterface statement) {
		this.conditionalExpression = conditionalExpression;
		this.statement = statement;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		
		ValueInterface conditionalExpressionValue = this.conditionalExpression.evaluate(symbolTable, heap);
		if (conditionalExpressionValue instanceof BoolValue == false) {
			throw new InvalidTypeException("Conditional expression is not boolean");
		}
		
		if (((BoolValue)conditionalExpressionValue).getValue() == true) {
			stack.push(this);
			stack.push(this.statement);
		}
		
		return null;
	}
	
	public String toString() {
		String representation = "";
		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the statement
		representation += ("while ("+ this.conditionalExpression.toString() + ") {\n\t");
		representation += (this.statement.toString() + "}\n");
		return representation;
	}
}
