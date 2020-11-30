package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.TypeInterface;

public class EmptyStatement implements StatementInterface{

	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		return null;
	}
	
	public String toString() {
		String representation = "";
		representation += "empty_statement\n";
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		return initialTypeEnvironment;
	}

}
