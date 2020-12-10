package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.value.ReferenceValue;
import model.value.ValueInterface;
import repository.RepositoryInterface;

public class TextController implements ControllerInterface{
	private RepositoryInterface repository;
	private ExecutorService executor;
	
	public TextController(RepositoryInterface repository) {
		this.repository = repository;
	}
	
	private List<Integer> getHeapAddressesFromSymbolTable(DictionaryInterface<String, ValueInterface> symbolTable) {
		return symbolTable.getAllValues()
				.stream()
				.filter(elem -> elem instanceof ReferenceValue)
				.map(elem -> {ReferenceValue elem1 = (ReferenceValue)elem; return elem1.getHeapAddress();})
				.collect(Collectors.toList());
	}
	
	private HashMap<Integer, ValueInterface> getGarbageCollectedHeap(List<ProgramState> threadList) {
		// the heap is the same for all threads, so we just pick one from which to get the heap
		DictionaryInterface<Integer, ValueInterface> heap = threadList.get(0).getHeap();
		
		List<Integer> symbolTableAddresses = new ArrayList<Integer>();
		threadList.forEach(thread -> symbolTableAddresses.addAll(this.getHeapAddressesFromSymbolTable(thread.getSymbolTable())));
		List<Integer> heapReferencedAddresses = heap.getAllValues()
											.stream()
											.filter(elem -> elem instanceof ReferenceValue)
											.map(elem -> {ReferenceValue elem1 = (ReferenceValue)elem; return elem1.getHeapAddress();})
											.collect(Collectors.toList());
		return (HashMap<Integer, ValueInterface>)heap.getAllPairs().entrySet().stream()
											.filter(elem -> symbolTableAddresses.contains(elem.getKey()) || heapReferencedAddresses.contains(elem.getKey()))
											.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public void fullProgramExecution() throws Exception {
		this.executor = Executors.newFixedThreadPool(2);
		List<ProgramState> threadsStillInExecution = this.removeCompletedThreads(this.repository.getThreadList());
		while (threadsStillInExecution.size() > 0) {
			threadsStillInExecution.get(0).getHeap().setContent(this.getGarbageCollectedHeap(threadsStillInExecution));
			this.oneStepExecutionAllThreads(threadsStillInExecution);
			threadsStillInExecution = this.removeCompletedThreads(this.repository.getThreadList());
		}
		
		this.executor.shutdownNow();
		this.repository.setThreadList(threadsStillInExecution); // what is the purpose of this ??????
	}
	
	private void oneStepExecutionAllThreads(List<ProgramState> threadList) throws Exception {
		this.repository.logCompleteThreadListExecution(true);
		List<Callable<ProgramState>> callableList = threadList.stream()
									.map((ProgramState thread) -> (Callable<ProgramState>)(() -> {return thread.oneStepExecution();}))
									.collect(Collectors.toList());
		// threads that have been advanced by 1 step
		List<ProgramState> advancedThreadList = this.executor.invokeAll(callableList).stream()
													.map(future -> {
														try {
															return future.get();
														}
														catch (Exception e) {
															// I have to throw a runtime exception because fuck you functional programming
															throw new RuntimeException(e.getMessage());
														}
													})
													.filter(thread -> thread != null)
													.collect(Collectors.toList());
		threadList.addAll(advancedThreadList);
		this.repository.logCompleteThreadListExecution(false);
		this.repository.setThreadList(threadList);
	}

	@Override
	public void addProgramState(ProgramState newProgramState) {
		this.repository.addProgramState(newProgramState);
	}

	@Override
	public List<ProgramState> removeCompletedThreads(List<ProgramState> initialList) {
		return initialList.stream().filter(thread -> thread.isCompleted() == false).collect(Collectors.toList());
	}
}
