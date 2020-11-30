package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.expression.ExpressionInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class PrintStatement implements StatementInterface {
	private final ExpressionInterface expressionToPrint;

	public PrintStatement(ExpressionInterface expressionToPrint) {
		this.expressionToPrint = expressionToPrint;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception{
		ListInterface<ValueInterface> output = crtState.getOutput();
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		
		output.addLast(this.expressionToPrint.evaluate(symbolTable, heap));
		
		return null;
	}
	
	// only displays print(var) (we're printing the print statement)
	public String toString() {
		String representation = "";
		representation += ("print (" + this.expressionToPrint.toString() + ");\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		this.expressionToPrint.typeCheck(initialTypeEnvironment); // throws an exception if the expression's type is invalid
		return initialTypeEnvironment; // the type environment is not modified
	}
}
