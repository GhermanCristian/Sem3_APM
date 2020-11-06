package model.statement;

import model.ProgramState;

public interface StatementInterface {
	public ProgramState execute(ProgramState crtState) throws Exception;
	public String toString();
}
