package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.expression.ValueExpression;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class SleepStatement implements StatementInterface {
	private final ExpressionInterface sleepTimeExpression;

	public SleepStatement(ExpressionInterface sleepTimeExpression) {
		this.sleepTimeExpression = sleepTimeExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		
		int sleepTimeAsInteger = ((IntValue)this.sleepTimeExpression.evaluate(symbolTable, heap)).getValue();
		if (sleepTimeAsInteger > 0) {
			stack.push(new SleepStatement(new ValueExpression(new IntValue(sleepTimeAsInteger - 1))));
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("sleep(" + this.sleepTimeExpression.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (this.sleepTimeExpression.typeCheck(initialTypeEnvironment).equals(new IntType()) == false) {
			throw new InvalidTypeException("SleepStatement: sleep time should be an integer");
		}
		return initialTypeEnvironment;
	}
}
