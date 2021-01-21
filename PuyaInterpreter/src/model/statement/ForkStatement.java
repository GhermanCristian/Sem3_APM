package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class ForkStatement implements StatementInterface {
	private final StatementInterface threadStatements;
	
	public ForkStatement(StatementInterface threadInstructions) {
		this.threadStatements = threadInstructions;
	}
	
	@Override
	public ProgramState execute(ProgramState parentThread) throws Exception {
		if (this.threadStatements == null) {
			return null;
		}
		
		StackInterface<StatementInterface> stack = new MyStack<StatementInterface>();
		StackInterface<DictionaryInterface<String, ValueInterface>> symbolTableStack = new MyStack<DictionaryInterface<String,ValueInterface>>();
		parentThread.getSymbolTableStack().forEach(symbolTable -> symbolTableStack.pushBack(symbolTable.clone()));
		
		return new ProgramState(stack, symbolTableStack, 
								parentThread.getOutput(), 
								parentThread.getFileTable(), 
								parentThread.getHeap(), 
								parentThread.getSemaphoreTable(),
								parentThread.getLatchTable(),
								parentThread.getProcedureTable(), this.threadStatements);
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("fork\n(\n" + this.threadStatements.toString() + ")\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		this.threadStatements.getTypeEnvironment(initialTypeEnvironment.clone());
		return initialTypeEnvironment;
	}
}
