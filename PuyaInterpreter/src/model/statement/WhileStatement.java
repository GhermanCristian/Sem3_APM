package model.statement;

import exception.InvalidTypeException;
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
