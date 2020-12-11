package view;

import controller.GUIController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Example;

public class GUI extends Application {
	private GUIController controller;
	// here I need the actual value of the thread ID because selecting from this list affects the program
	// selecting from the output, filetable etc does not affect the program, so I'll just display the string
	private ListView<Integer> threadListView; 
	private ListView<String> outputListView;
	private ListView<String> fileTableListView; // I might change this to a table view, because the file table is a dictionary after all
	private ListView<String> stackListView;
	private TableView<String> heapTableView;
	private TableView<String> symbolTableTableView;
	private TextField programStateCountTextField;
	
	private final int MINIMUM_MAIN_WINDOW_WIDTH = 700; // in pixels
	private final int MINIMUM_MAIN_WINDOW_HEIGHT = 300; // in pixels
	private final int MAXIMUM_MAIN_WINDOW_HEIGHT = 800; // in pixels
	private final int UPPER_LAYOUT_GAP = 10;
	private final int MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH = 80;
	private final int MAXIMUM_EXAMPLE_LIST_COMBO_BOX_WIDTH = 560;
	private final int MAXIMUM_THREAD_LIST_VIEW_WIDTH = MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH;
	
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
	
	private void updateStackListView(Integer newSelectedThreadID) {
		;
	}
	
	private void updateSymbolTableListView(Integer newSelectedThreadID) {
		;
	}
	
	private void updateThreadDependantStructures(Integer newSelectedThreadID) {
		this.updateStackListView(newSelectedThreadID);
		this.updateSymbolTableListView(newSelectedThreadID);
	}
	
	public void updateAllStructures() {
		// normally I wouldn't need to call this from the controller, I could just call it here after each button press for next step
		// however, what if there are some internal modifications that are done even when I don't press the button - then I need this
		
		// update the thread list view
		this.threadListView.getItems().clear();
		this.controller.getThreadList().forEach(thread -> this.threadListView.getItems().add(thread.getThreadID()));
		
		// update the textfield for the thread count
		this.programStateCountTextField.setText("Threads: " + Integer.toString(this.threadListView.getItems().size()));
		
		// what if the selected index is null ?
		this.updateThreadDependantStructures(this.threadListView.getSelectionModel().getSelectedIndex());
	}
	
	private void initialiseThreadListView() {
		this.threadListView = new ListView<Integer>();
		this.threadListView.setMaxWidth(this.MAXIMUM_THREAD_LIST_VIEW_WIDTH);
		this.threadListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				if (newValue != null) {
					// It's no use to pass the currently selected thread to the controller (and back again to the GUI)
					// because it doesn't affect the program execution, just the content displayed in the GUI
					updateThreadDependantStructures(newValue);
				}
			}
		});
		this.threadListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseSymbolTableTableView() {
		this.symbolTableTableView = new TableView<String>();
		this.symbolTableTableView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseOutputListView() {
		this.outputListView = new ListView<String>();
		this.outputListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseHeapTableTableView() {
		this.heapTableView = new TableView<String>();
		this.heapTableView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseFileTableListView() {
		this.fileTableListView = new ListView<String>();
		this.fileTableListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseStackListView() {
		this.stackListView = new ListView<String>();
		this.stackListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private HBox createStructuresLayout() {
		HBox mainStructuresLayout = new HBox(5);
		VBox rightLayout = new VBox(5);
		HBox upperRightLayout = new HBox(5);
		HBox lowerRightLayout = new HBox(5);
		
		this.initialiseThreadListView();
		this.initialiseSymbolTableTableView();
		this.initialiseOutputListView();
		this.initialiseHeapTableTableView();
		this.initialiseFileTableListView();
		this.initialiseStackListView();
		
		/*GridPane.setConstraints(this.threadListView, 0, 0, 1, 2, HPos.LEFT, VPos.CENTER);
		GridPane.setConstraints(this.symbolTableTableView, 1, 0, 2, 1, HPos.LEFT, VPos.TOP);
		GridPane.setConstraints(this.outputListView, 3, 0, 2, 1, HPos.CENTER, VPos.TOP);
		GridPane.setConstraints(this.heapTableView, 5, 0, 2, 1, HPos.RIGHT, VPos.TOP);
		GridPane.setConstraints(this.fileTableListView, 1, 1, 3, 1, HPos.LEFT, VPos.BOTTOM);
		GridPane.setConstraints(this.stackListView, 4, 1, 3, 1, HPos.RIGHT, VPos.BOTTOM);
		
		structuresLayout.getChildren().addAll(this.threadListView, this.symbolTableTableView, this.outputListView, this.heapTableView, this.fileTableListView, this.stackListView);*/
		HBox.setHgrow(this.symbolTableTableView, Priority.ALWAYS);
		HBox.setHgrow(this.heapTableView, Priority.ALWAYS);
		HBox.setHgrow(this.outputListView, Priority.ALWAYS);
		HBox.setHgrow(this.stackListView, Priority.ALWAYS);
		HBox.setHgrow(this.fileTableListView, Priority.ALWAYS);
		
		upperRightLayout.getChildren().addAll(this.symbolTableTableView, this.heapTableView, this.outputListView);
		lowerRightLayout.getChildren().addAll(this.stackListView, this.fileTableListView);
		rightLayout.getChildren().addAll(upperRightLayout, lowerRightLayout);
		mainStructuresLayout.getChildren().addAll(this.threadListView, rightLayout);
		return mainStructuresLayout;
	}
	
	private Scene createScene() throws Exception {
		Scene newScene;
		VBox mainLayout = new VBox();
		HBox upperLayout = new HBox(this.UPPER_LAYOUT_GAP);
		
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
		mainLayout.getChildren().addAll(upperLayout, programStateCountTextField, this.createStructuresLayout());
		newScene = new Scene(mainLayout);
		newScene.getStylesheets().add(getClass().getResource("applicationStyle.css").toExternalForm());
		return newScene;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception { // this is basically the constructor
		this.controller = new GUIController(this);
		this.programStateCountTextField = new TextField("Threads: 0");
		
		primaryStage.setMinWidth(this.MINIMUM_MAIN_WINDOW_WIDTH);
		primaryStage.setMinHeight(this.MINIMUM_MAIN_WINDOW_HEIGHT);
		//primaryStage.setMaxHeight(this.MAXIMUM_MAIN_WINDOW_HEIGHT);
		primaryStage.setTitle("PuyaInterpreter");
        primaryStage.setScene(this.createScene());
        primaryStage.show();
	}

}