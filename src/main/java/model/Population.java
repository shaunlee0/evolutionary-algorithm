package model;

/**
 * Population class to encapsulate a population for use in subsequent generations.
 */
public class Population {
    private int size;
    private Candidate[] population;

    public Population(int size,int encodingLengthOfCandidate, boolean randomGeneration) {

        if(randomGeneration){
            population = new Candidate[size];
            this.size = size;
            for (int i = 0; i < size; i++) {
                Candidate candidate = new Candidate(encodingLengthOfCandidate,true);
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
}
