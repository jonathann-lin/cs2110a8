package cs2110;

/**
 * An expression representing a variable with a given name. Evaluates to a Double
 */
public class Variable implements Expression<Double> {

    private final String name;

    /**
     * Creates a Variable expression given a name
     */
    public Variable(String var) {
        name = var;
    }

    /**
     * Returns the given name of a Variable expression
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value stored for this variable in the given <code>varTable</code>. If not found
     * in the <code>varTable</code>, throws an <code>UnassignedVariableException</code>.
     */
    @Override
    public Double eval(VarTable varTable) throws UnassignedVariableException {
        if (varTable.contains(name)) {
            return varTable.getValue(name);
        }
        throw new UnassignedVariableException();

    }

    /**
     * Simplifies the variable by returning either a constant with the correct value or
     * returning itself.
     */
    @Override
    public Expression<Double> simplify(VarTable vars) {
        if (vars.contains(name)) {
            return new Constant<>(vars.getValue(name));
        } else {
            return this;
        }

    }

    @Override
    public String infixString() {
        return name;
    }

}
