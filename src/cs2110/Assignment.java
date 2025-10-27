package cs2110;

/**
 * An expression representing an assignment operation, where a variable is updated to a new value
 */
public class Assignment implements Expression<VarTable> {
    private final Variable var;
    private final Expression<Double> aExpr;

    /**
     * Create an assignment between a variable and an arithmetic expression
     */
    public Assignment(Variable var, Expression<Double> aExpr) {
        this.var = var;
        this.aExpr = aExpr;
    }

    /**
     * Evaluates this assignment's arithmetic expression, assigns it to `varTable`, and returns
     * the updated `varTable`
     */
    @Override
    public VarTable eval(VarTable varTable) throws UnassignedVariableException {
        Double val = aExpr.eval(varTable);
        varTable.assign(var.getName(), val);
        return varTable;

    }

    /**
     * Simplifies the assignment statement by simplifying the arithmetic expression
     */
    @Override
    public Expression<VarTable> simplify(VarTable vars) {
        Expression<Double> simplifiedRight = aExpr.simplify(vars);
        return new Assignment(var,simplifiedRight);
    }

    @Override
    public String infixString() {
        return var.infixString() + " := " + aExpr.infixString();
    }

}
