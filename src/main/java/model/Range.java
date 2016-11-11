package model;

public class Range {
    double[] values;

    public Range(double value1, double value2){
        values = new double[2];
        if (value1<value2){
            values[0] = value1;
            values[1] = value2;
        }else{
            values[0] = value2;
            values[1] = value1;
        }
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }
}
