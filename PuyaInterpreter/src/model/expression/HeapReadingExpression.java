package model.expression;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ADT.DictionaryInterface;
import model.value.ReferenceValue;
import model.value.ValueInterface;

public class HeapReadingExpression implements ExpressionInterface{
	private final ExpressionInterface expression;
	
	public HeapReadingExpression(ExpressionInterface expression) {
		this.expression = expression;
	}
	
	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {		
		ValueInterface expressionValue = this.expression.evaluate(symbolTable, heap);
		if (expressionValue instanceof ReferenceValue == false) {
			throw new InvalidTypeException("Expression is not a Reference");
		}
		
		int heapAddress = ((ReferenceValue)expressionValue).getHeapAddress();
		if (heap.isDefined(heapAddress) == false) {
			throw new UndefinedVariableException("Undefined variable at address 0x" + Integer.toHexString(heapAddress));
		}
		return heap.getValue(heapAddress);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("*(" + this.expression.toString() + ")");
		return representation;
	}
}
