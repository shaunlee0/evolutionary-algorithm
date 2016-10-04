package application;

import model.Candidate;
import model.Population;
import service.MutationService;

import java.util.Random;

public class EvolutionaryAlgorithm {

    private static final Random random = new Random();
    private static final int populationSize = 100;
    public static final int encodingLength = 8;
    private static final double mutationProbability = 0.01;
    private static int generations = 1;
    private static MutationService mutationService = new MutationService();

    public static void main(String[] args) {


        Population population = new Population(populationSize, encodingLength, true);
        boolean success = evaluatePopulation(population);

        //do until exit condition met
        while (generations < 1000 && !success) {

            success = evaluatePopulation(population);
            Candidate bestFromPopulation = population.getBestCandidate();

            Population offspring = new Population(populationSize, encodingLength, false);

            //Create offspring using selection
            for (int i = 0; i < populationSize; i++) {

                //select parents
                Candidate[] parents = selectParents(population);

                //select individuals for next population
                Candidate bestFromParents = getBestFromParents(parents);

                //Fill up to population size with offspring obtained from above service
                offspring.getPopulation()[i] = bestFromParents;
            }

            //for offspring crossover
            for (int i = 0; i < populationSize; i++) {
                crossOverParentsAtIndex(offspring, i, i + 1);
                i++;
            }

            //Mutate on a given probability
            for (int i = 0; i < populationSize; i++) {
                mutateIndividual(offspring.getPopulation()[i]);
            }

            population = offspring;
            population.getPopulation()[0] = bestFromPopulation;
            success = evaluatePopulation(offspring);
            generations++;
        }

    }

    private static void mutateIndividual(Candidate candidate) {
        int[] candidateTemp = new int[candidate.getBinaryEncoding().length];
        System.arraycopy(candidate.getBinaryEncoding(), 0, candidateTemp, 0, encodingLength);

        //Using mutationService probability to decide whether to mutate this candidate
        boolean mutateThisCandidate = random.nextDouble() <= mutationProbability;

        if (mutateThisCandidate) {
            int indexToMutate = random.nextInt(encodingLength);

            int valueOfIndex = candidateTemp[indexToMutate];
            //Bitwise flip of first offspring.
            if (valueOfIndex == 0) {
                candidateTemp[indexToMutate] = 1;
            } else if (valueOfIndex == 1) {
                candidateTemp[indexToMutate] = 0;
            }

            candidate.setBinaryEncoding(candidateTemp);

            //TODO: this is a fitness preference heuristic is this allowed?
            //Only mutate if it provides higher fitness
            //if (mutationService.isFitnessHigherPostMutation(candidateTemp, candidate)) {
            //candidate.setBinaryEncoding(candidateTemp);
            //}
        }
    }


    private static Candidate getBestFromParents(Candidate[] parents) {

        double fitnessOfParent1 = evaluateCandidate(parents[0]);
        double fitnessOfParent2 = evaluateCandidate(parents[1]);

        if (fitnessOfParent1 > fitnessOfParent2) {
            return parents[0];
        } else {
            return parents[1];
        }

    }

    public static double evaluateCandidate(Candidate candidate) {
        double fitness = 0;
        int[] genes = candidate.getBinaryEncoding();

        for (int val : genes) {
            if (val == 1) {
                fitness++;
            }
        }

        fitness = fitness / encodingLength * 100;
        candidate.setFitness(fitness);
        return fitness;
    }

    private static void crossOverParentsAtIndex(Population population, int parent1Index, int parent2Index) {
        int crossoverPoint = random.nextInt(encodingLength);
        int[] firstParentTemp = new int[encodingLength];
        int[] secondParentTemp = new int[encodingLength];

        System.arraycopy(population.getPopulation()[parent1Index].getBinaryEncoding(), 0, firstParentTemp, 0, encodingLength);
        System.arraycopy(population.getPopulation()[parent2Index].getBinaryEncoding(), 0, secondParentTemp, 0, encodingLength);

        for (int i = crossoverPoint; i < encodingLength; i++) {
            population.getPopulation()[parent1Index].getBinaryEncoding()[i] = secondParentTemp[i];
            population.getPopulation()[parent2Index].getBinaryEncoding()[i] = firstParentTemp[i];
        }
    }

    private static Candidate[] selectParents(Population population) {
        Candidate[] toReturn = new Candidate[2];
        int populationSize = population.getPopulation().length;

        for (int i = 0; i < 2; i++) {
            int randomInt = random.nextInt(populationSize);
            toReturn[i] = population.getPopulation()[randomInt];
        }
        return toReturn;
    }

    private static boolean evaluatePopulation(Population population) {
        int populationFitness = 0;

        for (int i = 0; i < population.getPopulation().length; i++) {
            populationFitness += evaluateCandidate(population.getPopulation()[i]);
        }

        populationFitness = populationFitness / populationSize;

        if (populationFitness == 100) {
            System.out.println("Population fitness at 100%, solution found in " + generations + " generations.");
            return true;
        } else {
            System.out.println("population fitness = " + populationFitness + "%");
            return false;
        }
    }
}
