package model.statement;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyHeap;
import model.expression.ExpressionInterface;
import model.type.ReferenceType;
import model.type.TypeInterface;
import model.value.ReferenceValue;
import model.value.ValueInterface;

public class HeapAllocationStatement implements StatementInterface {
	private final String variableName;
	private final ExpressionInterface expression;
	
	public HeapAllocationStatement(String variableName, ExpressionInterface expression) {
		this.variableName = variableName;
		this.expression = expression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		
		if (symbolTable.isDefined(this.variableName) == false) {
			throw new UndefinedVariableException("HeapAllocationStatement: " + this.variableName + " is not defined in the symbol table");
		}
		
		ValueInterface variableValue = symbolTable.getValue(this.variableName);
		ValueInterface expressionValue = this.expression.evaluate(symbolTable, heap);
		TypeInterface referencedType = ((ReferenceValue)variableValue).getReferencedType();
		
		int newPositionInHeap = ((MyHeap<Integer, ValueInterface>)heap).getFirstAvailablePosition();
		heap.insert(newPositionInHeap, expressionValue);
		symbolTable.update(this.variableName, new ReferenceValue(newPositionInHeap, referencedType));
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("new(" + this.variableName + ", " + this.expression.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		TypeInterface expressionReferenceType = new ReferenceType(this.expression.typeCheck(initialTypeEnvironment));
		// the type of the reference that "variableName" is allocated to
		// if getValue does not return a ReferenceValue, the equals will fail; it will also fail if the inner types don't match
		// so we're doing both checks simultaneously
		if (initialTypeEnvironment.isDefined(this.variableName) == false) {
			throw new UndefinedVariableException("HeapAllocationStatement: " + this.variableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.variableName).equals(expressionReferenceType) == false) {
			throw new InvalidTypeException("HeapAllocationStatement: Expression cannot be evaluated to a " + expressionReferenceType.toString());
		}
		return initialTypeEnvironment;
	}
}
