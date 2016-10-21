package model;

/**
 * Created by shaun on 18/10/16.
 */
public class Data {
    int[] conditions;
    int output;

    public Data(int[] conditions, int output) {
        this.conditions = conditions;
        this.output = output;
    }

    public int[] getConditions() {
        return conditions;
    }

    public void setConditions(int[] conditions) {
        this.conditions = conditions;
    }

    public int getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
}
