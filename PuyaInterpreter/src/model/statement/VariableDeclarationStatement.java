package model.statement;

import exception.AlreadyDefinedVariableException;
import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.BoolType;
import model.type.IntType;
import model.type.StringType;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class VariableDeclarationStatement implements StatementInterface{
	private final String variableName;
	private final TypeInterface variableType;
	
	public VariableDeclarationStatement(String variableName, TypeInterface variableType) {
		this.variableName = variableName;
		this.variableType = variableType;
	}

	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		
		if (symbolTable.isDefined(this.variableName) == true) {
			throw new AlreadyDefinedVariableException("Variable " + this.variableName + " is already defined");
		}
		
		if (this.variableType.equals(new IntType())) { // variable is of type int
			symbolTable.insert(this.variableName, new IntType().getDefaultValue());
		}
		else if(this.variableType.equals(new BoolType())) { // variable is of type bool
			symbolTable.insert(this.variableName, new BoolType().getDefaultValue());
		}
		else if(this.variableType.equals(new StringType())) {
			symbolTable.insert(this.variableName, new StringType().getDefaultValue());
		}
		// I'm not sure if this part will ever be reached, because I'm already being given a TypeInterface (which should be valid)
		// but just in case...
		else {
			throw new InvalidTypeException("Invalid type when trying to declare " + this.variableName);
		}
		
		return crtState;
	}
	
	public String toString() {
		String representation = "";
		representation += (this.variableType.toString() + " " + this.variableName + ";\n");
		return representation;
	}
}
