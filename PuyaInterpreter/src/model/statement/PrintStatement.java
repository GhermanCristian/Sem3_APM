package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.expression.ExpressionInterface;
import model.value.ValueInterface;

public class PrintStatement implements StatementInterface {
	private ExpressionInterface expressionToPrint;

	@Override
	public ProgramState execute(ProgramState crtState) throws Exception{
		ListInterface<ValueInterface> output = crtState.getOutput();
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		
		output.addLast(this.expressionToPrint.evaluate(symbolTable));
		
		return crtState;
	}
	
	// only displays print(var) (we're printing the print statement)
	public String toString() {
		String representation = "";
		representation += ("print(" + this.expressionToPrint.toString() + ")");
		return representation;
	}
}
