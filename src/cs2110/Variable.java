package cs2110;

/**
 * An expression representing a variable with a given name. Evaluates to a Double
 */
public class Variable implements Expression<Double> {
    // TODO 3.3: Implement fields

    /**
     * Creates a Variable expression given a name
     */
    public Variable(String var) {
    }

    /**
     * Returns the given name of a Variable expression
     */
    public String getName() {
        // TODO 3.3: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the value stored for this variable in the given <code>varTable</code>. If not found
     * in the <code>varTable</code>, throws an <code>UnassignedVariableException</code>.
     */
    @Override
    public Double eval(VarTable varTable) throws UnassignedVariableException {
        // TODO 3.3: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    /**
     * Simplifies the variable by returning either a constant with the correct value or
     * returning itself.
     */
    @Override
    public Expression<Double> simplify(VarTable vars) {
        // TODO 3.9: Implement this method according to its specifications.
        return this;
    }

    @Override
    public String infixString() {
        // TODO 3.3: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

}
