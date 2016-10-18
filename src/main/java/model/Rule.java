package model;

/**
 * Created by shaun on 18/10/16.
 */
public class Rule {
    int[] conditions;
    int actual;

    public Rule(int[] conditions, int actual){
        this.conditions = conditions;
        this.actual = actual;
    }

    public int[] getConditions() {
        return conditions;
    }

    public void setConditions(int[] conditions) {
        this.conditions = conditions;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }
}
