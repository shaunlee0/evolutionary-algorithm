package application;

import model.Candidate;
import model.Population;

import java.util.Random;

public class EvolutionaryAlgorithm {

    private static final Random random = new Random();
    private static final int populationSize = 100;
    private static final int encodingLength = 16;


    public static void main(String[] args) {

        boolean success = false;

        Population population = new Population(populationSize, encodingLength, 2, false);

        int runs = 0;

        //do until exit condition met
        while (runs < 10) {
            Population offspring = new Population(populationSize, encodingLength, 2, true);

            //Create offspring using selection
            for (int i = 0; i < populationSize; i++) {

                //select parents
                Candidate[] parents = selectParents(population);

                Candidate bestFromParents = getBestFromParents(parents);

                offspring.getPopulation()[i] = bestFromParents;

            }

            evaluate(offspring);

            population = offspring;

            runs++;

            //select parents
            Candidate[] parents = selectParents(population);

            //recombine these parents
            recombineParents(parents);

            //mutate the offspring

            //evaluate the new candidates

            //select individuals for next population

        }


    }

    private static Candidate getBestFromParents(Candidate[] parents) {
        Candidate parent1 = parents[0];
        Candidate parent2 = parents[1];

        int fitnessOfParent1 = evaluateCanidate(parent1);
        int fitnessOfParent2 = evaluateCanidate(parent2);

        if (fitnessOfParent1 > fitnessOfParent2) {
            return parent1;
        } else {
            return parent2;
        }

    }

    private static int evaluateCanidate(Candidate candidate) {
        int fitness = 0;

        try {
            int[] genes = candidate.getBinaryEncoding();

            for (int i = 0; i < genes.length; i++) {
                if (genes[i] == 1) {
                    fitness++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        candidate.setFitness(fitness);

        return fitness;
    }

    private static Candidate[] recombineParents(Candidate[] recombinedParents) {
        Candidate[] toReturn = new Candidate[2];

        return toReturn;
    }

    private static Candidate[] selectParents(Population population) {
        Candidate[] toReturn = new Candidate[2];

        int populationSize = population.getPopulation().length;

        for (int i = 0; i < 2; i++) {
            int randomInt = random.nextInt(populationSize);
            toReturn[i] = population.getPopulation()[randomInt];
            if (toReturn[i] == null) {
                System.out.println("found null");
            }
        }

        return toReturn;
    }

    private static void evaluate(Population population) {
        int populationFitness = 0;

        for (int i = 0; i < population.getPopulation().length; i++) {
            populationFitness += evaluateCanidate(population.getPopulation()[i]);
        }

        System.out.println("population fitness = " + populationFitness);
    }


}
