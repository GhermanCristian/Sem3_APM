package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class ClearOutOfScopeVariablesStatement implements StatementInterface {
	private final DictionaryInterface<String, ValueInterface> initialSymbolTable;
	// this is the state of the sym table before entering the scope
	// the sym table of the crtState will contain even the variables declared inside the scope, and they should not be visible once we exit the scope
	// note: we can't just use a clone for the sym table in the inner scope, like in the fork statement
	// note2: we can use a clone for the typeEnv, because in the inner scope we can't modify the type of a variable, but we can modify the value
	// of a previously existing variable, and we want that to persist even outside of the scope
	/*
		int x = 2;
		for (int a = 1; a < 5; a++){
			int y;
			x = 6;
		}
		// here, x should have the value 6 and 
		// y should not exist in either the type env (works because of clone) or the sym table (works because of this clear statement)
	*/
	
	public ClearOutOfScopeVariablesStatement(DictionaryInterface<String, ValueInterface> initialSymbolTable) {
		this.initialSymbolTable = initialSymbolTable;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable(); // the sym table after executing the inner scope
		symbolTable.forEachKey(variableName -> {if(initialSymbolTable.isDefined(variableName) == false) symbolTable.remove(variableName);});
		
		// this is a "free" statement <=> it doesn't require an entire step to be executed
		// it really shouldn't even be seen in the execution stack, it's like a garbage collector
		// the stack will never be empty here, we only call this statement we have other statements to execute after it, there's no
		// use doing this if the program just ends after it
		return crtState.getExecutionStack().pop().execute(crtState);
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		// the variables that are removed from the sym table don't have to be removed from the main type env, because
		// they were never there in the first place (the inner scope worked with a clone)
		return initialTypeEnvironment;
	}
}
