package model.statement;

import java.util.ArrayList;
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
	private final String indexVariableName;
	
	public AwaitBarrierStatement(String indexVariableName) {
		this.indexVariableName = indexVariableName;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable = crtState.getBarrierTable();
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		
		if (symbolTable.isDefined(this.indexVariableName) == false) {
			throw new UndefinedVariableException("AwaitBarrierStatement: Variable " + this.indexVariableName + " is not defined in the symbolTable");
		}
		
		int barrierIndexAsInteger = ((IntValue)(symbolTable.getValue(this.indexVariableName))).getValue();
		if (barrierTable.isDefined(barrierIndexAsInteger) == false) {
			throw new UndefinedVariableException("AwaitBarrierStatement: Variable " + this.indexVariableName + " is not a valid index in the barrier table");
		}
		
		Pair<Integer, ArrayList<Integer>> barrierValue = barrierTable.getValue(barrierIndexAsInteger);
		Integer capacity = barrierValue.getKey();
		ArrayList<Integer> currentWaitingThreads = barrierValue.getValue();
		if (currentWaitingThreads.size() < capacity) {
			if (currentWaitingThreads.contains(crtState.getThreadID()) == false) {
				currentWaitingThreads.add(crtState.getThreadID());
				barrierTable.update(barrierIndexAsInteger, new Pair<Integer, ArrayList<Integer>>(capacity, currentWaitingThreads));
			}
			
			stack.push(this);
		}
		
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
