package model;

import java.io.BufferedReader;

import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.value.StringValue;
import model.value.ValueInterface;

public class ProgramState {
	private StackInterface<StatementInterface> executionStack;
	private DictionaryInterface<String, ValueInterface> symbolTable;
	private ListInterface<ValueInterface> output;
	private DictionaryInterface<StringValue, BufferedReader> fileTable;
	private StatementInterface originalProgram;
	
	public ProgramState(
			StackInterface<StatementInterface> stack, 
			DictionaryInterface<String, ValueInterface> symbolTable, 
			ListInterface<ValueInterface> output,
			DictionaryInterface<StringValue, BufferedReader> fileTable,
			StatementInterface program
			) {
		this.executionStack = stack;
		this.symbolTable = symbolTable;
		this.output = output;
		this.fileTable = fileTable;
		//this.originalProgram = program.deepCopy();
		if (program != null) { // we don't add null values to the exeStack because the Deque doesn't accept it
			this.executionStack.push(program);
		}
	}
	
	public StackInterface<StatementInterface> getExecutionStack(){
		return this.executionStack;
	}
	
	public DictionaryInterface<String, ValueInterface> getSymbolTable(){
		return this.symbolTable;
	}
	
	public ListInterface<ValueInterface> getOutput(){
		return this.output;
	}
	
	public DictionaryInterface<StringValue, BufferedReader> getFileTable(){
		return this.fileTable;
	}
	
	public StatementInterface getOriginalProgram() {
		return this.originalProgram;
	}
	
	public String toString() {
		String representation = "";
		
		representation += "ExecutionStack:\n";
		representation += this.executionStack.toString();
		representation += "SymbolTable:\n";
		representation += this.symbolTable.toString();
		representation += "FileTable:\n";
		representation += this.fileTable.toString();
		representation += "OutputTable:\n";
		representation += this.output.toString();
		
		return representation;
	}
}
