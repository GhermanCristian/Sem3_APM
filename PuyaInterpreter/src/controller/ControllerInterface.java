package controller;

import model.ProgramState;

public interface ControllerInterface {
	ProgramState oneStepExecution(ProgramState crtProgramState) throws Exception;
	void fullProgramExecution() throws Exception;
}