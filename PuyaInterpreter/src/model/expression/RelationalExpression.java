package model.expression;

import exception.InvalidOperatorException;
import exception.InvalidTypeException;
import model.ADT.DictionaryInterface;
import model.type.BoolType;
import model.type.IntType;
import model.type.TypeInterface;
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
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable, DictionaryInterface<Integer, ValueInterface> heap) throws Exception {
		ValueInterface firstVal, secondVal;
		
		firstVal = this.firstExp.evaluate(symbolTable, heap);
		secondVal = this.secondExp.evaluate(symbolTable, heap);
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
			throw new InvalidOperatorException("RelationalExpression: Invalid operator");
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
			throw new InvalidTypeException("RelationalExpression: First expression is not an integer");
		}
		if (secondType.equals(intType) == false) {
			throw new InvalidTypeException("RelationalExpression: Second expression is not an integer");
		}
		
		return new BoolType();
	}

}
