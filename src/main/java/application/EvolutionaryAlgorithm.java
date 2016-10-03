package application;

import model.Candidate;
import model.Population;
import process.Mutation;

import java.util.Random;

public class EvolutionaryAlgorithm {

    private static final Random random = new Random();
    private static final int populationSize = 10;
    public static final int encodingLength = 16;
    private static final double mutationProbability = 0.015;
    private static int generations = 1;
    private static Mutation mutation = new Mutation();


    public static void main(String[] args) {


        Population population = new Population(populationSize, encodingLength, 2, false);
        boolean success = evaluate(population);

        //do until exit condition met
        while (generations < 1000 && !success) {
            Population offspring = new Population(populationSize, encodingLength, 2, true);

            //Create offspring using selection
            for (int i = 0; i < populationSize; i++) {

                //select parents
                Candidate[] parents = selectParents(population);

                //recombine these parents
                Candidate[] crossedOverOffspring = crossoverParents(parents);

                //Mutate offspring
                Candidate[] mutatedOffSpring = mutateOffspring(crossedOverOffspring);

                //select individuals for next population
                Candidate bestFromParents = getBestFromParents(mutatedOffSpring);

                //Fill up to population size with offspring obtained from above process
                offspring.getPopulation()[i] = bestFromParents;
            }

            success = evaluate(offspring);

            population = offspring;

            generations++;

        }


    }

    private static Candidate[] mutateOffspring(Candidate[] crossedOverOffspring) {

        int[] firstParentTemp = new int[crossedOverOffspring[0].getBinaryEncoding().length];
        int[] secondParentTemp = new int[crossedOverOffspring[1].getBinaryEncoding().length];

        for (int i = 0; i < crossedOverOffspring[0].getBinaryEncoding().length; i++) {
            firstParentTemp[i] = crossedOverOffspring[0].getBinaryEncoding()[i];
        }

        for (int i = 0; i < crossedOverOffspring[1].getBinaryEncoding().length; i++) {
            secondParentTemp[i] = crossedOverOffspring[1].getBinaryEncoding()[i];
        }

        //Using mutation probability to decide whether to mutate this candidate
        boolean mutateThisCandidate = random.nextInt(100) <= mutationProbability * 100;

        if (mutateThisCandidate) {
            int indexToMutate = random.nextInt(encodingLength);
            //Bitwise flip of first offspring.
            if (firstParentTemp[indexToMutate] == 0) {
                firstParentTemp[indexToMutate] = 1;
            }else if (firstParentTemp[indexToMutate] == 1) {
                firstParentTemp[indexToMutate]= 0;
            }

            //Only mutate if it provides higher fitness
            if(mutation.isFitnessHigherPostMutation(firstParentTemp,crossedOverOffspring[0])){
                crossedOverOffspring[0].setBinaryEncoding(firstParentTemp);
            }

        }

        mutateThisCandidate = random.nextInt(100) <= mutationProbability * 100;

        if (mutateThisCandidate) {
            int indexToMutate = random.nextInt(encodingLength);
            //Bitwise flip of first offspring.
            if (secondParentTemp[indexToMutate] == 0) {
                secondParentTemp[indexToMutate] = 1;
            }else if (secondParentTemp[indexToMutate] == 1) {
                secondParentTemp[indexToMutate]= 0;
            }

            //Only mutate if it provides higher fitness
            if(mutation.isFitnessHigherPostMutation(secondParentTemp,crossedOverOffspring[1])){
                crossedOverOffspring[1].setBinaryEncoding(firstParentTemp);
            }
        }

        return crossedOverOffspring;
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

        try {
            int[] genes = candidate.getBinaryEncoding();

            for (int i = 0; i < genes.length; i++) {
                int val = genes[i];
                if (val == 1) {
                    fitness++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fitness = fitness / encodingLength * 100;

        candidate.setFitness(fitness);

        return fitness;
    }

    private static Candidate[] crossoverParents(Candidate[] recombinedParents) {


        int crossoverPoint = random.nextInt(encodingLength);

        int[] firstParentTemp = new int[recombinedParents[0].getBinaryEncoding().length];
        int[] secondParentTemp = new int[recombinedParents[1].getBinaryEncoding().length];

        for (int i = 0; i < recombinedParents[0].getBinaryEncoding().length; i++) {
            firstParentTemp[i] = recombinedParents[0].getBinaryEncoding()[i];
        }

        for (int i = 0; i < recombinedParents[1].getBinaryEncoding().length; i++) {
            secondParentTemp[i] = recombinedParents[1].getBinaryEncoding()[i];
        }

        for (int i = crossoverPoint; i < encodingLength; i++) {
            recombinedParents[0].getBinaryEncoding()[i] = secondParentTemp[i];
            recombinedParents[1].getBinaryEncoding()[i] = firstParentTemp[i];
        }


        return recombinedParents;
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

    private static boolean evaluate(Population population) {
        int populationFitness = 0;

        for (int i = 0; i < population.getPopulation().length; i++) {
            populationFitness += evaluateCandidate(population.getPopulation()[i]);
        }

        populationFitness = populationFitness / populationSize;

        if (populationFitness == 100) {
            System.out.println("Population fitness at 100% solution found in " + generations + " generations");
            return true;
        } else {
            System.out.println("population fitness = " + populationFitness + "%");
            return false;
        }
    }


}
