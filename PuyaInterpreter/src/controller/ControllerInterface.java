package controller;

import model.ProgramState;

public interface ControllerInterface {
	ProgramState oneStepExecution(ProgramState crtProgramState) throws Exception;
	ProgramState fullProgramExecution() throws Exception;
	void addProgramState(ProgramState newProgramState);
}
