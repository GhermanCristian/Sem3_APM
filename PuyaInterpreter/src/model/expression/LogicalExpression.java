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
	private final int operator;
	
	public LogicalExpression(ExpressionInterface firstExp, ExpressionInterface secondExp, int operator){
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operator = operator;
	}

	@Override
	public ValueInterface evaluate(DictionaryInterface<String, ValueInterface> symbolTable) throws Exception {
		ValueInterface firstVal, secondVal;
		firstVal = this.firstExp.evaluate(symbolTable);
		
		if (firstVal.getType() == new BoolType()) {
			secondVal = this.secondExp.evaluate(symbolTable);
			if (secondVal.getType().equals(new BoolType())) {
				boolean firstBoolean = ((BoolValue)firstVal).getValue();
				boolean secondBoolean = ((BoolValue)secondVal).getValue();
				
				if (this.operator == 0) {
					return new BoolValue(firstBoolean && secondBoolean);
				}
				if (this.operator == 1) {
					return new BoolValue(firstBoolean || secondBoolean);
				}
				else { // If I check the correctness of the operand before this (eg. in the controller/repo), I could just have case 1 as else
					throw new InvalidOperatorException();
				}
			}
			
			else {
				throw new InvalidTypeException("Second operand not a boolean");
			}
		}
		
		else {
			throw new InvalidTypeException("First operand not a boolean");
		}
	}
}
