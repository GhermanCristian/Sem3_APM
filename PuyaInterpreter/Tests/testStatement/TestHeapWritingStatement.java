package testStatement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.ProgramState;
import model.expression.ValueExpression;
import model.statement.HeapAllocationStatement;
import model.statement.HeapWritingStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.BoolType;
import model.type.IntType;
import model.type.ReferenceType;
import model.value.IntValue;

public class TestHeapWritingStatement extends BaseTest {
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
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(23)));

		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestHeapWritingStatement: variable is undefined");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_VariableNotAReference_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new IntType());
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestHeapWritingStatement: variable is not a reference");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ReferenceTypeNotMatching_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new BoolType()));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			typeEnvironment = s1.getTypeEnvironment(typeEnvironment);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.getTypeEnvironment(typeEnvironment);
			fail("TestHeapWritingStatement: the reference type does not match the expression type");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void GetTypeEnvironment_ValidOperandTypes_TypeEnvironmentUnchanged() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(23)));
		
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
		StatementInterface s1 = new HeapWritingStatement("undefined baby", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
			fail("Variable is undefined in the symbol table");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_VariableNotInHeap_ThrowsException() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(23)));
		
		try {
			s1.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		try {
			s2.execute(crtState);
			fail("Variable is not in the heap");
		}
		catch (Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Execute_ValidInput_HeapUpdated() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(24)));
		StatementInterface s3 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		
		assertTrue(heap.isEmpty());
		try {
			s1.execute(crtState);
			s3.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(heap.size(), 1);
		assertEquals(((IntValue)heap.getValue(1)).getValue(), 23);
		
		try {
			s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		assertEquals(heap.size(), 1);
		assertEquals(((IntValue)heap.getValue(1)).getValue(), 24);
	}
	
	@Test
	public void Execute_ValidInput_ReturnsNull() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s3 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		StatementInterface s2 = new HeapWritingStatement("a", new ValueExpression(new IntValue(24)));
		ProgramState result = null;
		
		try {
			s1.execute(crtState);
			s3.execute(crtState);
			result = s2.execute(crtState);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertNull(result);
	}
}
