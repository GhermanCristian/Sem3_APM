package view.GUI;

import java.util.ArrayList;
import controller.GUIController;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import model.ProgramState;

class ViewLayout {
	private GUIController controller;
	
	// I store the actual thread and not the threadID because I won't have to repeatedly call getThreadByID, which can prove costly,
	// especially in the tableViews, where it has to be called for each row
	// on the other hand, the size of a ProgramState is not too large (around 7 bytes)
	private ProgramState selectedThread;
	
	private final int MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH = 120;
	private final int MAXIMUM_THREAD_LIST_VIEW_WIDTH = MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH;
	private final int MAXIMUM_MAIN_STRUCTURES_LAYOUT_HEIGHT = 650;
	private final double COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_2_COLUMN_TABLE_VIEW = 0.5;
	private final double COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_3_COLUMN_TABLE_VIEW = 0.33;
	
	// here I need the actual value of the thread ID because selecting from this list affects the program
	// selecting from the output, filetable etc does not affect the program, so I'll just display the string
	private ListView<Integer> threadListView; 
	private ListView<String> outputListView;
	private ListView<String> fileTableListView;
	private ListView<String> stackListView;
	private TableView<Integer> heapTableView; // here I need it to store Integers because that's the key of the Heap structure
	private TableView<String> symbolTableTableView;
	private TableView<Integer> semaphoreTableTableView; // here I need it to store Integers because that's the key of the semaphore structure
	private TextField programStateCountTextField;
	
	public ViewLayout(GUIController controller) {
		this.controller = controller;
	}
	
	private void updateStackListView() {
		this.stackListView.getItems().clear();
		this.selectedThread.getExecutionStack().forEach(statement -> this.stackListView.getItems().add(statement.toString()));
	}
	
	private void updateSymbolTableTableView() {
		this.symbolTableTableView.getItems().clear();
		this.selectedThread.getSymbolTable().forEachKey(variableName -> this.symbolTableTableView.getItems().add(variableName));
	}
	
	private void updateThreadDependantStructures() {
		if (this.selectedThread == null) {
			return;
		}
		
		this.updateStackListView();
		this.updateSymbolTableTableView();
	}
	
	// the thread list view will change when a new thread is introduced / a thread is completed => taken out of the repo
	private void updateThreadListView() {
		this.threadListView.getItems().clear();
		this.controller.getThreadList().forEach(thread -> this.threadListView.getItems().add(thread.getThreadID()));
	}
	
	private void updateHeapTableView() {
		this.heapTableView.getItems().clear();
		ProgramState firstAvailableThread = this.controller.getFirstAvailableThread();
		if (firstAvailableThread == null) {
			return ;
		}
		
		firstAvailableThread.getHeap().forEachKey(variableAddress -> this.heapTableView.getItems().add(variableAddress));
	}
	
	private void updateSemaphoreTableTableView() {
		this.semaphoreTableTableView.getItems().clear();
		ProgramState firstAvailableThread = this.controller.getFirstAvailableThread();
		if (firstAvailableThread == null) {
			return ;
		}
		
		firstAvailableThread.getSemaphoreTable().forEachKey(variableAddress -> this.semaphoreTableTableView.getItems().add(variableAddress));
	}
	
	private void updateLockMechanismView() {
		this.updateSemaphoreTableTableView();
	}
	
	private void updateOutputListView() {
		this.outputListView.getItems().clear();
		ProgramState firstAvailableThread = this.controller.getFirstAvailableThread();
		if (firstAvailableThread == null) {
			return ;
		}
		
		firstAvailableThread.getOutput().forEach(message -> this.outputListView.getItems().add(message.toString()));
	}
	
	private void updateFileTableListView() {
		this.fileTableListView.getItems().clear();
		ProgramState firstAvailableThread = this.controller.getFirstAvailableThread();
		if (firstAvailableThread == null) {
			return ;
		}
		
		firstAvailableThread.getFileTable().forEachKey(fileName -> this.fileTableListView.getItems().add(fileName.toString()));
	}
	
	// threadList, heap, output, filetable - they don't depend on the current thread
	private void updateGlobalStructures() {
		this.updateThreadListView();
		this.updateHeapTableView();
		this.updateLockMechanismView();
		this.updateOutputListView();
		this.updateFileTableListView();
		
		// update the textfield for the thread count; only after the threadListView is updated in updateGlobalStructures()
		this.programStateCountTextField.setText("Threads: " + Integer.toString(this.threadListView.getItems().size()));
	}
	
	public void updateAllStructures() {
		// normally I wouldn't need to call this from the controller, I could just call it here after each button press for next step
		// however, if there are some internal modifications that are done even when I don't press the button - then I need this
		this.updateGlobalStructures();
		this.updateThreadDependantStructures();
	}
	
	public void afterProgramExecution() {
		this.updateThreadListView();
		this.programStateCountTextField.setText("Threads: 0");
	}
	
	private void initialiseThreadListView() {
		// should the threadList display all the threads (including those which are completed) ?
		this.threadListView = new ListView<Integer>();
		this.threadListView.setMaxWidth(this.MAXIMUM_THREAD_LIST_VIEW_WIDTH);
		this.threadListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				// It's no use to pass the currently selected thread to the controller (and back again to the GUI)
				// because the currentThread doesn't affect the program execution, just the content displayed in the GUI
				
				// also, if I click on the ID that's already clicked, there's no use in updating anything
				if (newValue == oldValue) {
					return;
				}
				
				if (newValue == null || newValue < 0) {
					// by doing this we reduce the possibility of not having a thread selected at a time, which did occur frequently for some reason
					ProgramState firstAvailableThread = controller.getFirstAvailableThread();
					if (firstAvailableThread == null) {
						; // what happens if there are no threads left ?
					}
					newValue = firstAvailableThread.getThreadID();
				}
				
				selectedThread = controller.getThreadByID(newValue);
				updateThreadDependantStructures();
			}
		});
	}
	
	private void initialiseSymbolTableTableView() {
		this.symbolTableTableView = new TableView<String>();
		this.symbolTableTableView.setEditable(false);
		
		this.symbolTableTableView.setMaxWidth(Double.MAX_VALUE);
		
		TableColumn<String, String> variableNameColumn = new TableColumn<String, String>("Variable name");
		variableNameColumn.prefWidthProperty().bind(this.symbolTableTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_2_COLUMN_TABLE_VIEW));
		// this approach (with the readOnlyStringWrapper) should only be used as long as the table is non-editable (which it is in this app)
		variableNameColumn.setCellValueFactory(currentValue -> new ReadOnlyStringWrapper(currentValue.getValue()));
		
		TableColumn<String, String> variableValueColumn = new TableColumn<String, String>("Value");
		variableValueColumn.prefWidthProperty().bind(this.symbolTableTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_2_COLUMN_TABLE_VIEW));
		variableValueColumn.setCellValueFactory(currentValue -> {
			if (this.selectedThread == null) {
				return null;
			} 
			return new ReadOnlyStringWrapper(this.selectedThread.getSymbolTable().getValue(currentValue.getValue()).toString());
		});
		
		this.symbolTableTableView.getColumns().add(variableNameColumn);
		this.symbolTableTableView.getColumns().add(variableValueColumn);
	}
	
	private void initialiseOutputListView() {
		this.outputListView = new ListView<String>();
		this.outputListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseHeapTableTableView() {
		this.heapTableView = new TableView<Integer>();
		this.heapTableView.setEditable(false);
		
		TableColumn<Integer, String> variableAddressColumn = new TableColumn<Integer, String>("Variable address");
		variableAddressColumn.prefWidthProperty().bind(this.heapTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_2_COLUMN_TABLE_VIEW));
		// this approach should only be used as long as the table is non-editable (which it is in this app)
		variableAddressColumn.setCellValueFactory(currentReference -> new ReadOnlyStringWrapper("0x" + Integer.toHexString(currentReference.getValue())));
		
		TableColumn<Integer, String> variableValueColumn = new TableColumn<Integer, String>("Value");
		variableValueColumn.prefWidthProperty().bind(this.heapTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_2_COLUMN_TABLE_VIEW));
		variableValueColumn.setCellValueFactory(currentReference -> {
			if (this.selectedThread == null) {
				return null;
			} 
			return new ReadOnlyStringWrapper(this.selectedThread.getHeap().getValue(currentReference.getValue()).toString());
		});
		
		this.heapTableView.getColumns().add(variableAddressColumn);
		this.heapTableView.getColumns().add(variableValueColumn);
		this.heapTableView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseSemaphoreTableTableView() {
		this.semaphoreTableTableView = new TableView<Integer>();
		this.semaphoreTableTableView.setEditable(false);
		
		TableColumn<Integer, String> semaphoreAddressColumn = new TableColumn<Integer, String>("Semaphore address");
		semaphoreAddressColumn.prefWidthProperty().bind(this.semaphoreTableTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_3_COLUMN_TABLE_VIEW));
		// this approach should only be used as long as the table is non-editable (which it is in this app)
		semaphoreAddressColumn.setCellValueFactory(currentSemaphoreKey -> new ReadOnlyStringWrapper(currentSemaphoreKey.getValue().toString()));
		
		TableColumn<Integer, String> semaphoreCapacityColumn = new TableColumn<Integer, String>("Capacity");
		semaphoreCapacityColumn.prefWidthProperty().bind(this.semaphoreTableTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_3_COLUMN_TABLE_VIEW));
		semaphoreCapacityColumn.setCellValueFactory(currentSemaphoreKey -> {
			if (this.selectedThread == null) {
				return null;
			}
			
			Pair<Integer, ArrayList<Integer>> currentSemaphoreValue = this.selectedThread.getSemaphoreTable().getValue(currentSemaphoreKey.getValue());
			return new ReadOnlyStringWrapper(currentSemaphoreValue.getKey().toString());
		});
		
		TableColumn<Integer, String> semaphoreThreadListColumn = new TableColumn<Integer, String>("ThreadList");
		semaphoreThreadListColumn.prefWidthProperty().bind(this.semaphoreTableTableView.widthProperty().multiply(this.COLUMN_WIDTH_AS_PERCENTAGE_OF_TOTAL_TABLE_WIDTH_3_COLUMN_TABLE_VIEW));
		semaphoreThreadListColumn.setCellValueFactory(currentSemaphoreKey -> {
			if (this.selectedThread == null) {
				return null;
			}
			
			Pair<Integer, ArrayList<Integer>> currentSemaphoreValue = this.selectedThread.getSemaphoreTable().getValue(currentSemaphoreKey.getValue());
			return new ReadOnlyStringWrapper(currentSemaphoreValue.getValue().toString());
		});
		
		this.semaphoreTableTableView.getColumns().add(semaphoreAddressColumn);
		this.semaphoreTableTableView.getColumns().add(semaphoreCapacityColumn);
		this.semaphoreTableTableView.getColumns().add(semaphoreThreadListColumn);
		this.semaphoreTableTableView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseLockMechanismView() {
		this.initialiseSemaphoreTableTableView();
	}
	
	private void initialiseFileTableListView() {
		this.fileTableListView = new ListView<String>();
		this.fileTableListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseStackListView() {
		this.stackListView = new ListView<String>();
		this.stackListView.setMaxWidth(Double.MAX_VALUE);
	}
	
	private void initialiseThreadCountTextField() {
		this.programStateCountTextField = new TextField("Threads: 0");
		this.programStateCountTextField.setEditable(false);
		this.programStateCountTextField.setMaxWidth(this.MAXIMUM_PROGRAM_STATE_COUNT_FIELD_WIDTH);
		this.programStateCountTextField.setId("threadCountTextField");
	}
	
	private void initialiseAllStructures() {
		this.initialiseThreadListView();
		this.initialiseSymbolTableTableView();
		this.initialiseOutputListView();
		this.initialiseHeapTableTableView();
		this.initialiseLockMechanismView();
		this.initialiseFileTableListView();
		this.initialiseStackListView();
		this.initialiseThreadCountTextField();
	}
	
	public HBox createViewLayout() {
		HBox mainStructuresLayout = new HBox(5);
		VBox leftLayout = new VBox(5);
		VBox rightLayout = new VBox(5);
		HBox upperRightLayout = new HBox(5);
		HBox lowerRightLayout = new HBox(5);
		
		this.initialiseAllStructures();
		
		// for now I don't know whether I should move these setHgrows to their corresponding item's initialise method, 
		// in case I might want to change from a HBox to sth else
		VBox.setVgrow(this.threadListView, Priority.ALWAYS);
		HBox.setHgrow(this.symbolTableTableView, Priority.ALWAYS);
		HBox.setHgrow(this.heapTableView, Priority.ALWAYS);
		HBox.setHgrow(this.semaphoreTableTableView, Priority.ALWAYS);
		HBox.setHgrow(this.outputListView, Priority.ALWAYS);
		HBox.setHgrow(this.stackListView, Priority.ALWAYS);
		HBox.setHgrow(this.fileTableListView, Priority.ALWAYS);
		VBox.setVgrow(leftLayout, Priority.ALWAYS);
		HBox.setHgrow(upperRightLayout, Priority.ALWAYS);
		HBox.setHgrow(lowerRightLayout, Priority.ALWAYS);
		HBox.setHgrow(rightLayout, Priority.ALWAYS);
		
		leftLayout.getChildren().addAll(this.programStateCountTextField, this.threadListView);
		upperRightLayout.getChildren().addAll(this.symbolTableTableView, this.heapTableView, this.semaphoreTableTableView);
		lowerRightLayout.getChildren().addAll(this.stackListView, this.fileTableListView, this.outputListView);
		rightLayout.getChildren().addAll(upperRightLayout, lowerRightLayout);
		mainStructuresLayout.getChildren().addAll(leftLayout, rightLayout);
		mainStructuresLayout.setMaxHeight(this.MAXIMUM_MAIN_STRUCTURES_LAYOUT_HEIGHT);
		mainStructuresLayout.setId("mainStructuresLayout");
		return mainStructuresLayout;
	}
}
