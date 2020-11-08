package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.ValueInterface;

public class IfStatement implements StatementInterface{
	private final ExpressionInterface conditionalExpression;
	private final StatementInterface trueConditionStatement;
	private final StatementInterface falseConditionStatement;
	
	public IfStatement(ExpressionInterface conditionalExpression, StatementInterface trueConditionStatement, StatementInterface falseConditionStatement) {
		this.conditionalExpression = conditionalExpression;
		this.trueConditionStatement = trueConditionStatement;
		this.falseConditionStatement = falseConditionStatement;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		
		ValueInterface conditionalExpressionValue = this.conditionalExpression.evaluate(symbolTable);
		if (conditionalExpressionValue.getType().equals(new BoolType()) == false) {
			throw new InvalidTypeException("Conditional expression is not boolean");
		}
		
		if (((BoolValue)conditionalExpressionValue).getValue() == true) { // the expression is true => go to the first branch
			stack.push(this.trueConditionStatement);
		}
		else { // the expression is false => go to the second branch
			stack.push(this.falseConditionStatement);
		}
		
		return crtState;
	}
	
	public String toString() {
		String representation = "";
		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the if statement
		representation += ("if ("+ this.conditionalExpression.toString() + ") {\n\t");
		representation += (this.trueConditionStatement.toString() + "}\n");
		representation += ("else {\n\t" + this.falseConditionStatement.toString() + "}\n");
		return representation;
	}
}
