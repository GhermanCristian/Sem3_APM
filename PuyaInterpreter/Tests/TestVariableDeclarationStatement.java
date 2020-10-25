import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;

import model.ProgramState;
import model.ADT.DictionaryInterface;
import model.ADT.ListInterface;
import model.ADT.MyDictionary;
import model.ADT.MyList;
import model.ADT.MyStack;
import model.ADT.StackInterface;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IntValue;
import model.value.ValueInterface;

public class TestVariableDeclarationStatement{
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
	public void Execute_NonExistentInt_IntAdded() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());
		
		assertEquals(symbolTable.size(), 0);
		assertFalse(symbolTable.isDefined("abc"));
		assertTrue(symbolTable.isEmpty());
		// getvalue throws an error when undefined; don't know if I should keep it here or not
		//assertNotEquals(((IntValue)symbolTable.getValue("abc")).getValue(), VariableDeclarationStatement.DEFAULT_INT_VALUE);
		try {
			s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(symbolTable.size(), 1);
		assertTrue(symbolTable.isDefined("abc"));
		assertFalse(symbolTable.isEmpty());
		assertEquals(((IntValue)symbolTable.getValue("abc")).getValue(), VariableDeclarationStatement.DEFAULT_INT_VALUE);
	}
	
	@Test
	public void Execute_ExistingVariable_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new IntType());
		StatementInterface s2 = new VariableDeclarationStatement("abc", new BoolType());
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		
		try {
			s2.execute(crtState);
			fail("Variable already exists");
		}
		catch (Exception e){
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_NonExistentBool_BoolAdded() {
		StatementInterface s1 = new VariableDeclarationStatement("abc", new BoolType());
		
		assertEquals(symbolTable.size(), 0);
		try {
			s1.execute(crtState);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(symbolTable.size(), 1);
		assertTrue(symbolTable.isDefined("abc"));
		assertFalse(symbolTable.isEmpty());
		assertEquals(((BoolValue)symbolTable.getValue("abc")).getValue(), VariableDeclarationStatement.DEFAULT_BOOL_VALUE);
	}

}
