package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.ValueExpression;
import model.statement.HeapAllocationStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ReferenceValue;

public class TestHeapAllocationStatement extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void GetTypeEnvironment_UndefinedVariable_ThrowsException() {
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestHeapAllocationStatement: variable is undefined");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_VariableNotAReference_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestHeapAllocationStatement: variable is not a reference");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ReferenceTypeNotMatching_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new BoolType()));
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestHeapAllocationStatement: the reference type does not match the expression type");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ValidOperandTypes_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s2.getTypeEnvironment(s1.getTypeEnvironment(typeEnvironment));
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(typeEnvironment.size(), 1);
		assertEquals(typeEnvironment.getValue("a"), new ReferenceType(new IntType()));
	}
	
	@Test
	public void Execute_VariableUndefinedInSymbolTable_ThrowsException() {
		StatementInterface s1 = new HeapAllocationStatement("undefined baby", new ValueExpression(new IntValue()));
		try {
			s1.execute(crtState);
			fail("Variable is undefined in the symbol table");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidInput_CorrectSymbolTableUpdate() {
		TypeInterface t1 = new ReferenceType(new IntType());
		StatementInterface s1 = new VariableDeclarationStatement("a", t1);
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(symbolTable.getValue("a"), t1.getDefaultValue());
		
		try {
			s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(symbolTable.getValue("a"), new ReferenceValue(1, t1));
	}
	
	@Test
	public void Execute_ValidInput_CorrectHeapAllocation() {
		TypeInterface t1 = new ReferenceType(new IntType());
		StatementInterface s1 = new VariableDeclarationStatement("a", t1);
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
		} 
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue(heap.isEmpty());
		
		try {
			s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(heap.size(), 1);
		assertEquals(((IntValue)heap.getValue(1)).getValue(), 23);
	}
	
	@Test
	public void Execute_ValidInput_ReturnsNull() {
		TypeInterface t1 = new ReferenceType(new IntType());
		StatementInterface s1 = new VariableDeclarationStatement("a", t1);
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		ProgramState result = null;
		
		try {
			s1.execute(crtState);
			result = s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertNull(result);
	}
}
