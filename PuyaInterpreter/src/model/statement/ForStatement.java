package model.statement;

import exception.AlreadyDefinedVariableException;
import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.expression.RelationalExpression;
import model.type.BoolType;
import model.type.IntType;
import model.type.TypeInterface;

public class ForStatement implements StatementInterface {
	private final String variableName;
	private final StatementInterface variableDeclarationStatement;
	private final ExpressionInterface initialExpression; // for(int v = initialExpression...)
	private final ExpressionInterface conditionalExpression; // has to be a relational expression
	private final StatementInterface finalStatement; // has to be an increment statement
	private final StatementInterface bodyStatement; // the one inside the curly brackets
	
	public ForStatement(String variableName, 
						ExpressionInterface initialExpression, 
						ExpressionInterface conditionalExpression, 
						StatementInterface finalStatement, 
						StatementInterface bodyStatement) {
		this.variableName = variableName;
		this.variableDeclarationStatement = new VariableDeclarationStatement(this.variableName, new IntType());
		// I create vardecl here and not in 'execute' because I also need it for the type check
		this.initialExpression = initialExpression;
		this.conditionalExpression = conditionalExpression;
		this.finalStatement = finalStatement;
		this.bodyStatement = bodyStatement;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		// we need a rel. expr because it checks if its operands are integers
		if (this.conditionalExpression instanceof RelationalExpression == false) {
			throw new InvalidTypeException("ForStatement: Conditional expression is not a RelationalExpression");
		}
		if (this.finalStatement instanceof IncrementStatement == false) {
			throw new InvalidTypeException("ForStatement: FinalStatement is not an IncrementStatement");
		}
		
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		if (stack.size() > 0) {
			stack.push(new ClearOutOfScopeVariablesStatement(crtState.getSymbolTable().clone()));
			// this is used to remove the variable that we create (for int = var..) after we finish the for execution
			// if there are no statements after this for => thread ends => no use in removing those variables
		}
			
		stack.push(new WhileStatement(
				this.conditionalExpression,
				new CompoundStatement(this.bodyStatement, this.finalStatement)
			));
		stack.push(new AssignmentStatement(this.variableName, this.initialExpression));
		
		// when I execute the for like this, it will start with the var decl in the same step; but normally, 
		// it would have push it to the stack (after all the others, at the end), then the var decl would start in the next step;
		// if I want to return to that behaviour: add var decl to stack, and here only return null
		return this.variableDeclarationStatement.execute(crtState);
	}
	
	public String toString() {
		String representation = "";
		
		// I have to do this in order to remove the "\n" and the ";" at the end of each statement
		String initialStatementString = this.variableDeclarationStatement.toString();
		initialStatementString = initialStatementString.substring(0, initialStatementString.length() - 2);
		String finalStatementString = this.finalStatement.toString();
		finalStatementString = finalStatementString.substring(0, finalStatementString.length() - 2);
		
		// this indentation doesn't work past 1 level - I'm going to need sth like an indentationLevel when creating the statement
		representation += ("for (" + initialStatementString + " = " + this.initialExpression.toString() + "; " + this.conditionalExpression.toString() + "; " + finalStatementString + ") {\n\t");
		representation += (this.bodyStatement.toString() + "}\n");
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		if (initialTypeEnvironment.isDefined(this.variableName) == true) {
			throw new AlreadyDefinedVariableException("ForStatement: variable " + this.variableName + " is already defined in the type environment");
		}
		initialTypeEnvironment = this.variableDeclarationStatement.getTypeEnvironment(initialTypeEnvironment);
		
		if (this.initialExpression.typeCheck(initialTypeEnvironment).equals(new IntType()) == false) {
			throw new InvalidTypeException("ForStatement: Initial assignment expression is not integer");
		}
		if (this.conditionalExpression.typeCheck(initialTypeEnvironment).equals(new BoolType()) == false) {
			throw new InvalidTypeException("ForStatement: The conditional expression is not boolean");
		}
		this.finalStatement.getTypeEnvironment(initialTypeEnvironment);
		this.bodyStatement.getTypeEnvironment(initialTypeEnvironment.clone());
		
		return initialTypeEnvironment;
	}
}
