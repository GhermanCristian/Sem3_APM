package repository;

import java.util.ArrayDeque;
import model.ProgramState;

public class Repository implements RepositoryInterface{
	private ArrayDeque<ProgramState> programStateQueue;
	
	public Repository() {
		this.programStateQueue = new ArrayDeque<ProgramState>();
	}
	
	@Override
	public ProgramState getCurrentProgramState() throws Exception{
		return this.programStateQueue.pop();
	}

	@Override
	public void addProgramState(ProgramState newProgramState) {
		this.programStateQueue.add(newProgramState);
	}
}
