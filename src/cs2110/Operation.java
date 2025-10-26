package cs2110;

/**
 * An operation performed on two arithmetic expressions
 */
public class Operation implements Expression<Double> {
    // TODO 3.6: implement fields
    /**
     * Create an Operation that performs an Operator `op` where `leftExp` is its left-hand argument
     * and `rightExp` is its right-hand
     */
    public Operation(Operator<Double> op, Expression<Double> leftExp, Expression<Double> rightExp) {
    }

    /**
     * Perform the operation on the left and right expressions
     */
    @Override
    public Double eval(VarTable vars) throws UnassignedVariableException {
        // TODO 3.6: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }


    /**
     * Returns a simplified form of this operation. If branches cannot be simplified any
     * further, then this will return an Operation expression. If both branches can be
     * simplified, then a Constant will be returned.
     */
    @Override
    public Expression<Double> simplify(VarTable vars) {
        // TODO 3.9: Implement this method according to its specifications.
        return this;
    }


    /**
     * Return the infix form of this operation. To avoid complications with nested operations,
     * parentheses are always included in the infix form.
     * e.g. the infix string of "x := 1 + 2" is "x := (1.0 + 2.0)".
     * @return
     */
    @Override
    public String infixString() {
        // TODO 3.6: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

}
