package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.type.BoolType;
import model.type.TypeInterface;

public class RepeatUntilStatement implements StatementInterface {
	private final StatementInterface statement;
	private final ExpressionInterface conditionalExpression;

	public RepeatUntilStatement(StatementInterface statement, ExpressionInterface conditionalExpression) {
		this.statement = statement;
		this.conditionalExpression = conditionalExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		stack.push(new WhileStatement(this.conditionalExpression, this.statement, false));
		return this.statement.execute(crtState);
	}
	
	public String toString() {
		String representation = "";
		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the statement
		representation += ("repeat {\n" + this.statement.toString() + "} ");
		representation += (" until (" + this.conditionalExpression.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (this.conditionalExpression.typeCheck(initialTypeEnvironment).equals(new BoolType()) == false) {
			throw new InvalidTypeException("RepeatUntilStatement: Conditional expression is not boolean");
		}
		this.statement.getTypeEnvironment(initialTypeEnvironment.clone());
		return initialTypeEnvironment;
	}
}
