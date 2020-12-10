package view;

import controller.GUIController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Example;
import model.ProgramState;

public class GUI extends Application {
	private GUIController controller;
	private ListView<ProgramState> threadListView;
	private TextField programStateCountTextField;
	private final int MINIMUM_MAIN_WINDOW_WIDTH = 700; // in pixels
	private final int MINIMUM_MAIN_WINDOW_HEIGHT = 300; // in pixels
	private final int UPPER_LAYOUT_GAP = 10;
	private final int MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH = 80;
	private final int MAXIMUM_EXAMPLE_LIST_COMBO_BOX_WIDTH = 560;
	
	/*
	observer -> GUI
		- it should have an "update" method, called as a response to "notify", which updates 
			-> prg state + their count
			-> heap, output, filetable
			-> stack, symbol table (they also have to be updated when chagning the current selected thread)
	subject (observable) -> controller
		- it should have a "notify" method, called after each oneStep() 
	*/
	
	private void displayErrorMessage(String message) {
		;
	}
	
	public void updateStructures() {
		// normally I wouldn't need to call this from the controller, I could just call it here after each button press for next step
		// however, what if there are some internal modifications that are done even when I don't press the button - then I need this
		this.threadListView.getItems().clear();
		this.controller.getThreadList().forEach(thread -> this.threadListView.getItems().add(thread));
		this.programStateCountTextField.setText("Threads: " + Integer.toString(this.threadListView.getItems().size()));
	}
	
	private Scene createScene() throws Exception {
		Scene newScene;
		VBox mainLayout = new VBox();
		HBox upperLayout = new HBox(this.UPPER_LAYOUT_GAP);
		
		this.threadListView.setMaxWidth(MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH);
		
		ComboBox<Example> exampleComboBox = new ComboBox<Example>();
		exampleComboBox.setVisibleRowCount(2);
		exampleComboBox.setMaxWidth(this.MAXIMUM_EXAMPLE_LIST_COMBO_BOX_WIDTH);
		this.controller.getAllExamples().forEach(example -> exampleComboBox.getItems().add(example));
		Button selectExampleButton = new Button("Execute program");
		selectExampleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		controller.loadProgramStateIntoRepository(exampleComboBox.getValue());
				} 
            	catch (Exception e) {
					displayErrorMessage(e.getMessage());
				}
            	
            	selectExampleButton.setDisable(true);
            	exampleComboBox.hide();
            	exampleComboBox.setPromptText("Program changing unavailable: a program is currently in execution"); // do this with a more visible font
            	exampleComboBox.show();
            	exampleComboBox.setDisable(true);
            	// for now I will try it like this, so that I can't accidentally click it and overwrite the current program
            	// I can probably reactivate it when the program execution is finished
            }
		});
		selectExampleButton.setTooltip(new Tooltip("Only one program can be run at a time"));
		
		this.programStateCountTextField.setEditable(false);
		this.programStateCountTextField.setMaxWidth(this.MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH);
		
		//exampleComboBox.getStyleClass().add("comboBox");
		upperLayout.getChildren().addAll(exampleComboBox, selectExampleButton);
		mainLayout.getChildren().addAll(upperLayout, programStateCountTextField, this.threadListView);
		newScene = new Scene(mainLayout);
		newScene.getStylesheets().add(getClass().getResource("applicationStyle.css").toExternalForm());
		return newScene;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception { // this is basically the constructor
		this.controller = new GUIController(this);
		this.threadListView = new ListView<ProgramState>();
		this.programStateCountTextField = new TextField("Threads: 0");
		
		primaryStage.setMinWidth(this.MINIMUM_MAIN_WINDOW_WIDTH);
		primaryStage.setMinHeight(this.MINIMUM_MAIN_WINDOW_HEIGHT);
		primaryStage.setTitle("PuyaInterpreter");
        primaryStage.setScene(this.createScene());
        primaryStage.show();
	}

}
