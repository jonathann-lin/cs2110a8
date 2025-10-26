package cs2110;

/**
 * Represents a binary arithmetic operator on real numbers.  Interface also defines singleton
 * operators for common operations.
 */
public interface Operator<T> {

    /**
     * Return the result of evaluating the operation on left operand `operand1` and right operand
     * `operand2`.
     */
    T operate(double operand1, double operand2);

    /**
     * Return the symbol used to represent this operator in expression strings.  For example, the
     * "plus" operator would have symbol "+".
     */
    String symbol();

    /**
     * Return a known operator given its symbol, `op`.  Guaranteed to recognize "+", "-", "*", "/".
     */
    static Operator fromToken(TokenType op) {
        return switch (op) {
            case PLUS -> ADD;
            case MINUS -> SUBTRACT;
            case MULTIPLY -> MULTIPLY;
            case DIVIDE -> DIVIDE;
            default -> throw new IllegalArgumentException("Unknown operator: " + op);
        };
    }

    /* Recognized operator symbols. */
    /* Note: All variables declared in an interface are automatically static and final. */
    String ADD_SYMBOL = "+";
    String SUBTRACT_SYMBOL = "-";
    String MULTIPLY_SYMBOL = "*";
    String DIVIDE_SYMBOL = "/";

    /**
     * Operator for addition.
     */
    Operator<Double> ADD = new Operator<>() {
        public Double operate(double operand1, double operand2) {
            return operand1 + operand2;
        }

        public String symbol() {
            return ADD_SYMBOL;
        }
    };

    /**
     * Operator for subtraction.
     */
    Operator<Double> SUBTRACT = new Operator<>() {
        public Double operate(double operand1, double operand2) {
            return operand1 - operand2;
        }

        public String symbol() {
            return SUBTRACT_SYMBOL;
        }
    };

    /**
     * Operator for multiplication.
     */
    Operator<Double> MULTIPLY = new Operator<>() {
        public Double operate(double operand1, double operand2) {
            return operand1 * operand2;
        }

        public String symbol() {
            return MULTIPLY_SYMBOL;
        }
    };

    /**
     * Operator for division.
     */
    Operator<Double> DIVIDE = new Operator<>() {
        public Double operate(double operand1, double operand2) {
            return operand1 / operand2;
        }

        public String symbol() {
            return DIVIDE_SYMBOL;
        }
    };


}
