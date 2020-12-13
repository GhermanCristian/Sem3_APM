package controller;

import java.util.List;
import model.ProgramState;
import repository.RepositoryInterface;

public class TextController extends Controller {	
	public TextController(RepositoryInterface repository) {
		this.repository = repository;
	}
	
	@Override
	// the text controller only has the option to fullyExecute a program
	public void fullProgramExecution() throws Exception {
		this.beforeProgramExecution();
		
		List<ProgramState> threadsStillInExecution = this.removeCompletedThreads(this.repository.getThreadList());
		while (threadsStillInExecution.size() > 0) {
			threadsStillInExecution.get(this.FIRST_THREAD_POSITION_IN_THREAD_LIST).getHeap().setContent(this.getGarbageCollectedHeap(threadsStillInExecution));
			this.oneStepExecutionAllThreads(threadsStillInExecution);
			threadsStillInExecution = this.removeCompletedThreads(this.repository.getThreadList());
		}
		
		this.afterProgramExecution();
	}
}
