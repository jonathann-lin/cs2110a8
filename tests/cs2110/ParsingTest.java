package cs2110;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Queue;

import cs2110.Expression.UnassignedVariableException;
import static cs2110.Parser.*;
import static org.junit.jupiter.api.Assertions.*;

public class ParsingTest {

    // These tests assume that methods like `Expression.infixString()` are implemented.
    // This is largely because of the lack of an `Expression.equals()` method.

    @Test
    @DisplayName("Parsing a Variable results in a Variable expression")
    void testVariable() throws IOException, MalformedExpressionException {
        Queue<Token> q = Main.tokenize("x");
        Parser p = new Parser(q);
        Expression<Double> expr = p.parseArithmeticUnit();
        assertSame(Variable.class, expr.getClass());
        assertEquals("x", expr.infixString());
    }

    @Test
    @DisplayName("Parsing multiplication results in an operation with the multiplication operator")
    void testMultiplication() throws IOException, MalformedExpressionException,
            UnassignedVariableException {
        Queue<Token> q = Main.tokenize("1 * x");
        Parser p = new Parser(q);
        Expression<Double> expr = p.parseArithmeticTerm();
        assertSame(Operation.class, expr.getClass());
        assertEquals(2.0, expr.eval(VarTable.of("x", 2)));
        assertEquals("(1.0 * x)", expr.infixString());
    }

    @Test
    @DisplayName("Parsing repeated multiplication results in nested operations with " +
            "the multiplication operator")
    void testMultiplication2() throws IOException, MalformedExpressionException,
            UnassignedVariableException {
        Queue<Token> q = Main.tokenize("1 * x * 2");
        Parser p = new Parser(q);
        Expression<Double> expr = p.parseArithmeticTerm();
        assertSame(Operation.class, expr.getClass());
        assertEquals(4.0, expr.eval(VarTable.of("x", 2)));
        assertEquals("(1.0 * (x * 2.0))", expr.infixString());
    }

    @Test
    @DisplayName("Parsing addition results in an operation with the addition operator")
    void testAddition() throws IOException, MalformedExpressionException, UnassignedVariableException {
        Queue<Token> q = Main.tokenize("1 + x");
        Parser p = new Parser(q);
        Expression<Double> expr = p.parseArithmeticExpr();
        assertSame(Operation.class, expr.getClass());
        assertEquals(3.0, expr.eval(VarTable.of("x", 2)));
        assertEquals("(1.0 + x)", expr.infixString());
    }

    @Test
    @DisplayName("Parsing repeated addition results in a nested operation with the addition operator")
    void testAddition2() throws IOException, MalformedExpressionException, UnassignedVariableException {
        Queue<Token> q = Main.tokenize("1 + x + 2");
        Parser p = new Parser(q);
        Expression<Double> expr = p.parseArithmeticExpr();
        assertSame(Operation.class, expr.getClass());
        assertEquals(5.0, expr.eval(VarTable.of("x", 2)));
        assertEquals("(1.0 + (x + 2.0))", expr.infixString());
    }

    @Test
    @DisplayName("Parsing a number through a ArithmeticExpression rule results in a number")
    void testTopDownArithmetic() throws IOException, MalformedExpressionException,
            UnassignedVariableException {
        Queue<Token> q = Main.tokenize("1");
        Parser p = new Parser(q);
        System.out.println(q.peek());
        Expression<Double> expr = p.parseArithmeticExpr();
        assertSame(Constant.class, expr.getClass());
        assertEquals(1, expr.eval(VarTable.empty()));
    }

}
