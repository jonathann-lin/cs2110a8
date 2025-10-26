package cs2110;

/**
 * An expression representing a while loop, where one command is performed repeatedly
 * while a boolean guard is true, and then a second command is performed
 */
public class While implements Expression<VarTable> {
    // TODO 4.2: Implement fields


    /**
     * Creates a while loop with the specified boolean guard, and do/else commands
     */
    public While(Expression<Boolean> guard, Expression<VarTable> doCommand, Expression<VarTable> elseCommand) {
    }

    @Override
    public VarTable eval(VarTable varTable) throws UnassignedVariableException {
        // TODO 4.2: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    /**
     * If the guard can be evaluated to false, then return a simplified elseCommand. Otherwise,
     * return a While command with simplified subexpressions.
     */
    @Override
    public Expression<VarTable> simplify(VarTable vars) {
        // TODO 4.2: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

    @Override
    public String infixString() {
        // TODO 4.2: Implement this method according to its specifications.
        throw new UnsupportedOperationException();
    }

}
