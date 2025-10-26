package cs2110;

/**
 * An expression representing a conditional operation, where one of two commands are evaluated
 * based on a boolean guard
 */
public class Conditional implements Expression<VarTable> {
    // TODO 4.2: Implement fields


    /**
     * Creates a conditional with the specified boolean guard, and then/else commands
     */
    public Conditional(Expression<Boolean> guard, Expression<VarTable> thenCommand,
                       Expression<VarTable> elseCommand) {
    }

    /**
     * Evaluates the boolean guard, and evaluates the then/else commands accordingly
     */
    @Override
    public VarTable eval(VarTable varTable) throws UnassignedVariableException {
        // TODO 4.2: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to simplify by evaluating the boolean guard and returning simplified expressions.
     */
    @Override
    public Expression<VarTable> simplify(VarTable vars) {
        // TODO 4.2: Implement this method according to its specifications.
        return this;
    }

    @Override
    public String infixString() {
        // TODO 4.2: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

}
