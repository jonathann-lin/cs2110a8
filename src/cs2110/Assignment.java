package cs2110;

/**
 * An expression representing an assignment operation, where a variable is updated to a new value
 */
public class Assignment implements Expression<VarTable> {
    // TODO 3.5: Implement fields

    /**
     * Create an assignment between a variable and an arithmetic expression
     */
    public Assignment(Variable var, Expression<Double> aExpr) {
    }

    /**
     * Evaluates this assignment's arithmetic expression, assigns it to `varTable`, and returns
     * the updated `varTable`
     */
    @Override
    public VarTable eval(VarTable varTable) throws UnassignedVariableException {
        // TODO 3.5: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    /**
     * Simplifies the assignment statement by simplifying the arithmetic expression
     */
    @Override
    public Expression<VarTable> simplify(VarTable vars) {
        // TODO 3.9: Implement this method according to its specifications.
        return this;
    }

    @Override
    public String infixString() {
        // TODO 3.5: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

}
