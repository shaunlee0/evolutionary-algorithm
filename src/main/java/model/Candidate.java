package model;

import java.util.Random;

/**
 * Created by shaun on 27/09/16. Candidate solution representation using an binary array of int to encode a solution.
 */
public class Candidate {
    private double fitness;
    private int[] encoding;

    Candidate(int encodingLength, boolean autoInitialise) {
        encoding = new int[encodingLength];
        if(autoInitialise){
            for (int i = 0; i < encodingLength; i++) {
                Random random = new Random();
                int value = random.nextInt(2);
                encoding[i] = value;
            }
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
}
