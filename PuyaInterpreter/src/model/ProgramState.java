package model;

import java.io.BufferedReader;
import java.util.ArrayList;
import exception.EmptyADTException;
import javafx.util.Pair;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.value.StringValue;
import model.value.ValueInterface;

public class ProgramState {
	private StackInterface<StatementInterface> executionStack;
	private StackInterface<DictionaryInterface<String, ValueInterface>> symbolTable;
	private ListInterface<ValueInterface> output;
	private DictionaryInterface<StringValue, BufferedReader> fileTable;
	private DictionaryInterface<Integer, ValueInterface> heap;
	private DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable;
	private DictionaryInterface<Integer, Integer> latchTable;
	private DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable;
	private DictionaryInterface<Integer, Integer> lockTable;
	private DictionaryInterface<String, Procedure> procedureTable;
	private StatementInterface originalProgram;
	private static int globalThreadCount = 1;
	private final int threadID;
	
	public ProgramState(
			StackInterface<StatementInterface> stack, 
			StackInterface<DictionaryInterface<String, ValueInterface>> symbolTable, 
			ListInterface<ValueInterface> output,
			DictionaryInterface<StringValue, BufferedReader> fileTable,
			DictionaryInterface<Integer, ValueInterface> heap,
			DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> semaphoreTable,
			DictionaryInterface<Integer, Integer> latchTable,
			DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> barrierTable,
			DictionaryInterface<Integer, Integer> lockTable,
			DictionaryInterface<String, Procedure> procedureTable,
			StatementInterface program
			) {
		this.executionStack = stack;
		this.symbolTable = symbolTable;
		this.output = output;
		this.fileTable = fileTable;
		this.heap = heap;
		this.semaphoreTable = semaphoreTable;
		this.latchTable = latchTable;
		this.barrierTable = barrierTable;
		this.lockTable = lockTable;
		this.procedureTable = procedureTable;
		//this.originalProgram = program.deepCopy();
		this.setStatement(program);
		this.threadID = ProgramState.manageThreadID();
	}
	
	public static synchronized int manageThreadID() {
		int newThreadID = ProgramState.globalThreadCount;
		ProgramState.globalThreadCount += 1;
		return newThreadID;
	}
	
	public int getThreadID() {
		return this.threadID;
	}
	
	public StackInterface<StatementInterface> getExecutionStack() {
		return this.executionStack;
	}
	
	public StackInterface<DictionaryInterface<String, ValueInterface>> getSymbolTableStack() {
		return this.symbolTable;
	}
	
	public DictionaryInterface<String, ValueInterface> getSymbolTable() {
		return this.symbolTable.top();
	}
	
	public ListInterface<ValueInterface> getOutput() {
		return this.output;
	}
	
	public DictionaryInterface<StringValue, BufferedReader> getFileTable() {
		return this.fileTable;
	}
	
	public DictionaryInterface<Integer, ValueInterface> getHeap() {
		return this.heap;
	}
	
	public DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> getSemaphoreTable() {
		return this.semaphoreTable;
	}
	
	public DictionaryInterface<Integer, Integer> getLatchTable() {
		return this.latchTable;
	}
	
	public DictionaryInterface<Integer, Pair<Integer, ArrayList<Integer>>> getBarrierTable() {
		return this.barrierTable;
	}
	
	public DictionaryInterface<Integer, Integer> getLockTable() {
		return this.lockTable;
	}
	
	public DictionaryInterface<String, Procedure> getProcedureTable() {
		return this.procedureTable;
	}
	
	public StatementInterface getOriginalProgram() {
		return this.originalProgram;
	}
	
	public void setStatement(StatementInterface statement) {
		// we don't add null values to the exeStack because the Deque doesn't accept it
		if (statement != null) {
			this.executionStack.push(statement);
		}
	}
	
	public boolean isCompleted() {
		return (this.executionStack.size() == 0);
	}
	
	public ProgramState oneStepExecution() throws Exception{
		if (this.executionStack.size() == 0) {
			throw new EmptyADTException("ProgramState: No program states available");
		}
		StatementInterface currentStatement = this.executionStack.pop();
		return currentStatement.execute(this);
	}
	
	public void clear() {
		this.executionStack.clear();
		this.symbolTable.forEach(symbolTable -> symbolTable.clear());
		this.symbolTable.clear();
		this.output.clear();
		for (BufferedReader crtBuffer : this.fileTable.getAllValues()) {
			try {
				crtBuffer.close();
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		this.fileTable.clear();
		this.heap.clear();
		this.semaphoreTable.clear();
		this.latchTable.clear();
		this.barrierTable.clear();
		this.lockTable.clear();
		this.procedureTable.clear();
	}
	
	public static synchronized void resetThreadID() {
		ProgramState.globalThreadCount = 1;
	}
	
	public String toString() {
		String representation = "";
		
		representation += "\n======== ThreadID: " + Integer.toString(this.threadID) + "========\n";
		representation += "ExecutionStack:\n";
		representation += this.executionStack.toString();
		representation += "\nSymbolTable:\n";
		representation += this.symbolTable.toString();
		representation += "\nFileTable:\n";
		representation += this.fileTable.toString();
		representation += "\nOutputTable:\n";
		representation += this.output.toString();
		representation += "\nHeap:\n";
		representation += this.heap.toString();
		representation += "\nSemaphoreTable:\n";
		representation += this.semaphoreTable.toString();
		representation += "\nLatchTable:\n";
		representation += this.latchTable.toString();
		representation += "\nBarrierTable:\n";
		representation += this.barrierTable.toString();
		representation += "\nLockTable:\n";
		representation += this.lockTable.toString();
		representation += "\nProcedureTable:\n";
		representation += this.procedureTable.toString();
		
		return representation;
	}
}
