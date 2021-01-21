package model.statement;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyLockTable;
import model.expression.ExpressionInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class CreateLatchStatement implements StatementInterface {
	private final String indexVariableName;
	private final ExpressionInterface countExpression;
	
	public CreateLatchStatement(String indexVariableName, ExpressionInterface countExpression) {
		this.indexVariableName = indexVariableName;
		this.countExpression = countExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		DictionaryInterface<Integer, Integer> latchTable = crtState.getLatchTable();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("CreateLatchStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		
		ValueInterface count = this.countExpression.evaluate(symbolTable, heap);
		int newPositionInLatchTable = ((MyLockTable<Integer, Integer>)(latchTable)).getFirstAvailablePosition();
		latchTable.insert(newPositionInLatchTable, ((IntValue)(count)).getValue());
		symbolTable.update(this.indexVariableName, new IntValue(newPositionInLatchTable));
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("createSemaphore(" + this.indexVariableName + ", " + this.countExpression.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("CreateLatchStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("CreateLatchStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		if (this.countExpression.typeCheck(initialTypeEnvironment).equals(new IntType()) == false) {
			throw new InvalidTypeException("CreateLatchStatement: Total permit count expression is not integer");
		}
		return initialTypeEnvironment;
	}
}
