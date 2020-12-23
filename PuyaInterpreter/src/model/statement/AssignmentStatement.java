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
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		
		if (symbolTable.isDefined(this.variableName) == false) {
			throw new UndefinedVariableException("AssignmentStatement: Variable " + this.variableName + " is not defined");
		}
		
		// the value of the new expression (the one we want to assign to the existing variable)
		ValueInterface newExpressionValue = this.expression.evaluate(symbolTable, heap);
		symbolTable.update(this.variableName, newExpressionValue);
		
		return null;
	}
	
	public String toString() {
		String representation = "";
		representation += (this.variableName + " = " + this.expression.toString() + ";\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.getValue(this.variableName).equals(this.expression.typeCheck(initialTypeEnvironment)) == false) {
			throw new InvalidTypeException("AssignmentStatement: type of "+ this.variableName + " doesn't match the expression's type");
		}
		return initialTypeEnvironment; // the type environment remains unchanged
	}
}
