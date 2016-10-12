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
    private static final double mutationProbability = 0.01;

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

            crossOverOffspring(offspring);
            mutatePopulation(offspring);
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

    private static void mutatePopulation(Population offspring) {
        for (Candidate candidate : offspring.getPopulation()) {
            for (int i = 0; i < candidate.encoding.length; i++) {
                float rand = random.nextFloat();
                boolean mutateThisGene = rand < mutationProbability;
                if (mutateThisGene){
                    int value =  candidate.encoding[i];
                    if(value == 0){
                        candidate.encoding[i]=1;
                    }else{
                        candidate.encoding[i]=0;
                    }
                }
            }
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
        int[] genes = candidate.encoding;

        for (int val : genes) {
            if (val == 1) {
                fitness++;
            }
        }
        candidate.setFitness(fitness);
        return fitness;
    }

    private static void crossOverOffspring(Population population){
        //for offspring crossover
        for (int i = 0; i < populationSize-1; i++) {
            int crossoverPoint = random.nextInt(encodingLength);
            int[] firstParentTemp = new int[encodingLength];
            int[] secondParentTemp = new int[encodingLength];

            System.arraycopy(population.getPopulation().get(i).encoding, 0, firstParentTemp, 0, encodingLength);
            System.arraycopy(population.getPopulation().get(i+1).encoding, 0, secondParentTemp, 0, encodingLength);

            for (int j = crossoverPoint; j < encodingLength; j++) {
                population.getPopulation().get(i).encoding[j] = secondParentTemp[j];
                population.getPopulation().get(i+1).encoding[j] = firstParentTemp[j];
            }

            i++;
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
        return populationFitness;
    }
}
