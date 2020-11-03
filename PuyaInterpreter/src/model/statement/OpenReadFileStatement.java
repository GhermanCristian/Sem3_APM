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
		
		if (filePathValue.getType().equals(new StringType())) {
			// we know filePathValue is a StringValue, we can cast
			String filePathString = ((StringValue)filePathValue).getValue();
			
			if (symbolTable.isDefined(filePathString) == true) {
				throw new AlreadyDefinedVariableException("File path " + filePathString + " is already in the symbol table");
			}
			
			if (fileTable.isDefined((StringValue)filePathValue) == false) {
				BufferedReader fileBuffer = new BufferedReader(new FileReader(filePathString));
				fileTable.insert((StringValue)filePathValue, fileBuffer);
			}
			
			else {
				throw new AlreadyDefinedVariableException("File path " + filePathString + " is already in the file table");
			}
		}
		
		else {
			throw new InvalidTypeException("File path should be a string");
		}
		
		return crtState;
	}
}
