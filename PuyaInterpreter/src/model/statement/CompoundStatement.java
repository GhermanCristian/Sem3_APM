package model.statement;

import model.ProgramState;

public class CompoundStatement implements StatementInterface {
	private final StatementInterface firstStatement;
	private final StatementInterface secondStatement;
	
	public CompoundStatement(StatementInterface firstStatement, StatementInterface secondStatement) {
		this.firstStatement = firstStatement;
		this.secondStatement = secondStatement;
	}
	
	private void processStatement(ProgramState crtState, StatementInterface statement) throws Exception {
		if (statement instanceof CompoundStatement) {
			statement.execute(crtState);
		}
		else {
			crtState.getExecutionStack().push(statement);
		}
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		this.processStatement(crtState, this.secondStatement);
		this.processStatement(crtState, this.firstStatement);
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		// need to better highlight the fact that this is a CompoundStatement
		representation += (this.firstStatement.toString() + this.secondStatement.toString());
		return representation;
	}
}
