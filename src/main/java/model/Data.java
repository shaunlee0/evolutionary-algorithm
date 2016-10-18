package model;

/**
 * Created by shaun on 18/10/16.
 */
public class Data {
    int[] variables;
    int output;

    public Data(int[] variables, int output) {
        this.variables = variables;
        this.output = output;
    }

    public int[] getVariables() {
        return variables;
    }

    public void setVariables(int[] variables) {
        this.variables = variables;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
}
