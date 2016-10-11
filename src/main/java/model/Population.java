package model;

import java.util.ArrayList;

/**
 * Population class to encapsulate a population for use in subsequent generations.
 */
public class Population {
    private int size;
    private ArrayList<Candidate> population;
    private int encodingLengthOfCandidate;

    public Population(int size,int encodingLengthOfCandidate) {

            population = new ArrayList<Candidate>();
            this.size = size;
        this.encodingLengthOfCandidate = encodingLengthOfCandidate;


    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Candidate> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<Candidate> population) {
        this.population = population;
    }

    public void initialise(){
        for (int i = 0; i < size; i++) {
            Candidate candidate = new Candidate(encodingLengthOfCandidate,true);
            population.add(candidate);
        }
    }

    public Candidate getBestCandidate() {

        Candidate fittestIndividual = null;

        for (Candidate candidate : population) {
            if(fittestIndividual == null){
                fittestIndividual = candidate;
            }else if(candidate.getFitness() > fittestIndividual.getFitness()){
                fittestIndividual = candidate;
            }
        }

        return fittestIndividual;
    }

    public void clear() {
        population.clear();
    }

    public void fill(ArrayList<Candidate> offspringPopulation) {

        this.population = offspringPopulation;

    }

    public int getWorstFromPopulation() {

        int unFittestIndividualIndex = 0;

        for (int i = 0; i < population.size(); i++) {
            Candidate unFittestIndividual = population.get(i);
            if(unFittestIndividual == null){
                unFittestIndividualIndex = i;
            }else if(unFittestIndividual.getFitness() > unFittestIndividual.getFitness()){
                unFittestIndividualIndex = i;
            }
        }

        return unFittestIndividualIndex;
    }
}
