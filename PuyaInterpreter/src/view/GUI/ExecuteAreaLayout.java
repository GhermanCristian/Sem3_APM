package view.GUI;

import controller.GUIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

class ExecuteAreaLayout {
	private GUIController controller;
	private Button advanceOneStepButton;
	private Button fullProgramExecutionButton;
	
	public ExecuteAreaLayout(GUIController controller) {
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
		this.advanceOneStepButton.setDisable(false);
		this.fullProgramExecutionButton.setDisable(false);
	}
	
	public void afterProgramExecution() {
		this.advanceOneStepButton.setDisable(true);
		this.fullProgramExecutionButton.setDisable(true);
	}
	
	public void updateAllStructures(int threadCount) {
		
	}
	
	private void initialiseAdvanceOneStepButton() {
		this.advanceOneStepButton = new Button("One step");
		this.advanceOneStepButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent event) {
            	try {
					controller.advanceOneStepAllThreads();
				} 
            	catch (Exception e) {
					displayErrorMessage(e.getMessage());
				}
            }
		});
		this.advanceOneStepButton.setDisable(true); // these buttons will be disabled until an example is selected
		this.advanceOneStepButton.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void intialiseFullProgramExecutionButton() {
		this.fullProgramExecutionButton = new Button("Full execution");
		this.fullProgramExecutionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent event) {
            	try {
					controller.fullProgramExecution();
				} 
            	catch (Exception e) {
					displayErrorMessage(e.getMessage());
				}
            }
		});
		this.fullProgramExecutionButton.setDisable(true);
		this.fullProgramExecutionButton.setMaxWidth(Double.MAX_VALUE);
	}
	
	public HBox createExecuteAreaLayout() {
		HBox executeAreaLayout = new HBox(5);
		
		this.initialiseAdvanceOneStepButton();
		this.intialiseFullProgramExecutionButton();
		
		HBox.setHgrow(this.advanceOneStepButton, Priority.ALWAYS);
		HBox.setHgrow(this.fullProgramExecutionButton, Priority.ALWAYS);
		HBox.setHgrow(executeAreaLayout, Priority.ALWAYS);
		
		executeAreaLayout.getChildren().addAll(this.advanceOneStepButton, this.fullProgramExecutionButton);
		executeAreaLayout.setId("executeAreaLayout");
		
		return executeAreaLayout;
	}
}
