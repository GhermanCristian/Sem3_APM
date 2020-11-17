package model.expression;

import exception.DivisionByZeroException;
import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.value.IntValue;
import model.value.ValueInterface;

public class ArithmeticExpression implements ExpressionInterface{
	private final ExpressionInterface firstExp;
	private final ExpressionInterface secondExp;
	private final String operator;
	
	public ArithmeticExpression(ExpressionInterface firstExp, ExpressionInterface secondExp, String operator) {
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operator = operator;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {
		ValueInterface firstVal, secondVal;
		firstVal = this.firstExp.evaluate(symbolTable, heap);
	
		if (firstVal.getType().equals(new IntType()) == false) {
			throw new InvalidTypeException("First operand not an integer");
		}
		
		secondVal = this.secondExp.evaluate(symbolTable, heap);
		if (secondVal.getType().equals(new IntType()) == false) {
			throw new InvalidTypeException("Second operand not an integer");
		}
		
		int firstInt = ((IntValue)firstVal).getValue();
		int secondInt = ((IntValue)secondVal).getValue();
		
		if (this.operator.equals("+")) {
			return new IntValue(firstInt + secondInt);
		}
		if (this.operator.equals("-")) {
			return new IntValue(firstInt - secondInt);
		}
		if (this.operator.equals("*")) {
			return new IntValue(firstInt * secondInt);
		}
		if (this.operator.equals("/")) {
			if (secondInt == 0) {
				throw new DivisionByZeroException("Division by zero");
			}
			return new IntValue(firstInt / secondInt);
		}
		else { // If I check the correctness of the operand before this (eg. in the controller/repo), I could just have case 3 as else
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
