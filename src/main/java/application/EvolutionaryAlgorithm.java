package application;

import model.Candidate;
import model.Population;
import util.CsvFileWriter;

import java.util.ArrayList;
import java.util.Random;

public class EvolutionaryAlgorithm {

    private static int generations = 1;
    private static final Random random = new Random();

    //GA Constants.
    private static final int populationSize = 50;
    public static final int encodingLength = 50;
    public static final double mutationProbability = 0.01;
    public static final double crossoverProbability = 0.3;

    public static void main(String[] args) {
        int totalOnesPossible = populationSize * encodingLength;
        CsvFileWriter csvFileWriter = new CsvFileWriter();
        Population population = new Population(populationSize, encodingLength);
        population.initialise();

        ArrayList<ArrayList<String>> audit = new ArrayList<ArrayList<String>>();

        float k = (((float) evaluatePopulation(population)) / ((float) totalOnesPossible)) * 100f;
        boolean success = k == 100;


        while (generations < 500 && !success) {

            int currentFitness = evaluatePopulation(population);
            Candidate bestFromPopulation = population.getBestCandidate();
            Population offspring = performSelection(population);
            offspring = crossOverOffspring(offspring);
            offspring = mutatePopulation(offspring);
            population.clear();
            population.fill(offspring.getPopulation());
            offspring.clear();
            population.getPopulation().remove(population.getWorstFromPopulation());
            population.getPopulation().add(bestFromPopulation);

            k = (((float) evaluatePopulation(population)) / ((float) totalOnesPossible)) * 100f;
            success = k == 100;
            if (success) {
                System.out.println("here");
            }
            generations++;

            ArrayList<String> auditValues = new ArrayList<String>();
            auditValues.add((String.valueOf(evaluatePopulationGetMean(population))));
            auditValues.add(String.valueOf(bestFromPopulation.getFitness()));
            auditValues.add(String.valueOf(generations));
            audit.add(auditValues);

            System.out.println("Generation = " + bestFromPopulation.getFitness());
        }

        System.out.println("End of loop at : " + population.getBestCandidate().getFitness() + " out of possible " + totalOnesPossible / populationSize);

//        String csv = "/home/shaun/Desktop/ga.csv";
//        CsvFileWriter.writeCsvFile(csv,audit);
    }

    private static Population performSelection(Population population) {
        Population offspring = new Population(populationSize, encodingLength);
        for (int i = 0; i < populationSize; i++) {
            //select parents
            Candidate[] parents = selectParents(population);

            //select individuals for next population
            Candidate bestFromParents = getBestFromParents(parents);

            //Fill up to population size with offspring obtained from above service
            offspring.getPopulation().add(bestFromParents);
        }
        return offspring;
    }

    public static Population mutatePopulation(Population offspring) {
        int mutatedCount = 0;
        for (Candidate candidate : offspring.getPopulation()) {
            for (int i = 0; i < candidate.encoding.length; i++) {
                float rand = random.nextFloat();
                boolean mutateThisGene = rand <= mutationProbability;
                if (mutateThisGene) {
                    mutatedCount ++;
                    candidate.encoding[i] = 1 - candidate.encoding[i];
                }
            }
        }
        System.out.println("mutated " + mutatedCount + " out of possible " + populationSize * encodingLength );
        return offspring;
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
        int[] genes = candidate.encoding;

        for (int val : genes) {
            if (val == 1) {
                fitness++;
            }
        }
        candidate.setFitness(fitness);
        return fitness;
    }

    public static Population crossOverOffspring(Population offspring) {
        for (int i = 0; i < populationSize - 1; i++) {

            float rand = random.nextFloat();
            boolean crossoverParents = rand <= crossoverProbability;

            if (crossoverParents){
                int crossoverPoint = random.nextInt(encodingLength);
                int[] firstParentTemp = new int[encodingLength];
                int[] secondParentTemp = new int[encodingLength];

                System.arraycopy(offspring.getPopulation().get(i).encoding, 0, firstParentTemp, 0, encodingLength);
                System.arraycopy(offspring.getPopulation().get(i + 1).encoding, 0, secondParentTemp, 0, encodingLength);

                for (int j = crossoverPoint; j < encodingLength; j++) {
                    offspring.getPopulation().get(i).encoding[j] = secondParentTemp[j];
                    offspring.getPopulation().get(i + 1).encoding[j] = firstParentTemp[j];
                }
            }

            i++;
        }

        return offspring;
    }

    private static Candidate[] selectParents(Population population) {
        Candidate[] toReturn = new Candidate[2];
        int populationSize = population.getPopulation().size();

        for (int i = 0; i < 2; i++) {

//          Tournament selection
            int randomInt = random.nextInt(populationSize);
            toReturn[i] = population.getPopulation().get(randomInt);
//            toReturn[i] = selectIndividualUsingRouletteWheelSelection(population);
        }
        return toReturn;
    }

    private static Candidate selectIndividualUsingRouletteWheelSelection(Population population) {
        Candidate candidate = null;
        int totalSum = 0;
        for (int i = 0; i < population.getPopulation().size(); i++) {
            totalSum += evaluateCandidate(population.getPopulation().get(i));
        }
        int randNumber = random.nextInt(totalSum);
        int partialSum = 0;
        for (int j = 0; j < population.getPopulation().size(); j++) {
            partialSum += evaluateCandidate(population.getPopulation().get(j));
            if (partialSum >= randNumber) {
                candidate = population.getPopulation().get(j);
            }
        }
        return candidate;
    }

    private static int evaluatePopulation(Population population) {
        int populationFitness = 0;
        for (int i = 0; i < population.getPopulation().size(); i++) {
            populationFitness += evaluateCandidate(population.getPopulation().get(i));
        }
        return populationFitness;
    }

    private static int evaluatePopulationGetMean(Population population) {
        int populationFitness = 0;

        for (int i = 0; i < population.getPopulation().size(); i++) {
            populationFitness += evaluateCandidate(population.getPopulation().get(i));
        }
        return populationFitness;
    }
}
