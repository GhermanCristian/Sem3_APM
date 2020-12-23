package model.expression;

import exception.DivisionByZeroException;
import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.type.TypeInterface;
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
		secondVal = this.secondExp.evaluate(symbolTable, heap);
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
				throw new DivisionByZeroException("ArithmeticExpression: Division by zero");
			}
			return new IntValue(firstInt / secondInt);
		}
		else { // If I check the correctness of the operand before this (eg. in the controller/repo), I could just have case 3 as else
			throw new InvalidOperatorException("ArithmeticExpression: Invalid operator");
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

	@Override
	public TypeInterface typeCheck(DictionaryInterface<String, TypeInterface> typeEnvironment) throws Exception {
		TypeInterface firstType, secondType, intType;
		firstType = this.firstExp.typeCheck(typeEnvironment);
		secondType = this.secondExp.typeCheck(typeEnvironment);
		intType = new IntType();
		
		if (firstType.equals(intType) == false) {
			throw new InvalidTypeException("ArithmeticExpression: First expression is not an integer");
		}
		if (secondType.equals(intType) == false) {
			throw new InvalidTypeException("ArithmeticExpression: Second expression is not an integer");
		}
		
		return intType;
	}
}
