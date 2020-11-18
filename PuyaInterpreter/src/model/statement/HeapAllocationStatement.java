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
		TypeInterface referencedType = ((ReferenceValue)variableValue).getReferencedType();
		
		if (expressionValue.getType().equals(referencedType) == false){
			throw new InvalidTypeException("Expression cannot be evaluated to a " + referencedType.toString());
		}
		
		int previousHeapAddress = ((ReferenceValue)variableValue).getHeapAddress();
		if (previousHeapAddress == ReferenceValue.DEFAULT_HEAP_ADDRESS) {
			// it means that the reference exists, but is not in the heap yet
			int newPositionInHeap = ((MyHeap<Integer, ValueInterface>)heap).getFirstAvailablePosition();
			heap.insert(newPositionInHeap, expressionValue);
			symbolTable.update(this.variableName, new ReferenceValue(newPositionInHeap, referencedType));
		}
		else {
			// it means that the variable that we want to assign a new value to alreeady is in the heap and has sth assigned to it
			// for example, doing Ref v; new (v, 20); new (v, 21) -> the second new is in this category
			heap.update(previousHeapAddress, expressionValue);
			// here we don't have to update the symbol table - same variable name, same position, same referenced type
			
			// I am only able to do this because we are not changing the referenced type
			// Otherwise, this approach should not be used (think of array resizing - when we do the second new, 
			// we cannot paste it over the previous memory location, because it might have a different size
			// I should probably change that in the future
		}
		
		return crtState;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("new(" + this.variableName + ", " + this.expression.toString() + ");\n");
		return representation;
	}
}
