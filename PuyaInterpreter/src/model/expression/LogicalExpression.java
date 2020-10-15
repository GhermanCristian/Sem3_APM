package model.expression;

import model.ADT.MyDictionary;
import model.type.BoolType;
import model.value.BoolValue;
import model.value.ValueInterface;

public class LogicalExpression implements ExpressionInterface{
	private final ExpressionInterface firstExp;
	private final ExpressionInterface secondExp;
	private final int operand;
	
	public LogicalExpression(ExpressionInterface firstExp, ExpressionInterface secondExp, int operand) throws Exception {
		this.firstExp = firstExp;
		this.secondExp = secondExp;
		this.operand = operand;
	}

	@Override
	public ValueInterface evaluate(MyDictionary<String, ValueInterface> symbolTable) throws Exception {
		ValueInterface firstVal, secondVal;
		firstVal = this.firstExp.evaluate(symbolTable);
		
		if (firstVal.getType() == new BoolType()) {
			secondVal = this.secondExp.evaluate(symbolTable);
			if (secondVal.getType().equals(new BoolType())) {
				boolean firstBoolean = ((BoolValue)firstVal).getValue();
				boolean secondBoolean = ((BoolValue)secondVal).getValue();
				
				if (this.operand == 0) {
					return new BoolValue(firstBoolean && secondBoolean);
				}
				if (this.operand == 1) {
					return new BoolValue(firstBoolean || secondBoolean);
				}
				else { // If I check the correctness of the operand before this (eg. in the controller/repo), I could just have case 1 as else
					throw new Exception("Invalid operand");
				}
			}
			
			else {
				throw new Exception("Second operand not a boolean");
			}
		}
		
		else {
			throw new Exception("First operand not a boolean");
		}
	}
}
