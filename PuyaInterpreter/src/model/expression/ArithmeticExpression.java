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
	private final int operator;
	
	public ArithmeticExpression(ExpressionInterface firstExp, ExpressionInterface secondExp, int operator) {
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operator = operator;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable) throws Exception {
		ValueInterface firstVal, secondVal;
		firstVal = this.firstExp.evaluate(symbolTable);
	
		if (firstVal.getType().equals(new IntType())) {
			secondVal = this.secondExp.evaluate(symbolTable);
			if (secondVal.getType().equals(new IntType())) {
				int firstInt = ((IntValue)firstVal).getValue();
				int secondInt = ((IntValue)secondVal).getValue();
				
				if (this.operator == 0) {
					return new IntValue(firstInt + secondInt);
				}
				if (this.operator == 1) {
					return new IntValue(firstInt - secondInt);
				}
				if (this.operator == 2) {
					return new IntValue(firstInt * secondInt);
				}
				if (this.operator == 3) {
					if (secondInt == 0) {
						throw new DivisionByZeroException("Division by zero");
					}
					return new IntValue(firstInt / secondInt);
				}
				else { // If I check the correctness of the operand before this (eg. in the controller/repo), I could just have case 3 as else
					throw new InvalidOperatorException();
				}
			}
			
			else {
				throw new InvalidTypeException("Second operand not an integer");
			}
		}
		
		else {
			throw new InvalidTypeException("First operand not an integer");
		}
	}
}
