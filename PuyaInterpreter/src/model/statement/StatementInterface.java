package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.TypeInterface;

public interface StatementInterface {
	public ProgramState execute(ProgramState crtState) throws Exception;
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception;
	public String toString();
}
