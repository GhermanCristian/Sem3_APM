package model.statement;

import java.io.BufferedReader;

import exception.InvalidTypeException;
import exception.UndefinedVariableException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.expression.ExpressionInterface;
import model.type.StringType;
import model.value.StringValue;
import model.value.ValueInterface;

public class CloseReadFileStatement implements StatementInterface{
	private final ExpressionInterface filePath;
	
	public CloseReadFileStatement(ExpressionInterface filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<StringValue, BufferedReader> fileTable = crtState.getFileTable();
		ValueInterface filePathValue = this.filePath.evaluate(symbolTable);
		
		if (filePathValue.getType().equals(new StringType()) == false) {
			throw new InvalidTypeException("File path should be a string");
		}
		
		// we know filePathValue is a StringValue, we can cast
		String filePathString = ((StringValue)filePathValue).getValue();
		if (symbolTable.isDefined(filePathString) == false) {
			throw new UndefinedVariableException("File path " + filePathString + " is not defined in the symbol table");
		}
		if (fileTable.isDefined((StringValue)filePathValue) == false) {
			throw new UndefinedVariableException("File path " + filePathString + " is not defined in the file table");
		}
		
		BufferedReader fileBuffer = fileTable.getValue((StringValue)filePathValue);
		fileBuffer.close();
		symbolTable.remove(filePathString);
		fileTable.remove((StringValue)filePathValue);
		
		return crtState;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("closeRead(" + this.filePath + ");\n");
		return representation;
	}
}
