package view;

public class ExitCommand extends Command {
	public ExitCommand(String key, String description) {
		super(key, description);
	}
	
	@Override
	public void execute() throws Exception {
		System.out.println("Program execution has ended");
		System.exit(0);
	}
}
