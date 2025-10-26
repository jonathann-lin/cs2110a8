package cs2110;

/**
 * An Expression that chains two commands together in a sequence
 */
public class Sequence implements Expression<VarTable> {
    // TODO 3.8: Implement fields

    /**
     * Creates a sequence of two commands, where `leftCommand` is executed before `rightCommand`
     */
    public Sequence(Expression<VarTable> leftCommand, Expression<VarTable> rightCommand) {
    }

    /**
     * Evaluates the sequence of commands in order
     */
    @Override
    public VarTable eval(VarTable varTable) throws UnassignedVariableException {
        // TODO 3.8: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    /**
     * Simplifies commands in the sequence as much as possible, and returns the simplified form.
     * If evaluation is possible, then simplification considers evaluation when simplifying commands.
     */
    @Override
    public Expression<VarTable> simplify(VarTable vars) {
        // TODO 3.9: Implement this method according to its specifications.
        return this;
    }

    @Override
    public String infixString() {
        // TODO 3.8: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

}
