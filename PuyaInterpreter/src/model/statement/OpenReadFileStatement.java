package model.statement;

import java.io.BufferedReader;
import java.io.FileReader;

import exception.AlreadyDefinedVariableException;
import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.expression.ExpressionInterface;
import model.type.StringType;
import model.value.StringValue;
import model.value.ValueInterface;

public class OpenReadFileStatement implements StatementInterface{
	private final ExpressionInterface filePath;
	
	public OpenReadFileStatement(ExpressionInterface filePath) {
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
		if (fileTable.isDefined((StringValue)filePathValue) == true) {
			throw new AlreadyDefinedVariableException("File path " + filePathString + " is already in the file table");
		}
		BufferedReader fileBuffer = new BufferedReader(new FileReader(filePathString));
		fileTable.insert((StringValue)filePathValue, fileBuffer);
	
		return crtState;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("openRead(" + this.filePath + ");\n");
		return representation;
	}
}
