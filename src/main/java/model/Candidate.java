package model;

import application.EvolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Candidate solution representation using an binary array of int to encode a solution.
 */
public class Candidate {
    private double fitness;
    public int[] encoding;
    public ArrayList<Rule> rules;

    public Candidate(int encodingLength, boolean autoInitialise) {
        encoding = new int[encodingLength];
        rules = new ArrayList<Rule>();
        if(autoInitialise){
            for (int i = 0; i < encodingLength; i++) {
                Random random = new Random();
                int value = random.nextInt(2);
                encoding[i] = value;
            }
            extractRules();
        }
    }

    public Candidate(Candidate another){
        this.fitness = another.getFitness();
        this.encoding = another.getBinaryEncoding().clone();
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public void extractRules(){
        int count = 0;
        for (int i = 0; i < encoding.length - 5; i+=6) {
            int[] conditions = Arrays.copyOfRange(encoding,i,i+5);
            int actual = encoding[i+5];
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

    public int[] getBinaryEncoding() {
        return encoding;
    }

    public void setBinaryEncoding(int[] encoding) {
        this.encoding = encoding;
    }

    public int getGene(int index) {
        return this.encoding[index];
    }

    public void setGene(int index, int gene) {
        this.encoding[index] = gene;
    }
}
