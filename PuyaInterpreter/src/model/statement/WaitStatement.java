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

public class WaitStatement implements StatementInterface {
	private final ExpressionInterface waitTimeExpression;

	public WaitStatement(ExpressionInterface waitTimeExpression) {
		this.waitTimeExpression = waitTimeExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		
		int waitTimeAsInteger = ((IntValue)this.waitTimeExpression.evaluate(symbolTable, heap)).getValue();
		if (waitTimeAsInteger > 0) {
			crtState.getOutput().addLast(new IntValue(waitTimeAsInteger));
			stack.push(new WaitStatement(new ValueExpression(new IntValue(waitTimeAsInteger - 1))));
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("wait(" + this.waitTimeExpression.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (this.waitTimeExpression.typeCheck(initialTypeEnvironment).equals(new IntType()) == false) {
			throw new InvalidTypeException("WaitStatement: wait time should be an integer");
		}
		return initialTypeEnvironment;
	}
}
