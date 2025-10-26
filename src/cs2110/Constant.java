package cs2110;

/**
 * An expression representing a fixed value (e.g., a number or a boolean).
 * Evaluates to a <code>T</code> object
 */
public class Constant<T> implements Expression<T> {

    /**
     * The value of this expression
     */
    private final T value;

    /**
     * Create an expression with value of <code>value</code>
     */
    protected Constant(T value) {
        this.value = value;
    }

    /**
     * Evaluates the constant to the value it was defined as in <code>varTable</code>
     */
    @Override
    public T eval(VarTable varTable) {
        return value;
    }

    /**
     * Returns the string representation of this expression
     */
    @Override
    public String infixString() {
        return value.toString();
    }

    /**
     * Returns itself, as constants cannot be simplified further
     */
    @Override
    public Expression<T> simplify(VarTable vars) {
        return this;
    }


    @Override
    public String toString() {
        return "Constant(" + infixString() + ")";
    }
}


