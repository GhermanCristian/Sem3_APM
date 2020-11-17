package model.expression;

import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import model.ADT.DictionaryInterface;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.ValueInterface;

public class LogicalExpression implements ExpressionInterface{
	private final ExpressionInterface firstExp;
	private final ExpressionInterface secondExp;
	private final String operator;
	
	public LogicalExpression(ExpressionInterface firstExp, ExpressionInterface secondExp, String operator){
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operator = operator;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {
		ValueInterface firstVal, secondVal;
		firstVal = this.firstExp.evaluate(symbolTable, heap);
		
		if (firstVal.getType().equals(new BoolType()) == false) {
			throw new InvalidTypeException("First operand not a boolean");
		}
		
		secondVal = this.secondExp.evaluate(symbolTable, heap);
		if (secondVal.getType().equals(new BoolType()) == false) {
			throw new InvalidTypeException("Second operand not a boolean");
		}
		
		boolean firstBoolean = ((BoolValue)firstVal).getValue();
		boolean secondBoolean = ((BoolValue)secondVal).getValue();
		
		if (this.operator == "&&") {
			return new BoolValue(firstBoolean && secondBoolean);
		}
		if (this.operator == "||") {
			return new BoolValue(firstBoolean || secondBoolean);
		}
		else { // If I check the correctness of the operand before this (eg. in the controller/repo), I could just have case 1 as else
			throw new InvalidOperatorException();
		}
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += (this.firstExp.toString());
		representation += (" " + this.operator + " ");
		representation += (this.secondExp.toString());
		return representation;
	}
}
