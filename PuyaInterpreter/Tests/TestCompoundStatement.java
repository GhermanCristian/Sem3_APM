import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.expression.ValueExpression;
import model.statement.AssignmentStatement;
import model.statement.CompoundStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.value.IntValue;
import model.value.ValueInterface;

public class TestCompoundStatement {
	static StackInterface<StatementInterface> stack;
	static DictionaryInterface<String, ValueInterface> symbolTable;
	static ListInterface<ValueInterface> output;
	static ProgramState crtState;
	
	@BeforeClass
	public static void initialiseData() {
		stack = new MyStack<StatementInterface>();
		symbolTable = new MyDictionary<String, ValueInterface>();
		output = new MyList<ValueInterface>();
		crtState = new ProgramState(stack, symbolTable, output, null);
	}
	
	@Before
	public void clearData() {
		stack.clear();
		symbolTable.clear();
		output.clear();
	}
	
	@Test
	public void Execute_ValidInput_CorrectStackSize() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
		);
		
		assertEquals(stack.size(), 0);
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(stack.size(), 2); // the stack now has two statements
	}
	
	@Test
	public void Execute_ValidInput_CorrectStatementOrder() {
		StatementInterface s1 = new CompoundStatement(
			new VariableDeclarationStatement("abc", new IntType()), 
			new AssignmentStatement("abc", new ValueExpression(new IntValue(23)))
		);
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertFalse(symbolTable.isDefined("abc"));
		try { // execute the first statement (var decl)
			stack.pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(symbolTable.isDefined("abc"));
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), 0); // the default initialization value is 0
		
		try { // execute the second statement (value assignment)
			stack.pop().execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), 23);
		assertEquals(stack.size(), 0); // the stack is now empty
	}
}
