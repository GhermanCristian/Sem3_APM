package model;

import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.value.ValueInterface;

public class ProgramState {
	private StackInterface<StatementInterface> executionStack;
	private DictionaryInterface<String, ValueInterface> symbolTable;
	private ListInterface<ValueInterface> output;
	private StatementInterface originalProgram;
	
	public ProgramState(
			StackInterface<StatementInterface> stack, 
			DictionaryInterface<String, ValueInterface> symbolTable, 
			ListInterface<ValueInterface> output, 
			StatementInterface program
			) {
		this.executionStack = stack;
		this.symbolTable = symbolTable;
		this.output = output;
		//this.originalProgram = program.deepCopy();
		this.executionStack.push(program);
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
	
	public StatementInterface getOriginalProgram() {
		return this.originalProgram;
	}
	
	public String toString() {
		String representation = "";
		return representation;
	}
}
