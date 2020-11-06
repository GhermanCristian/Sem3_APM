package model.statement;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.expression.ExpressionInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class AssignmentStatement implements StatementInterface{
	private final String variableName; // left operator
	private final ExpressionInterface expression;  // right operator

	public AssignmentStatement(String variableName, ExpressionInterface expression) {
		this.variableName = variableName;
		this.expression = expression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		
		if (symbolTable.isDefined(this.variableName) == false) {
			throw new UndefinedVariableException("Variable " + this.variableName + " is not defined");
		}
		
		// the value of the new expression (the one we want to assign to the existing variable)
		ValueInterface newExpressionValue = this.expression.evaluate(symbolTable);
		TypeInterface newExpressionType = newExpressionValue.getType();
		TypeInterface variableType = symbolTable.getValue(this.variableName).getType();
		
		// if the new expression's type matches the type of the existing variable => update
		if (variableType.equals(newExpressionType)) {
			symbolTable.update(this.variableName, newExpressionValue);
		}
		else {
			throw new InvalidTypeException("Type of " + this.variableName + " doesn't match type of expression");
		}
		
		return crtState;
	}
	
	public String toString() {
		String representation = "";
		representation += (this.variableName + " = " + this.expression.toString() + ";\n");
		return representation;
	}

}
