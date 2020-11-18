package controller;

import model.ProgramState;

public interface ControllerInterface {
	public ProgramState oneStepExecution(ProgramState crtProgramState) throws Exception;
	public ProgramState fullProgramExecution() throws Exception;
	public void addProgramState(ProgramState newProgramState);
}
