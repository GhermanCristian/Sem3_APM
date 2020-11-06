package repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import model.ProgramState;

public class Repository implements RepositoryInterface{
	private ArrayDeque<ProgramState> programStateQueue;
	private final String logFilePath;
	
	public Repository(String logFilePath) {
		this.programStateQueue = new ArrayDeque<ProgramState>();
		this.logFilePath = logFilePath;
	}
	
	@Override
	public ProgramState getCurrentProgramState() throws Exception{
		return this.programStateQueue.peek();
	}

	@Override
	public void addProgramState(ProgramState newProgramState) {
		this.programStateQueue.add(newProgramState);
	}

	@Override
	public void logProgramExecution() throws Exception {
		PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
		for (ProgramState crtProgramState : this.programStateQueue) {
			// I should also probably print sth like the name of the program state (especially when going to threads)
			logFile.append(crtProgramState.toString()); 
		}
		logFile.close();
	}
}
