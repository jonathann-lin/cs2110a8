    package cs2110;

    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Nested;
    import org.junit.jupiter.api.Test;

    import cs2110.Expression.UnassignedVariableException;
    import static org.junit.jupiter.api.Assertions.*;

    public class ExpressionTest {

        /**
         * Helper function that tests whether an expression throws an UnassignedVariableException
         * when evaluated by itself
         */
        void testThrowUnassignedVariableException(Expression expr) {
            assertThrows(UnassignedVariableException.class,  () -> expr.eval(VarTable.empty()));
        }

        @Nested
        class ConstantTest {

            @Test
            @DisplayName("A Constant evaluates to itself")
            void testEvaluate() throws UnassignedVariableException {
                Expression<Double> constant = new Constant<>(5.0);
                Expression<Boolean> constantBool = new Constant<>(false);

                assertEquals(5.0, constant.eval(VarTable.empty()));
                assertFalse(constantBool.eval(VarTable.empty()));
            }

            @Test
            @DisplayName("A Constant node should produce an infix representation with just its value (as " +
                    "formatted by String.valueOf(double))")
            void testInfix() {
                Expression<Double> expr = new Constant<>(1.5);
                assertEquals("1.5", expr.infixString());

                expr = new Constant<>(Math.PI);
                assertEquals("3.141592653589793", expr.infixString());

                expr = new Constant<>(-2.0);
                assertEquals("-2.0", expr.infixString());
            }

            @Test
            @DisplayName("A Constant node should simplify to itself (regardless of var table)")
            void testSimplify() {
                Expression<Double> expr = new Constant<>(1.5);
                Expression<Double> opt = expr.simplify(VarTable.empty());
                assertEquals(expr, opt);
            }

        }

        @Nested
        class VariableTest {

            @Test
            @DisplayName("A Variable evaluates to a number it is mapped to")
            void testEvaluate() throws UnassignedVariableException {
                Expression<Double> expr = new Variable("x");
                assertEquals(1.0, expr.eval(VarTable.of("x", 1.0)));
            }

            @Test
            @DisplayName("A Variable node should throw an UnassignedVariableException when evaluated if its " +
                    "variable is not in the var map")
            void testEvalUnbound() {
                Expression<Double> expr = new Variable("x");
                testThrowUnassignedVariableException(expr);
            }

            @Test
            @DisplayName("A Variable node should produce the string we store as a variable")
            void testInfix() {
                Expression<Double> expr = new Variable("x");
                assertEquals("x", expr.infixString());
            }

            @Test
            @DisplayName("A Variable node should simplify to a constant if properly mapped")
            void testSimplify() {
                Expression<Double> expr = new Variable("x");
                Expression<Double> opt = expr.simplify(VarTable.of("x", 1));
                assertSame(opt.getClass(), Constant.class);
            }

            @Test
            @DisplayName("A Variable node should simplify to itself if not mapped")
            void testSimplifyUndefined() {
                Expression<Double> expr = new Variable("x");
                Expression<Double> opt = expr.simplify(VarTable.empty());
                assertEquals(expr, opt);
            }
        }

        @Nested
        class ArithmeticOperationTest {

            @Test
            @DisplayName("An Operation node for ADD with two Constant operands should evaluate to their " +
                    "sum")
            void testEvalAdd() throws UnassignedVariableException {
                Expression<Double> expr = new Operation(Operator.ADD, new Constant<>(1.5), new Constant<>(2.0));
                assertEquals(3.5, expr.eval(VarTable.empty()));
            }

            @Test
            @DisplayName("An Operation node for ADD with a Variable for an operand should evaluate " +
                    "to its operands' sum when the variable is in the var map")
            void testEvalAddBound() throws UnassignedVariableException {
                Expression expr = new Operation(Operator.ADD, new Constant<>(1.5), new Variable("x"));
                assertEquals(3.5, expr.eval(VarTable.of("x", 2.0)));

                expr = new Operation(Operator.ADD, new Variable("x"), new Constant<>(2.0));
                assertEquals(3.5, expr.eval(VarTable.of("x", 1.5)));
            }

            @Test
            @DisplayName("An Operation node for ADD with a Variable for an operand should throw an " +
                    "UnboundVariableException when evaluated if the variable is not in the var map")
            void testEvalAddUnbound() {
                Expression expr1 = new Operation(Operator.ADD, new Constant<>(1.5), new Variable("x"));
                testThrowUnassignedVariableException(expr1);

                Expression expr2 = new Operation(Operator.ADD, new Variable("x"), new Constant<>(2.0));
                testThrowUnassignedVariableException(expr2);
            }

            @Test
            @DisplayName("An Operation node for SUBTRACT should produce the correct string representation")
            void testInfix() {
                Expression expr = new Operation(Operator.SUBTRACT, new Constant<>(1.5), new Variable("x"));
                assertEquals("(1.5 - x)", expr.infixString());
            }
        }

        @Nested
        class AssignmentTest {

            @Test
            @DisplayName("An Assignment evaluates to a VarTable that stores the outcome of assignment")
            void testEvaluate() throws UnassignedVariableException {
                Expression<VarTable> expr = new Assignment(new Variable("x"), new Constant<>(1.0));
                VarTable vars = expr.eval(VarTable.empty());
                assertTrue(vars.contains("x"));
                assertEquals(1.0, vars.getValue("x"));
            }

            @Test
            @DisplayName("An Assignment node should throw an UnassignedVariableException when evaluated if a subexpression " +
                    "throws an UnassignedVariableException")
            void testEvalUnbound() {
                Expression<VarTable> expr = new Assignment(new Variable("x"), new Variable("y"));
                testThrowUnassignedVariableException(expr);
            }

            @Test
            @DisplayName("An Assignment node should produce the correct string representation")
            void testInfix() {
                Expression<VarTable> expr = new Assignment(new Variable("x"), new Constant<>(1.0));
                assertEquals("x := 1.0", expr.infixString());
            }

            @Test
            @DisplayName("An Assignment node should simplify subexpressions")
            void testSimplify() {
                Expression<VarTable> expr = new Assignment(new Variable("x"),
                        new Operation(Operator.ADD, new Variable("y"), new Constant<>(1.0)));
                Expression<VarTable> opt = expr.simplify(VarTable.of("y", 1));
                assertSame(opt.getClass(), Assignment.class);
                assertEquals("x := 2.0", opt.infixString());
            }
        }

        @Nested
        class SequenceTest {

            @Test
            @DisplayName("A Sequence evaluates to a VarTable that stores the outcome of assignments")
            void testEvaluate() throws UnassignedVariableException {
                Expression<VarTable> expr = new Sequence(
                        new Assignment(new Variable("x"), new Constant<>(1.0)),
                        new Assignment(new Variable("y"), new Constant<>(2.0)));
                VarTable vars = expr.eval(VarTable.empty());
                assertEquals(2, vars.varSet().size());
                assertTrue(vars.contains("x"));
                assertTrue(vars.contains("y"));
                assertEquals(1.0, vars.getValue("x"));
                assertEquals(2.0, vars.getValue("y"));
            }

            @Test
            @DisplayName("A Sequence node should throw an UnassignedVariableException when evaluated if a subexpression " +
                    "throws an UnassignedVariableException")
            void testEvalUnbound() {
                Expression<VarTable> expr = new Sequence(
                        new Assignment(new Variable("x"), new Variable("z")),
                        new Assignment(new Variable("y"), new Constant<>(2.0)));
                testThrowUnassignedVariableException(expr);
            }

            @Test
            @DisplayName("A Sequence node should produce the correct string representation")
            void testInfix() {
                Expression<VarTable> expr = new Sequence(
                        new Assignment(new Variable("x"), new Constant<>(1.0)),
                        new Assignment(new Variable("y"), new Constant<>(2.0)));
                assertEquals("x := 1.0 ; y := 2.0", expr.infixString());
            }

            @Test
            @DisplayName("A Sequence node where subexpressions can't be simplified cannot be simplified further")
            void testSimplify() {
                Expression<VarTable> expr = new Sequence(
                        new Assignment(new Variable("x"), new Constant<>(1.0)),
                        new Assignment(new Variable("y"), new Constant<>(2.0)));
                Expression<VarTable> opt = expr.simplify(VarTable.empty());
                assertSame(opt.getClass(), Sequence.class);
                assertEquals("x := 1.0 ; y := 2.0", opt.infixString());
            }
        }

        @Nested
        class BooleanOperationTest {

        }

        @Nested
        class ConditionalTest {

    //        @Test
    //        @DisplayName("A Conditional evaluates to a VarTable that stores the " +
    //                "outcome of the if-statement")
    //        void testEvaluateTrue() throws UnassignedVariableException {
    //            Expression<VarTable> expr = new Conditional(
    //                    new Constant<>(true),
    //                    new Assignment(new Variable("x"), new Constant<>(1.0)),
    //                    new Assignment(new Variable("y"), new Constant<>(2.0)));
    //            VarTable vars = expr.eval(VarTable.empty());
    //            assertEquals(1, vars.varSet().size());
    //            assertTrue(vars.contains("x"));
    //            assertEquals(1.0, vars.getValue("x"));
    //        }
    //
    //        @Test
    //        @DisplayName("A Conditional node should throw an UnassignedVariableException when evaluated " +
    //                "if a subexpression throws an UnassignedVariableException")
    //        void testEvalUnbound() {
    //            Expression<VarTable> expr = new Conditional(
    //                    new Constant<>(true),
    //                    new Assignment(new Variable("x"), new Variable("a")),
    //                    new Assignment(new Variable("y"), new Constant<>(2.0)));
    //            testThrowUnassignedVariableException(expr);
    //        }
    //
    //        @Test
    //        @DisplayName("A Conditional node should produce the correct string representation")
    //        void testInfix() {
    //            Expression<VarTable> expr = new Conditional(
    //                    new Constant<>(true),
    //                    new Assignment(new Variable("x"), new Constant<>(1.0)),
    //                    new Assignment(new Variable("y"), new Constant<>(2.0)));
    //            assertEquals("if true then {x := 1.0} else {y := 2.0}", expr.infixString());
    //        }
    //
    //        @Test
    //        @DisplayName("A Conditional node where subexpressions can be simplified will be " +
    //                "simplified to a subexpression")
    //        void testSimplify() {
    //            Expression<VarTable> expr = new Conditional(
    //                    new Constant<>(true),
    //                    new Assignment(new Variable("x"), new Constant<>(1.0)),
    //                    new Assignment(new Variable("y"), new Constant<>(2.0)));
    //            Expression<VarTable> opt = expr.simplify(VarTable.empty());
    //            assertSame(opt.getClass(), Assignment.class);
    //            assertEquals("x := 1.0", opt.infixString());
    //        }
        }

        @Nested
        class WhileTest {

    //        @Test
    //        @DisplayName("A While node evaluates to a VarTable that stores the " +
    //                "outcome of the while-loop")
    //        void testEvaluate() throws UnassignedVariableException {
    //            // TODO 4.2
    //        }
    //
    //        @Test
    //        @DisplayName("A While node should throw an UnassignedVariableException when evaluated " +
    //                "if a subexpression throws an UnassignedVariableException")
    //        void testEvalUnbound() {
    //            Expression<VarTable> expr = new While(
    //                    new Constant<>(true),
    //                    new Assignment(new Variable("x"), new Variable("a")),
    //                    new Assignment(new Variable("y"), new Constant<>(2.0)));
    //            testThrowUnassignedVariableException(expr);
    //        }
    //
    //        @Test
    //        @DisplayName("A While node should produce the correct string representation")
    //        void testInfix() {
    //            Expression<VarTable> expr = new While(
    //                    new Constant<>(false),
    //                    new Assignment(new Variable("x"), new Constant<>(1.0)),
    //                    new Assignment(new Variable("y"), new Constant<>(2.0)));
    //            assertEquals("while false do {x := 1.0} then {y := 2.0}", expr.infixString());
    //        }
    //
    //        @Test
    //        @DisplayName("A While node where subexpressions can be simplified will be " +
    //                "simplified to a subexpression")
    //        void testSimplify() {
    //            Expression<VarTable> expr = new While(
    //                    new Constant<>(false),
    //                    new Assignment(new Variable("x"), new Constant<>(1.0)),
    //                    new Assignment(new Variable("y"), new Constant<>(2.0)));
    //            Expression<VarTable> opt = expr.simplify(VarTable.empty());
    //            assertSame(opt.getClass(), Assignment.class);
    //            assertEquals("y := 2.0", opt.infixString());
    //        }
        }

    }