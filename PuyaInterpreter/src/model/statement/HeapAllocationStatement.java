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
			throw new UndefinedVariableException(this.variableName + " is not defined in the symbol table");
		}
		
		ValueInterface variableValue = symbolTable.getValue(this.variableName);
		if (variableValue instanceof ReferenceValue == false) {
			throw new InvalidTypeException(this.variableName + " is not a Reference");
		}
		
		ValueInterface expressionValue = this.expression.evaluate(symbolTable, heap);
		TypeInterface expressionType = expressionValue.getType();
		TypeInterface referencedType = ((ReferenceValue)variableValue).getReferencedType();
		
		if (expressionType.equals(referencedType) == false){
			throw new InvalidTypeException("Expression cannot be evaluated to a " + referencedType.toString());
		}
		
		int positionInHeap = ((MyHeap<Integer, ValueInterface>)heap).getFirstAvailablePosition();
		heap.insert(positionInHeap, expressionValue);
		symbolTable.update(this.variableName, new ReferenceValue(positionInHeap, referencedType));
		
		return crtState;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("new(" + this.variableName + ", " + this.expression.toString() + ");\n");
		return representation;
	}
}
