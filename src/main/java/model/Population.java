package model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by shaun on 27/09/16.
 */
public class Population {
    private int size;
    private int geneRangeUpperBound;
    private int encodingLengthOfCandidate;
    private Candidate[] population;

    public Population(int size,int encodingLengthOfCandidate,int geneRangeUpperBound, boolean isOffSpring) {

        if(!isOffSpring){
            population = new Candidate[size];
            this.size = size;
            for (int i = 0; i < size; i++) {
                Candidate candidate = new Candidate(encodingLengthOfCandidate,geneRangeUpperBound,true);
                population[i] = candidate;
            }
        }else{
            population = new Candidate[size];
            this.size = size;
        }

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Candidate[] getPopulation() {
        return population;
    }

    public void setPopulation(Candidate[] population) {
        this.population = population;
    }
}
