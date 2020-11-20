package repository;

import java.util.List;
import model.ProgramState;

public interface RepositoryInterface {
	public void addProgramState(ProgramState newProgramState);
	public List<ProgramState> getThreadList();
	public void setThreadList(List<ProgramState> newList);
	public void logProgramExecution(ProgramState crtProgramState) throws Exception;
}
