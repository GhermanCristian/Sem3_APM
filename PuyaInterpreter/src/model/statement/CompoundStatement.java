package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.type.TypeInterface;

public class CompoundStatement implements StatementInterface {
	private final StatementInterface firstStatement;
	private final StatementInterface secondStatement;
	
	public CompoundStatement(StatementInterface firstStatement, StatementInterface secondStatement) {
		this.firstStatement = firstStatement;
		this.secondStatement = secondStatement;
	}
	
	private void processStatement(ProgramState crtState, StatementInterface statement) throws Exception {
		if (statement instanceof CompoundStatement) {
			((CompoundStatement) statement).addStatementsToStack(crtState);
		}
		else {
			crtState.getExecutionStack().push(statement);
		}
	}
	
	private void addStatementsToStack(ProgramState crtState) throws Exception {
		this.processStatement(crtState, this.secondStatement);
		this.processStatement(crtState, this.firstStatement);
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		this.addStatementsToStack(crtState);
		return crtState.getExecutionStack().pop().execute(crtState);
	}
	
	@Override
	public String toString() {
		String representation = "";
		// need to better highlight the fact that this is a CompoundStatement
		representation += (this.firstStatement.toString() + this.secondStatement.toString());
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		return this.secondStatement.getTypeEnvironment(this.firstStatement.getTypeEnvironment(initialTypeEnvironment));
	}
}
