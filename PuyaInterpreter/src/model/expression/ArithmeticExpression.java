package model.expression;

import model.ADT.DictionaryInterface;
import model.type.IntType;
import model.value.IntValue;
import model.value.ValueInterface;

public class ArithmeticExpression implements ExpressionInterface{
	private final ExpressionInterface firstExp;
	private final ExpressionInterface secondExp;
	private final int operand;
	
	public ArithmeticExpression(ExpressionInterface firstExp, ExpressionInterface secondExp, int operand) {
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operand = operand;
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
				
				if (this.operand == 0) {
					return new IntValue(firstInt + secondInt);
				}
				if (this.operand == 1) {
					return new IntValue(firstInt - secondInt);
				}
				if (this.operand == 2) {
					return new IntValue(firstInt * secondInt);
				}
				if (this.operand == 3) {
					if (secondInt == 0) {
						throw new Exception("Division by zero");
					}
					return new IntValue(firstInt / secondInt);
				}
				else { // If I check the correctness of the operand before this (eg. in the controller/repo), I could just have case 3 as else
					throw new Exception("Invalid operand");
				}
			}
			
			else {
				throw new Exception("Second operand not an integer");
			}
		}
		
		else {
			throw new Exception("First operand not an integer");
		}
	}
}
