package view.GUI;

import controller.GUIController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
	private GUIController controller;
	private UpperLayout upperLayout;
	private ExecuteAreaLayout executeAreaLayout;
	private ViewLayout viewLayout;
	
	private final int MINIMUM_MAIN_WINDOW_WIDTH = 700; // in pixels
	private final int MINIMUM_MAIN_WINDOW_HEIGHT = 300; // in pixels

	/*observer -> GUI
		- it should have an "update" method, called as a response to "notify", which updates 
			-> prg state + their count
			-> heap, output, filetable
			-> stack, symbol table (they also have to be updated when chagning the current selected thread)
	subject (observable) -> controller
		- it should have a "notify" method, called after each oneStep() */
	
	public void updateAllStructures() {
		this.viewLayout.updateAllStructures();
	}
	
	public void beforeProgramExecution() {
		this.updateAllStructures();
		this.upperLayout.beforeProgramExecution();
		this.executeAreaLayout.beforeProgramExecution();
	}
	
	public void afterProgramExecution() {
		this.viewLayout.afterProgramExecution();
		this.upperLayout.afterProgramExecution();
		this.executeAreaLayout.afterProgramExecution();
	}
	
	private Scene createMainScene() throws Exception {
		Scene mainScene;
		VBox mainLayout = new VBox(10);
		
		mainScene = new Scene(mainLayout);
		mainScene.getStylesheets().add(getClass().getResource("applicationStyle.css").toExternalForm());
		mainLayout.getChildren().addAll(this.upperLayout.createUpperLayout(), this.executeAreaLayout.createExecuteAreaLayout(), this.viewLayout.createViewLayout());
		
		return mainScene;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception { // this is basically the constructor
		this.controller = new GUIController(this);
		this.upperLayout = new UpperLayout(this.controller);
		this.executeAreaLayout = new ExecuteAreaLayout(this.controller);
		this.viewLayout = new ViewLayout(this.controller);
		
		primaryStage.setMinWidth(this.MINIMUM_MAIN_WINDOW_WIDTH);
		primaryStage.setMinHeight(this.MINIMUM_MAIN_WINDOW_HEIGHT);
		primaryStage.setTitle("PuyaInterpreter");
        primaryStage.setScene(this.createMainScene());
        primaryStage.show();
	}
}
