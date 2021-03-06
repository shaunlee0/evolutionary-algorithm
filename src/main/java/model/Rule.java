package model;

/**
 * Created by shaun on 18/10/16.
 */
public class Rule {
    Range[] conditions;
    double actual;
    int fitness;

    public Rule(Range[] conditions, double actual){
        this.conditions = conditions;
        this.actual = actual;
        this.fitness = 0;
    }

    public Range[] getConditions() {
        return conditions;
    }

    public void setConditions(Range[] conditions) {
        this.conditions = conditions;
    }

    public double getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString(){
        String toReturn = "conditions : ";

        for (int i = 0; i < conditions.length; i++) {
            toReturn = toReturn + "(" + i + ") " + conditions[i].getValues()[0] + " : " + conditions[i].getValues()[1] + " | ";
        }

        return toReturn + "actual : " + getActual() + ", fitness " + getFitness();
    }
}
