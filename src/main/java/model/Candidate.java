package model;

import java.util.Random;

/**
 * Created by shaun on 27/09/16.
 */
public class Candidate {
    private int fitness;
    public int[] encoding;
    private int encodingLength;

    public Candidate(int encodingLength,int geneRangeUpperBound, boolean autoInitialise) {
        encoding = new int[encodingLength];
        this.encodingLength = encodingLength;
        if(autoInitialise){
            for (int i = 0; i < encodingLength; i++) {
                Random random = new Random();
                int value = random.nextInt(geneRangeUpperBound);
                encoding[i] = value;
            }
        }

    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int[] getBinaryEncoding() {
        return encoding;
    }

    public void setBinaryEncoding(int[] encoding) {
        this.encoding = encoding;
    }
}
