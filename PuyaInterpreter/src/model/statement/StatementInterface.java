package model.statement;

import model.ProgramState;

public interface StatementInterface {
	ProgramState execute(ProgramState crtState);
}
