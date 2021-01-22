package testExpression;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.*;
import baseTest.BaseTest;
import model.expression.ExpressionInterface;
import model.expression.HeapReadingExpression;
import model.expression.ValueExpression;
import model.expression.VariableExpression;
import model.statement.HeapAllocationStatement;
import model.statement.StatementInterface;
import model.statement.VariableDeclarationStatement;
import model.type.IntType;
import model.type.ReferenceType;
import model.type.TypeInterface;
import model.value.IntValue;
import model.value.ReferenceValue;
import model.value.ValueInterface;

public class TestHeapReadingExpression extends BaseTest {
	@BeforeClass
	public static void initialiseData() {
		BaseTest.initialiseData();
	}
	
	@After
	public void clearAndCloseData() {
		super.initialiseData();
	}
	
	@Test
	public void TypeCheck_ExpressionNotReference_ThrowsException() {
		ExpressionInterface e1 = new HeapReadingExpression(new ValueExpression(new IntValue()));
		try {
			e1.typeCheck(typeEnvironment);
			fail("Expression is not a reference");
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void TypeCheck_ValidExpressionType_ReturnsInnerType() {
		ExpressionInterface e1 = new HeapReadingExpression(new ValueExpression(new ReferenceValue(new IntType())));
		TypeInterface result = null;
		try {
			result = e1.typeCheck(typeEnvironment);
		}
		catch(Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(result, new IntType());
	}
	
	@Test
	public void Evaluate_UndefinedVariable_ThrowsException() {
		ExpressionInterface e1 = new HeapReadingExpression(new ValueExpression(new ReferenceValue(new IntType())));
		try {
			e1.evaluate(symbolTable, heap);
			fail("Undefined variable");
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void Evaluate_ValidInput_CorrectValue() {
		StatementInterface s1 = new VariableDeclarationStatement("a", new ReferenceType(new IntType()));
		StatementInterface s2 = new HeapAllocationStatement("a", new ValueExpression(new IntValue(23)));
		ExpressionInterface e1 = new HeapReadingExpression(new VariableExpression("a"));
		
		ValueInterface res = null;
		try {
			s1.execute(crtState);
			s2.execute(crtState);
			res = e1.evaluate(symbolTable, heap);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals(((IntValue)res).getValue(), 23);
	}
}
