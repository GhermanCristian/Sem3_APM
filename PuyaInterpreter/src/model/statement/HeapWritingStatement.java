package model.statement;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.expression.ExpressionInterface;
import model.type.TypeInterface;
import model.value.ReferenceValue;
import model.value.ValueInterface;

public class HeapWritingStatement implements StatementInterface {
	private final String variableName;
	private final ExpressionInterface expression;
	
	public HeapWritingStatement(String variableName, ExpressionInterface expression) {
		this.variableName = variableName;
		this.expression = expression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		
		if (symbolTable.isDefined(this.variableName) == false) {
			throw new UndefinedVariableException(this.variableName + " is not defined in the symbol table");
		}
		
		ValueInterface variableValue = symbolTable.getValue(this.variableName);
		if (variableValue instanceof ReferenceValue == false) {
			throw new InvalidTypeException(this.variableName + " is not a Reference");
		}
		
		int positionInHeap = ((ReferenceValue)variableValue).getHeapAddress();
		if (heap.isDefined(positionInHeap) == false) {
			throw new UndefinedVariableException("Undefined variable at address 0x" + Integer.toHexString(positionInHeap));
		}
		
		ValueInterface expressionValue = this.expression.evaluate(symbolTable, heap);
		TypeInterface expressionType = expressionValue.getType();
		ValueInterface referencedValue = heap.getValue(positionInHeap);
		TypeInterface referencedType = referencedValue.getType();
		
		if (expressionType.equals(referencedType) == false) {
			throw new InvalidTypeException("The referenced type of " + this.variableName + " does not match the expression");
		}
		
		heap.update(positionInHeap, expressionValue);
		
		return crtState;
	}

	@Override
	public String toString() {
		String representation = "";
		representation += ("*(" + this.variableName + ") = " + this.expression.toString() + ";\n");
		return representation;
	}
}