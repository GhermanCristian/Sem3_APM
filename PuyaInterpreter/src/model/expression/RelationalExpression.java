package model.expression;

import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ValueInterface;

public class RelationalExpression implements ExpressionInterface{
	private final ExpressionInterface firstExp;
	private final ExpressionInterface secondExp;
	private final String operator;
	
	public RelationalExpression(ExpressionInterface firstExp, ExpressionInterface secondExp, String operator) {
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operator = operator;
	}
	
	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable) throws Exception {
		ValueInterface firstVal, secondVal;
		
		firstVal = this.firstExp.evaluate(symbolTable);
		if (firstVal.getType().equals(new IntType()) == false) {
			throw new InvalidTypeException("First operand is not an integer");
		}
		secondVal = this.secondExp.evaluate(symbolTable);
		if (secondVal.getType().equals(new IntType()) == false) {
			throw new InvalidTypeException("Second operand is not an integer");
		}
		int firstInt = ((IntValue)firstVal).getValue();
		int secondInt = ((IntValue)secondVal).getValue();
		
		if (this.operator.equals("<")) {
			return new BoolValue(firstInt < secondInt);
		}
		if (this.operator.equals("<=")) {
			return new BoolValue(firstInt <= secondInt);
		}
		if (this.operator.equals(">")) {
			return new BoolValue(firstInt > secondInt);
		}
		if (this.operator.equals(">=")) {
			return new BoolValue(firstInt >= secondInt);
		}
		if (this.operator.equals("==")) {
			return new BoolValue(firstInt == secondInt);
		}
		if (this.operator.equals("!=")) {
			return new BoolValue(firstInt != secondInt);
		}
		else {
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
