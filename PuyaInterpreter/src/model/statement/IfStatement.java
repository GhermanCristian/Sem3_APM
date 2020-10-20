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
		if (conditionalExpressionValue.getType().equals(new BoolType())) {
			// the expression is true => go to the first branch
			if (((BoolValue)conditionalExpressionValue).getValue() == true) {
				stack.push(this.trueConditionStatement);
			}
			// the expression is false => go to the second branch
			else {
				stack.push(this.falseConditionStatement);
			}
		}
		else {
			throw new InvalidTypeException("Conditional expression is not boolean");
		}
		
		return crtState;
	}
	
	public String toString() {
		String representation = "";
		representation += ("if ("+ this.conditionalExpression.toString() + "){\n");
		representation += (this.trueConditionStatement.toString() + "\n}\n");
		representation += ("else {" + this.falseConditionStatement.toString() + "\n}");
		return representation;
	}

}
