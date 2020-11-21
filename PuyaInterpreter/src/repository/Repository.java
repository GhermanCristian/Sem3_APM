package repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import model.ProgramState;

public class Repository implements RepositoryInterface{
	private List<ProgramState> threadList;
	private final String logFilePath;
	
	public Repository(String logFilePath) {
		this.threadList = new ArrayList<ProgramState>();
		this.logFilePath = logFilePath;
	}

	@Override
	public void addProgramState(ProgramState newProgramState) {
		this.threadList.add(newProgramState);
	}

	@Override
	public void logCompleteThreadListExecution(boolean beforeStep) throws Exception {
		PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
		if (beforeStep == true) {
			logFile.append("\n======== New step ========\n");
		}
		this.threadList.forEach(thread -> logFile.append(thread.toString()));
		logFile.close();
	}

	@Override
	public List<ProgramState> getThreadList() {
		return this.threadList;
	}

	@Override
	public void setThreadList(List<ProgramState> newList) {
		this.threadList.clear();
		this.threadList.addAll(newList);
	}
}
