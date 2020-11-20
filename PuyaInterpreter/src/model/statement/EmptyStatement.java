package model.statement;

import model.ProgramState;

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

}
