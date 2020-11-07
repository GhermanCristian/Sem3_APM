package view;

import controller.Controller;

public class RunExampleCommand extends Command{
	private Controller crtController;
	
	public RunExampleCommand(String key, String description, Controller crtController) {
		super(key, description);
		this.crtController = crtController;
	}
	
	@Override
	public void execute() {
		try {
			this.crtController.fullProgramExecution();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
