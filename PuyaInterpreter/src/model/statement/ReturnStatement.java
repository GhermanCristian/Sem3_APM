package model.statement;

import exception.EmptyADTException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class ReturnStatement implements StatementInterface {
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<DictionaryInterface<String, ValueInterface>> symbolTable = crtState.getSymbolTableStack();
		if (symbolTable.size() == 0) {
			throw new EmptyADTException("ReturnStatement: there is no procedure to return from");
		}
		symbolTable.pop();
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("return;\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		return initialTypeEnvironment;
	}
}
