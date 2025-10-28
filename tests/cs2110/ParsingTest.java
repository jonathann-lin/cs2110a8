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

    @Test
    @DisplayName("Parsing a single assignment sequence")
    void testSingleAssignmentSequence() throws IOException, MalformedExpressionException, UnassignedVariableException {
        Queue<Token> q = Main.tokenize("x := 1");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        assertSame(Assignment.class, expr.getClass());
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(1.0, vars.getValue("x"));
        assertEquals("x := 1.0", expr.infixString());
    }

    @Test
    @DisplayName("Parsing a sequence of two assignments")
    void testTwoAssignmentSequence() throws IOException, MalformedExpressionException, UnassignedVariableException {
        Queue<Token> q = Main.tokenize("x := 1 ; y := 2");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        assertSame(Sequence.class, expr.getClass());
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(1.0, vars.getValue("x"));
        assertEquals(2.0, vars.getValue("y"));
        assertEquals("x := 1.0 ; y := 2.0", expr.infixString());
    }

    @Test
    @DisplayName("Parsing a nested sequence")
    void testNestedSequence() throws IOException, MalformedExpressionException, UnassignedVariableException {
        Queue<Token> q = Main.tokenize("x := 1 ; (y := 2 ; z := 3)");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        assertSame(Sequence.class, expr.getClass());
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(1.0, vars.getValue("x"));
        assertEquals(2.0, vars.getValue("y"));
        assertEquals(3.0, vars.getValue("z"));
        assertEquals("x := 1.0 ; y := 2.0 ; z := 3.0", expr.infixString());
    }

    @Test
    @DisplayName("Sequence uses variables assigned in previous commands")
    void testSequenceVariableDependency() throws IOException, MalformedExpressionException, UnassignedVariableException {
        Queue<Token> q = Main.tokenize("x := 1 ; y := x + 1 ; x := 2");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        assertSame(Sequence.class, expr.getClass());
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(2.0, vars.getValue("x")); // final x
        assertEquals(2.0, vars.getValue("y")); // y uses x's value at assignment time
        assertEquals("x := 1.0 ; y := (x + 1.0) ; x := 2.0", expr.infixString());
    }

    @Test
    @DisplayName("Nested sequences with dependent variables")
    void testNestedSequenceWithDependencies() throws IOException, MalformedExpressionException, UnassignedVariableException {
        Queue<Token> q = Main.tokenize("(a := 1 ; b := a + 1) ; c := b + 1");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        assertSame(Sequence.class, expr.getClass());
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(1.0, vars.getValue("a"));
        assertEquals(2.0, vars.getValue("b"));
        assertEquals(3.0, vars.getValue("c"));
        assertEquals("a := 1.0 ; b := (a + 1.0) ; c := (b + 1.0)", expr.infixString());
    }


    @Test
    @DisplayName("Parsing assignment with negative number")
    void testNegativeNumberAssignment() throws Exception {
        Queue<Token> q = Main.tokenize("x := -5");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(-5.0, vars.getValue("x"));
        assertEquals("x := -5.0", expr.infixString());
    }

    @Test
    @DisplayName("Parsing arithmetic expression with precedence")
    void testArithmeticPrecedence() throws Exception {
        Queue<Token> q = Main.tokenize("x := 1 + 2 * 3");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(7.0, vars.getValue("x")); // 1 + (2*3)
        assertEquals("x := (1.0 + (2.0 * 3.0))", expr.infixString());
    }


    @Test
    @DisplayName("Parsing assignment using unassigned variable throws")
    void testUnassignedVariableThrows() throws Exception {
        Queue<Token> q = Main.tokenize("x := y + 1");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        assertThrows(Expression.UnassignedVariableException.class, () -> expr.eval(VarTable.empty()));
    }

    @Test
    @DisplayName("Parsing malformed missing assignment operator")
    void testMalformedMissingAssign() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x 1");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Parsing malformed extra token at start")
    void testMalformedExtraToken() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("1");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Parsing arithmetic with negative numbers and multiplication")
    void testArithmeticEdgeCases() throws Exception {
        Queue<Token> q = Main.tokenize("x := 0 - 1 * 3");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(-3.0, vars.getValue("x"));
        assertEquals("x := (0.0 - (1.0 * 3.0))", expr.infixString());
    }

    @Test
    @DisplayName("Parsing chained arithmetic expression")
    void testChainedArithmetic() throws Exception {
        Queue<Token> q = Main.tokenize("x := 1 + 2 - 3 * 4 / 2");
        Parser p = new Parser(q);
        Expression<VarTable> expr = p.parseProgram();
        VarTable vars = expr.eval(VarTable.empty());
        assertEquals(-3.0, vars.getValue("x")); // ((1+2) - ((3*4)/2))
        assertEquals("x := (1.0 + (2.0 - (3.0 * (4.0 / 2.0))))", expr.infixString());
    }

    @Test
    @DisplayName("Assignment with missing value throws MalformedExpressionException")
    void testAssignmentMissingValue() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x :=");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Assignment with extra token after number throws MalformedExpressionException")
    void testAssignmentExtraToken() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := 1 2");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("If statement missing then throws MalformedExpressionException")
    void testIfMissingThen() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("if true { x := 1 } else { x := 2 }");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("If statement missing else throws MalformedExpressionException")
    void testIfMissingElse() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("if true then { x := 1 }");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }



    @Test
    @DisplayName("Unbalanced parentheses throws MalformedExpressionException")
    void testUnbalancedParentheses() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("(x := 1 ; y := 2");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Empty program throws MalformedExpressionException")
    void testEmptyProgram() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Invalid token at start throws MalformedExpressionException")
    void testInvalidTokenStart() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize(";");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Multiple consecutive semicolons throws MalformedExpressionException")
    void testConsecutiveSemicolons() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := 1 ;; y := 2");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Negative number without following digit throws MalformedExpressionException")
    void testMinusWithoutNumber() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := -");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Extra tokens after program throws MalformedExpressionException")
    void testExtraTokensAfterProgram() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := 1 1");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Assignment followed by unexpected variable throws MalformedExpressionException")
    void testAssignmentFollowedByVar() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := 1 y");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Number directly followed by parentheses throws MalformedExpressionException")
    void testNumberFollowedByParen() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("1(x := 2)");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Extra colon in assignment throws MalformedExpressionException")
    void testExtraColon() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x :: = 1");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Semicolon directly after open brace throws MalformedExpressionException")
    void testSemicolonAfterBrace() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := 1 ; { ; y := 2 }");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Floating point without leading digit throws MalformedExpressionException")
    void testDotWithoutLeadingDigit() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := .5");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Two operators in a row throws MalformedExpressionException")
    void testTwoOperatorsInARow() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := 1 + * 2");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Boolean comparison missing operator throws MalformedExpressionException")
    void testBooleanMissingOperator() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x 2");
            Parser p = new Parser(q);
            p.parseBooleanExpr();
        });
    }

    @Test
    @DisplayName("Multiple colons and semicolons in a row throws MalformedExpressionException")
    void testMultipleColonsSemicolons() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("x := 1 ; ; : y := 2");
            Parser p = new Parser(q);
            p.parseProgram();
        });
    }

    @Test
    @DisplayName("Nested parentheses with no content throws MalformedExpressionException")
    void testEmptyParentheses() {
        assertThrows(Parser.MalformedExpressionException.class, () -> {
            Queue<Token> q = Main.tokenize("()");
            Parser p = new Parser(q);
            p.parseArithmeticExpr();
        });
    }


}
