package service;


import application.EvolutionaryAlgorithm;
import model.Candidate;

public class MutationService {

    public boolean isFitnessHigherPostMutation(int[] mutatedEncoding, Candidate candidate){

        double fitnessOfCandidate = EvolutionaryAlgorithm.evaluateCandidate(candidate);
        double mutationFitness = 0;

        for (int i = 0; i < mutatedEncoding.length; i++) {
            int val = mutatedEncoding[i];
            if (val == 1) {
                mutationFitness++;
            }
        }

        mutationFitness = mutationFitness / EvolutionaryAlgorithm.encodingLength * 100;


        return mutationFitness > fitnessOfCandidate;
    }

}
