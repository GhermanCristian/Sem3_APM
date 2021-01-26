package model.statement;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import javafx.util.Pair;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.type.IntType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ValueInterface;

public class AwaitBarrierStatement implements StatementInterface {
	private final String indexVariableName; // "cnt"
	private static Lock lock = new ReentrantLock();
	
	public AwaitBarrierStatement(String indexVariableName) {
		this.indexVariableName = indexVariableName;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("AwaitBarrierStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		
		int barrierIndexAsInteger = ((IntValue)(symbolTable.getValue(this.indexVariableName))).getValue();
		
		lock.lock();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable = crtState.getBarrierTable();
		if (barrierTable.isDefined(barrierIndexAsInteger) == false) {
			throw new UndefinedVariableException("AwaitBarrierStatement: Variable " + this.indexVariableName + " is not a valid index in the barrier table");
		}
		// revin imediat
		Pair<Integer, ArrayList<Integer>> barrierValue = barrierTable.getValue(barrierIndexAsInteger);
		Integer capacity = barrierValue.getKey();
		ArrayList<Integer> currentWaitingThreads = barrierValue.getValue();
		
		if (currentWaitingThreads.size() < capacity) { // the thread will have to wait for the barrier to "fill"
			if (currentWaitingThreads.contains(crtState.getThreadID()) == false) {
				currentWaitingThreads.add(crtState.getThreadID());
				barrierTable.update(barrierIndexAsInteger, new Pair<Integer, ArrayList<Integer>>(capacity, currentWaitingThreads));
			}
			
			stack.push(this); // will wait one more step for the barrier to "fill"
		}
		lock.unlock();
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("awaitBarrier(" + this.indexVariableName + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("AwaitBarrierStatement: Variable " + this.indexVariableName + " is not defined in the typeEnvironment");
		}
		if (initialTypeEnvironment.getValue(this.indexVariableName).equals(new IntType()) == false) {
			throw new InvalidTypeException("AwaitBarrierStatement: Variable " + this.indexVariableName + " is not an integer");
		}
		return initialTypeEnvironment;
	}
}
