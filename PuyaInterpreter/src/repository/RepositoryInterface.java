package repository;

import model.ProgramState;

public interface RepositoryInterface {
	public ProgramState getCurrentProgramState() throws Exception;
	public void addProgramState(ProgramState newProgramState);
	public void logProgramExecution() throws Exception;
}
