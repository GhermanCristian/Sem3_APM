package model.statement;

import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.type.BoolType;
import model.type.TypeInterface;

public class ForStatement implements StatementInterface {
	private final StatementInterface initialStatement;
	private final ExpressionInterface conditionalExpression;
	private final StatementInterface finalStatement;
	private final StatementInterface bodyStatement; // the one inside the curly brackets
	
	public ForStatement(StatementInterface initialStatement, ExpressionInterface conditionalExpression, StatementInterface finalStatement, StatementInterface innerStatement) {
		this.initialStatement = initialStatement;
		this.conditionalExpression = conditionalExpression;
		this.finalStatement = finalStatement;
		this.bodyStatement = innerStatement;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		if (this.initialStatement instanceof AssignmentStatement == false) {
			throw new InvalidTypeException("ForStatement: InitialStatement is not an AssignmentStatement");
		}
		if (this.finalStatement instanceof AssignmentStatement == false) {
			throw new InvalidTypeException("ForStatement: FinalStatement is not an AssignmentStatement");
		}
		
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		stack.push(new WhileStatement(
					this.conditionalExpression,
					new CompoundStatement(this.bodyStatement, this.finalStatement)
				));
		
		return this.initialStatement.execute(crtState); // this is an AssignmentStatement so it always returns null, but this way is more elegant
	}
	
	public String toString() {
		String representation = "";
		
		// I have to do this in order to remove the "\n" and the ";" at the end of each statement
		String initialStatementString = this.initialStatement.toString();
		initialStatementString = initialStatementString.substring(0, initialStatementString.length() - 2);
		String finalStatementString = this.finalStatement.toString();
		finalStatementString = finalStatementString.substring(0, finalStatementString.length() - 2);
		
		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the statement
		representation += ("for (" + initialStatementString + "; " + this.conditionalExpression.toString() + "; " + finalStatementString + ") {\n\t");
		representation += (this.bodyStatement.toString() + "}\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		// normally, initial and finalStatement are AssignmentStatements, so they will not modify the type environment,
		// so we can write them separately, without storing each resulting typeEnv; basically we only call this to check for exceptions
		this.initialStatement.getTypeEnvironment(initialTypeEnvironment);
		if (this.conditionalExpression.typeCheck(initialTypeEnvironment).equals(new BoolType()) == false) {
			throw new InvalidTypeException("ForStatement: Conditional expression is not boolean");
		}
		this.finalStatement.getTypeEnvironment(initialTypeEnvironment);
		this.bodyStatement.getTypeEnvironment(initialTypeEnvironment.clone());
		
		return initialTypeEnvironment;
	}
}
