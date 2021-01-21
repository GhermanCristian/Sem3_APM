package model.statement;

import java.util.ArrayList;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MySemaphoreTable;
import model.expression.ExpressionInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class CreateSemaphoreStatement implements StatementInterface {
	private final String indexVariableName;
	private final ExpressionInterface totalPermitCountExpression;

	public CreateSemaphoreStatement(String indexVariableName, ExpressionInterface totalPermitCountExpression) {
		this.indexVariableName = indexVariableName;
		this.totalPermitCountExpression = totalPermitCountExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable = crtState.getSemaphoreTable();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("CreateSemaphoreStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		
		ValueInterface totalPermitCount = this.totalPermitCountExpression.evaluate(symbolTable, heap);
		int newPositionInSemaphoreTable = ((MySemaphoreTable<Integer, Pair<Integer, ArrayList<Integer>>>)(semaphoreTable)).getFirstAvailablePosition();
		semaphoreTable.insert(newPositionInSemaphoreTable, new Pair<Integer, ArrayList<Integer>>(((IntValue)totalPermitCount).getValue(), new ArrayList<Integer>()));
		symbolTable.update(this.indexVariableName, new IntValue(newPositionInSemaphoreTable));
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("createSemaphore(" + this.indexVariableName + ", " + this.totalPermitCountExpression.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("CreateSemaphoreStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("CreateSemaphoreStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		if (this.totalPermitCountExpression.typeCheck(initialTypeEnvironment).equals(new IntType()) == false) {
			throw new InvalidTypeException("CreateSemaphoreStatement: Total permit count expression is not integer");
		}
		return initialTypeEnvironment;
	}
}
