package model.statement;

import java.util.ArrayList;
import exception.InvalidTypeException;
import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.StackInterface;
import model.expression.ExpressionInterface;
import model.type.TypeInterface;
import model.value.ValueInterface;

public class SwitchStatement implements StatementInterface {
	private final ExpressionInterface switchExpression;
	private final ArrayList<ExpressionInterface> caseExpressionList;
	private final ArrayList<StatementInterface> caseStatementList;
	
	public SwitchStatement(ExpressionInterface switchExpression, ArrayList<ExpressionInterface> caseExpressionList, ArrayList<StatementInterface> caseStatementList) {
		this.switchExpression = switchExpression;
		this.caseExpressionList = caseExpressionList;
		this.caseStatementList = caseStatementList;
	}
	
	@Override
	public ProgramState execute(ProgramState crtState) throws Exception {
		if (this.caseExpressionList.size() + 1 != this.caseStatementList.size()) {
			throw new Exception("SwitchStatement: number of case expressions does not match the number of case statements");
		}
		
		DictionaryInterface<String, ValueInterface> symbolTable = crtState.getSymbolTable();
		DictionaryInterface<Integer, ValueInterface> heap = crtState.getHeap();
		StackInterface<StatementInterface> stack = crtState.getExecutionStack();
		
		if (stack.size() > 0) {
			stack.push(new ClearOutOfScopeVariablesStatement(symbolTable.clone()));
		}
		
		int pos;
		ValueInterface switchExpressionValue = this.switchExpression.evaluate(symbolTable, heap);
		for (pos = 0; pos < this.caseExpressionList.size(); pos++) {
			if (this.caseExpressionList.get(pos).evaluate(symbolTable, heap).equals(switchExpressionValue) == true) {
				stack.push(this.caseStatementList.get(pos));
				return null;
			}
		}
		
		// the 'default' case
		stack.push(this.caseStatementList.get(pos));
		
		return null;
	}
	
	@Override
	public String toString() {
		String representation = "";
		representation += ("switch(" + this.switchExpression.toString() + ")\n");
		
		int pos;
		for (pos = 0; pos < this.caseExpressionList.size(); pos++) {
			representation += "case (" + this.caseExpressionList.get(pos).toString() + ") \n{\n" + this.caseStatementList.get(pos) + "}\n";
		}
		representation += "default\n{\n" + this.caseStatementList.get(pos) + "}\n";
		
		return representation;
	}

	@Override
	public DictionaryInterface<String, TypeInterface> getTypeEnvironment(
			DictionaryInterface<String, TypeInterface> initialTypeEnvironment) throws Exception {
		TypeInterface switchExpressionType = this.switchExpression.typeCheck(initialTypeEnvironment);
		
		for (ExpressionInterface caseExpression : this.caseExpressionList) {
			if (caseExpression.typeCheck(initialTypeEnvironment).equals(switchExpressionType) == false) {
				throw new InvalidTypeException("SwitchStatement: expression does not match the type of the switch expression");
			}
		}
		for (StatementInterface caseStatement : this.caseStatementList) {
			caseStatement.getTypeEnvironment(initialTypeEnvironment.clone());
		}
		
		return initialTypeEnvironment;
	}
}
