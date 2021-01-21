package view.GUI;

import controller.GUIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.Example;

class UpperLayout {
	private GUIController controller;
	private Button selectExampleButton;
	private ComboBox<Example> exampleComboBox;
	
	private final int UPPER_LAYOUT_GAP = 10;
	private final int MINIMUM_SELECT_EXAMPlE_BUTTON_WIDTH = 150;
	
	public UpperLayout(GUIController controller) {
		this.controller = controller;
	}
	
	private void displayErrorMessage(String message) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setTitle("Error");
		errorAlert.setContentText(message);
		errorAlert.setResizable(true);
		errorAlert.showAndWait();
	}
	
	public void beforeProgramExecution() {
		this.selectExampleButton.setDisable(true); // deactivate the select example button and the example combo box
		this.exampleComboBox.setDisable(true);
	}
	
	public void afterProgramExecution() {
		this.selectExampleButton.setDisable(false); // re-activate / reset the select example button and the example combo box
		this.exampleComboBox.setDisable(false);
		this.exampleComboBox.getSelectionModel().clearSelection();
	}
	
	private void initialiseExampleComboBox() {
		this.exampleComboBox = new ComboBox<Example>();
		this.exampleComboBox.setVisibleRowCount(2);
		this.exampleComboBox.setMaxWidth(Double.MAX_VALUE);
		this.exampleComboBox.setMaxHeight(100);
		this.controller.getAllExamples().forEach(example -> exampleComboBox.getItems().add(example));
	}
	
	private void initialiseSelectExampleButton() {
		this.selectExampleButton = new Button("Execute program");
		this.selectExampleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	try {
            		controller.loadProgramStateIntoRepository(exampleComboBox.getValue());
				} 
            	catch (Exception e) {
					displayErrorMessage(e.getMessage());
					return;
				}

            	// I don't think this works ?
            	exampleComboBox.setPromptText("Program changing unavailable: a program is currently in execution");
            }
		});
		this.selectExampleButton.setTooltip(new Tooltip("Only one program can be run at a time"));
		this.selectExampleButton.setMaxWidth(Double.MAX_VALUE);
		this.selectExampleButton.setMinWidth(this.MINIMUM_SELECT_EXAMPlE_BUTTON_WIDTH);
	}
	
	public HBox createUpperLayout() {
		HBox upperLayout = new HBox(this.UPPER_LAYOUT_GAP);
		
		this.initialiseExampleComboBox();
		this.initialiseSelectExampleButton();
		
		HBox.setHgrow(this.exampleComboBox, Priority.ALWAYS);
		HBox.setHgrow(this.selectExampleButton, Priority.ALWAYS);
		HBox.setHgrow(upperLayout, Priority.ALWAYS);
		
		upperLayout.getChildren().addAll(this.exampleComboBox, this.selectExampleButton);
		upperLayout.setId("upperLayout"); // css stuff
		
		return upperLayout;
	}
}
