package model;

import model.statement.StatementInterface;

public class Example {
	private final StatementInterface currentStatement;
	private final String exampleDescription; // the names of the commands executed in this example (int x; x = x + 2 etc.)
	private final String repositoryLocation;
	
	public Example(StatementInterface currentStatement, String exampleDescription, String repositoryLocation) {
		this.currentStatement = currentStatement;
		this.exampleDescription = exampleDescription;
		this.repositoryLocation = repositoryLocation;
	}
	
	public StatementInterface getStatement() {
		return this.currentStatement;
	}

	public String getExampleDescription() {
		return this.exampleDescription;
	}

	public String getRepositoryLocation() {
		return this.repositoryLocation;
	}
	
	@Override
	public String toString() {
		return this.currentStatement.toString();
	}
}
