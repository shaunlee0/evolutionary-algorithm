package application;

import model.Candidate;
import model.Population;
import service.MutationService;
import util.CsvFileWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

public class EvolutionaryAlgorithm {

    private static int generations = 1;
    private static final Random random = new Random();
    private static MutationService mutationService = new MutationService();

    //GA Constants.
    private static final int populationSize = 50;
    public static final int encodingLength = 50;
    private static final double mutationProbability = 0.001;

    public static void main(String[] args) {
        CsvFileWriter csvFileWriter = new CsvFileWriter();
        Population population = new Population(populationSize, encodingLength);
        population.initialise();

        ArrayList<ArrayList<String>> audit = new ArrayList<ArrayList<String>>();

        boolean success = evaluatePopulation(population);

        while (generations < 50 && !success) {



            evaluatePopulation(population);
            Candidate bestFromPopulation = population.getBestCandidate();

            Population offspring = new Population(populationSize, encodingLength);

            //Create offspring using selection
            for (int i = 0; i < populationSize; i++) {

                //select parents
                Candidate[] parents = selectParents(population);

                //select individuals for next population
                Candidate bestFromParents = getBestFromParents(parents);

                //Fill up to population size with offspring obtained from above service
                offspring.getPopulation().add(bestFromParents);
            }

            //for offspring crossover
            for (int i = 0; i < populationSize; i++) {
                crossOverParentsAtIndex(offspring, i, i + 1);
                i++;
            }

            //Mutate on a given probability
            for (int i = 0; i < populationSize; i++) {
                mutateIndividual(offspring.getPopulation().get(i));
            }

            evaluatePopulation(offspring);
            population.clear();
            population.fill(offspring.getPopulation());
            population.getPopulation().add(population.getWorstFromPopulation(),bestFromPopulation);
            success = evaluatePopulation(population);
            generations++;

            ArrayList<String> auditValues = new ArrayList<String>();
            auditValues.add((String.valueOf(evaluatePopulationGetMean(population))));
            auditValues.add(String.valueOf(bestFromPopulation.getFitness()));
            auditValues.add(String.valueOf(generations));

            audit.add(auditValues);
        }

        String csv = "/home/shaun/Desktop/ga.csv";

        CsvFileWriter.writeCsvFile(csv,audit);


    }

    private static void mutateIndividual(Candidate candidate) {


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

        System.arraycopy(population.getPopulation().get(parent1Index).getBinaryEncoding(), 0, firstParentTemp, 0, encodingLength);
        System.arraycopy(population.getPopulation().get(parent2Index).getBinaryEncoding(), 0, secondParentTemp, 0, encodingLength);

        for (int i = crossoverPoint; i < encodingLength; i++) {
            population.getPopulation().get(parent1Index).getBinaryEncoding()[i] = secondParentTemp[i];
            population.getPopulation().get(parent2Index).getBinaryEncoding()[i] = firstParentTemp[i];
        }
    }

    private static Candidate[] selectParents(Population population) {
        Candidate[] toReturn = new Candidate[2];
        int populationSize = population.getPopulation().size();

        for (int i = 0; i < 2; i++) {
            int randomInt = random.nextInt(populationSize);
            toReturn[i] = population.getPopulation().get(randomInt);
        }
        return toReturn;
    }

    private static boolean evaluatePopulation(Population population) {
        int populationFitness = 0;

        for (int i = 0; i < population.getPopulation().size(); i++) {
            populationFitness += evaluateCandidate(population.getPopulation().get(i));
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

    private static int evaluatePopulationGetMean(Population population) {
        int populationFitness = 0;

        for (int i = 0; i < population.getPopulation().size(); i++) {
            populationFitness += evaluateCandidate(population.getPopulation().get(i));
        }
        return populationFitness / populationSize;
    }

    public static void writeResult(int generation, int value){

    }
}
