package model;

/**
 * Created by shaun on 18/10/16.
 */
public class Data {
    double[] conditions;
    double output;

    public Data(double[] conditions, double output) {
        this.conditions = conditions;
        this.output = output;
    }

    public double[] getConditions() {
        return conditions;
    }

    public void setConditions(double[] conditions) {
        this.conditions = conditions;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(int output) {
        this.output = output;
    }
}
