package cs2110;

/**
 * Interface for an Expression node with a return type of T
 */
public interface Expression<T> {

    /**
     * Return the result of evaluating this expression. Uses the mappings provided in
     * <code>varTable</code> to evaluate known variables.
     * <br>
     * Throws <code>UnassignedVariableException</code> if expressions haven't assigned
     * variables before using them in a command.
     */
    T eval(VarTable varTable) throws UnassignedVariableException;

    /**
     * Return the simplified form of this expression such that there are no more
     * evaluations that can be done on sub-expressions. Uses the mappings provided in
     * <code>varTable</code> to simplify known variables. Does not modify this Expression.
     */
    Expression<T> simplify(VarTable varTable);

    /**
     * Return the infix form of the <code>Expression</code>, e.g. "x := (1 + 2) * 3"
     */
    String infixString();

    /**
     *  An exception that is thrown when calling <code>Expression.eval(varTable)</code> and
     *  variables in the <code>Expression</code> aren't assigned in <code>varTable</code>.
     */
    class UnassignedVariableException extends Exception {}
}

