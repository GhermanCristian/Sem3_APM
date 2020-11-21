package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyDictionary;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.value.ValueInterface;

public class ForkStatement implements StatementInterface {
	private final StatementInterface threadInstructions;
	
	public ForkStatement(StatementInterface threadInstructions) {
		this.threadInstructions = threadInstructions;
	}
	
	@Override
	public ProgramState execute(ProgramState parentThread) throws Exception {
		if (this.threadInstructions == null) {
			return null;
		}
		
		StackInterface<StatementInterface> stack = new MyStack<StatementInterface>();
		DictionaryInterface<String, ValueInterface> symbolTable = new MyDictionary<String, ValueInterface>();
		symbolTable.clone(parentThread.getSymbolTable().getAllPairs());
		return new ProgramState(stack, symbolTable, parentThread.getOutput(), parentThread.getFileTable(), parentThread.getHeap(), this.threadInstructions);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("fork\n(\n" + this.threadInstructions.toString() + ")\n");
		return representation;
	}
}
