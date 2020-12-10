package view;

import controller.GUIController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
	private GUIController controller;
	
	/*void loadExamples() // gets all the text examples from an example srcfile, adds them to a list, we add each one to the controller*/
	
	@Override
	public void start(Stage primaryStage) throws Exception { // this is basically the constructor
		this.controller = new GUIController(this);
		
		VBox mainLayout = new VBox();
		Label l1 = new Label("salut");
		Button b1 = new Button("some text");
		mainLayout.getChildren().addAll(l1, b1);
		
		primaryStage.setTitle("My App");
        primaryStage.setScene(new Scene(mainLayout));
        
        primaryStage.show();
	}

}
