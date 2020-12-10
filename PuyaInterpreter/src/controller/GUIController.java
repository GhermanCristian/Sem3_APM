package controller;

import java.util.List;
import java.util.stream.Collectors;

import model.ProgramState;
import view.GUI;

public class GUIController implements ControllerInterface {
	private GUI currentGUI;
	
	public GUIController(GUI currentGUI) {
		this.currentGUI = currentGUI;
	}
	
	@Override
	public void fullProgramExecution() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addProgramState(ProgramState newProgramState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ProgramState> removeCompletedThreads(List<ProgramState> initialList) {
		return initialList.stream().filter(thread -> thread.isCompleted() == false).collect(Collectors.toList());
	}

}
