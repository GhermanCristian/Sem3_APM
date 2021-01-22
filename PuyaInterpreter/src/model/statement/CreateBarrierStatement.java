package model.statement;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import exception.InvalidTypeException;
import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyLockTable;
import model.expression.ExpressionInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class CreateBarrierStatement implements StatementInterface {
	private final String indexVariableName;
	private final ExpressionInterface capacityExpression;
	private static Lock lock = new ReentrantLock(); 

	public CreateBarrierStatement(String indexVariableName, ExpressionInterface capacityExpression) {
		this.indexVariableName = indexVariableName;
		this.capacityExpression = capacityExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable = crtState.getBarrierTable();
		
		ValueInterface capacity = this.capacityExpression.evaluate(symbolTable, heap);
		lock.lock();
		int newPositionInBarrierTable = ((MyLockTable<Integer, Pair<Integer, ArrayList<Integer>>>)(barrierTable)).getFirstAvailablePosition();
		barrierTable.insert(newPositionInBarrierTable, new Pair<Integer, ArrayList<Integer>>(((IntValue)capacity).getValue(), new ArrayList<Integer>()));
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			symbolTable.insert(this.indexVariableName, new IntValue(newPositionInBarrierTable));
		}
		else {
			symbolTable.update(this.indexVariableName, new IntValue(newPositionInBarrierTable));
		}
		lock.unlock();
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("createBarrier(" + this.indexVariableName + ", " + this.capacityExpression.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			initialTypeEnvironment.insert(this.indexVariableName, new IntType());
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("CreateBarrierStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		if (this.capacityExpression.typeCheck(initialTypeEnvironment).equals(new IntType()) == false) {
			throw new InvalidTypeException("CreateBarrierStatement: Capacity expression is not integer");
		}
		return initialTypeEnvironment;
	}
}
