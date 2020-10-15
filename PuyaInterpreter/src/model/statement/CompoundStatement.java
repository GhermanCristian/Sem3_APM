package model.statement;

import model.ProgramState;
import model.ADT.StackInterface;

public class CompoundStatement implements StatementInterface {
	private StatementInterface firstStatement;
	private StatementInterface secondStatement;
	
	@Override
	public ProgramState execute(ProgramState crtState) {
		StackInterface <StatementInterface> stack = crtState.getExecutionStack();
		stack.push(this.secondStatement);
		stack.push(this.firstStatement);
		return crtState;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("(" + this.firstStatement.toString() + "; " + this.secondStatement.toString() + ")");
		return representation;
	}
}
