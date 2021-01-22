package model.statement;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.type.TypeInterface;

public class ConditionalAssignmentStatement implements StatementInterface {
	private final String variableName;
	private final ExpressionInterface conditionalExpression;
	private final ExpressionInterface trueBranchExpression;
	private final ExpressionInterface falseBranchExpression;
	
	public ConditionalAssignmentStatement(String variableName, ExpressionInterface conditionalExpression, ExpressionInterface trueBranchExpression, ExpressionInterface falseBranchExpression) {
		this.variableName = variableName;
		this.conditionalExpression = conditionalExpression;
		this.trueBranchExpression = trueBranchExpression;
		this.falseBranchExpression = falseBranchExpression;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();		
		stack.push(new IfStatement(this.conditionalExpression, 
									new AssignmentStatement(this.variableName, this.trueBranchExpression), 
									new AssignmentStatement(this.variableName, this.falseBranchExpression)));
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += (this.variableName + " = " + this.conditionalExpression.toString() + " ? " + this.trueBranchExpression.toString() + " : " + this.falseBranchExpression.toString());
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		return initialTypeEnvironment;
	}
}
