package model;

import application.EvolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Candidate solution representation using an array of doubles to encode a solution.
 */
public class Candidate {
    private double fitness;
    public double[] encoding;
    public ArrayList<Rule> rules;

    public Candidate(int encodingLength, boolean autoInitialise) {
        encoding = new double[encodingLength];
        rules = new ArrayList<>();
        if(autoInitialise){
            Random random = new Random();
            for (int i = 0; i < encodingLength; i++) {
                if ((i + 1) % 13 == 0){
                    double output = random.nextInt(2);
                    encoding[i] = output;
                }else{
                    double value = random.nextDouble();
                    encoding[i] = value;
                }
            }
            extractRules();
        }
    }

    public Candidate(Candidate another){
        this.fitness = another.getFitness();
        this.encoding = another.getBinaryEncoding().clone();
        rules = new ArrayList<>();
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void extractRules(){
        if(this.rules==null){
            System.out.println("Rules are null");
        }
        if(this.rules.size() > 1){
            this.rules.clear();
        }
        int count = 0;
        int reachedInConditions = 0;
        for (int i = 0; i < encoding.length - 12; i+=13) {
            Range[] conditions = new Range[6];
            for (int j = 0; j < 6; j++) {
                Range range = new Range(encoding[reachedInConditions],encoding[reachedInConditions+1]);
                conditions[j] = range;
                reachedInConditions += 2;
            }
            double actual = encoding[i+12];
            Rule rule = new Rule(conditions,actual);
            rules.add(rule);
        }
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double[] getBinaryEncoding() {
        return encoding;
    }

    public void setEncoding(double[] encoding) {
        this.encoding = encoding;
    }

}
