package model.statement;

import model.ProgramState;
import model.ADT.StackInterface;

public class CompoundStatement implements StatementInterface {
	private final StatementInterface firstStatement;
	private final StatementInterface secondStatement;
	
	public CompoundStatement(StatementInterface firstStatement, StatementInterface secondStatement) {
		this.firstStatement = firstStatement;
		this.secondStatement = secondStatement;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception{
		StackInterface <StatementInterface> stack = crtState.getExecutionStack();
		stack.push(this.secondStatement);
		stack.push(this.firstStatement);
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
