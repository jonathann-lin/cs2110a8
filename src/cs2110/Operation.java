package cs2110;

/**
 * An operation performed on two arithmetic expressions
 */
public class Operation implements Expression<Double> {

    private final Operator<Double> op;
    private final Expression<Double> leftExp;
    private final Expression<Double> rightExp;

    /**
     * Create an Operation that performs an Operator `op` where `leftExp` is its left-hand argument
     * and `rightExp` is its right-hand
     */
    public Operation(Operator<Double> op, Expression<Double> leftExp, Expression<Double> rightExp) {
        this.op = op;
        this.leftExp = leftExp;
        this.rightExp = rightExp;
    }

    /**
     * Perform the operation on the left and right expressions
     */
    @Override
    public Double eval(VarTable vars) throws UnassignedVariableException {
        return op.operate(leftExp.eval(vars), rightExp.eval(vars));
    }


    /**
     * Returns a simplified form of this operation. If branches cannot be simplified any
     * further, then this will return an Operation expression. If both branches can be
     * simplified, then a Constant will be returned.
     */
    @Override
    public Expression<Double> simplify(VarTable vars) {
        Expression<Double> leftSimplify = leftExp.simplify(vars);
        Expression<Double> rightSimplify = rightExp.simplify(vars);
        if(leftSimplify instanceof Constant && rightSimplify instanceof  Constant){
            double leftVal = ((Constant<Double>) leftSimplify).eval(vars);
            double rightVal = ((Constant<Double>) rightSimplify).eval(vars);
            return new Constant<>(op.operate(leftVal, rightVal));
        }
        else return new Operation(op, leftSimplify, rightSimplify);
    }


    /**
     * Return the infix form of this operation. To avoid complications with nested operations,
     * parentheses are always included in the infix form.
     * e.g. the infix string of "x := 1 + 2" is "x := (1.0 + 2.0)".
     * @return
     */
    @Override
    public String infixString() {
        return "(" + leftExp.infixString() + " " + op.symbol() + " " + rightExp.infixString() + ")";
    }

}
