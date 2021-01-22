package model.statement;

import java.util.ArrayList;
import exception.InvalidProcedureArgumentException;
import exception.UndefinedVariableException;
import model.Procedure;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.MyDictionary;
import model.expression.ExpressionInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class CallProcedureStatement implements StatementInterface {
	private final String procedureName;
	private final ArrayList<ExpressionInterface> argumentValuesExpression;
	
	public CallProcedureStatement(String procedureName, ArrayList<ExpressionInterface> argumentValuesExpression) {
		this.procedureName = procedureName;
		this.argumentValuesExpression = argumentValuesExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		DictionaryInterface<String, Procedure> procedureTable = crtState.getProcedureTable();
		if (procedureTable.isDefined(this.procedureName) == false) {
			throw new UndefinedVariableException("CallProcedureStatement: procedure " + this.procedureName + " does not exist");
		}
		
		Procedure procedureFrame = procedureTable.getValue(this.procedureName);
		if (this.argumentValuesExpression.size() != procedureFrame.getArgumentNames().size()) {
			throw new InvalidProcedureArgumentException("CallProcedureStatement: invalid number of arguments");
		}
		
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		ArrayList<TypeInterface> argumentTypes = procedureFrame.getArgumentTypes();
		ArrayList<String> argumentNames = procedureFrame.getArgumentNames();
		

		DictionaryInterface<String, ValueInterface> procedureSymbolTable = new MyDictionary<String, ValueInterface>();
		for (int pos = 0; pos < this.argumentValuesExpression.size(); pos++) {
			ValueInterface argumentValue = this.argumentValuesExpression.get(pos).evaluate(symbolTable, heap);
			if (argumentValue.getType().equals(argumentTypes.get(pos)) == false) {
				throw new InvalidProcedureArgumentException("CallProcedureStatement: invalid argument type on position " + Integer.toString(pos));
			}
			// here we don't have to check for the validity of the names inside the procedure, that is done when creating it
			procedureSymbolTable.insert(argumentNames.get(pos), argumentValue);
		}
		
		crtState.getSymbolTableStack().push(procedureSymbolTable);
		crtState.getExecutionStack().push(new ReturnStatement());
		crtState.getExecutionStack().push(procedureFrame.getProcedureStatement());
		
		return null;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		// the expression - argument type checks have to be done inside execute, because in this method
		// we don't have access to the crtState => we can't access the procedure table
		// however, the proc statement will work well as long as the expressions match the argument (when creating the
		// procedure, we type check the statement with the argument types)
		return initialTypeEnvironment;
	}
}
