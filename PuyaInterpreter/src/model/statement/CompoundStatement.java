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
		return crtState;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("(" + this.firstStatement.toString() + ";\n" + this.secondStatement.toString() + ");\n");
		return representation;
	}
}
