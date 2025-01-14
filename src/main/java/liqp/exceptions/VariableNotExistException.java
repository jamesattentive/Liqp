package liqp.exceptions;


public class VariableNotExistException extends RuntimeException {
    private final String variableName;

    public VariableNotExistException(String variableName) {
        super(String.format("Variable '%s' does not exist", variableName));

        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }
}
