package controller;

import java.util.List;

import model.ProgramState;

public interface ControllerInterface {
	public void fullProgramExecution() throws Exception;
	public void oneStepExecutionAllThreads(List<ProgramState> threadList) throws Exception;
	public void addProgramState(ProgramState newProgramState);
	public List<ProgramState> removeCompletedThreads(List<ProgramState> initialList);
}
