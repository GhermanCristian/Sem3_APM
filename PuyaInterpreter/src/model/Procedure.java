package model;

import java.util.ArrayList;

import model.statement.StatementInterface;
import model.type.TypeInterface;

public class Procedure {
	private final ArrayList<TypeInterface> argumentTypes;
	private final ArrayList<String> argumentNames;
	// the lengths of these 2 lists are assumed to be equal
	private final StatementInterface procedureStatement;
	
	public Procedure(ArrayList<TypeInterface> argumentTypes, ArrayList<String> argumentValues, StatementInterface procedureStatement) {
		this.argumentTypes = argumentTypes;
		this.argumentNames = argumentValues;
		this.procedureStatement = procedureStatement;
	}
	
	public ArrayList<TypeInterface> getArgumentTypes() {
		return this.argumentTypes;
	}
	
	public ArrayList<String> getArgumentNames() {
		return this.argumentNames;
	}
	
	public StatementInterface getProcedureStatement() {
		return this.procedureStatement;
	}
	
	@Override
	public String toString() {
		String representation = "";
		
		representation += "(";
		int pos;
		for (pos = 0; pos < argumentTypes.size() - 1; pos++) {
			representation += this.argumentTypes.get(pos).toString() + " " + this.argumentNames.get(pos) + ", ";
		}
		representation += this.argumentTypes.get(pos).toString() + " " + this.argumentNames.get(pos) + ")\n{";
		representation += this.procedureStatement.toString() + "}\n";
		
		return representation;
	}
}
