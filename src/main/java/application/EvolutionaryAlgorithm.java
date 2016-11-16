package application;

import model.*;
import service.TextFileService;
import util.CsvFileWriter;

import java.util.*;
import java.util.stream.Collectors;

public class EvolutionaryAlgorithm {

    private static int generations = 0;
    private static final Random random = new Random();

    //GA Constants.
    private static final int populationSize = 100;
    private static final int encodingLength = 130;
    private static double mutationProbability = 0.001;
    private static double crossoverProbability = 0.9;
    private static int tournamentSize = 6;
    private static double mutationBound = 0.4;

    private static TextFileService textFileService = new TextFileService();
    private static ArrayList<Data> data = textFileService.getDataFromTextFile("data3.txt");
    private static ArrayList<Data> test = textFileService.getDataFromTextFile("data3-test.txt");
    private static int mutationAdditions = 0;
    private static int mutationReductions = 0;



    public static void main(String[] args) {
        System.out.println("Training set size : " + data.size());
        System.out.println("Test set size : " + test.size());
        ArrayList<ArrayList<String>> audit = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            generations = 0;
            int stuckInLocalOptimaCounter = 0;
            int mutationAdditions = 0;

            int totalPossibleFitness = data.size();
            Candidate bestCandidate = new Candidate(encodingLength, false);
            Population population = new Population(populationSize, encodingLength);
            population.initialise();
            System.out.println("initial fitness = " + evaluatePopulation(population));
            ArrayList<String> auditValues = new ArrayList<>();


            boolean success = false;


            while (!success) {

                Population offspring = performSelection(population);
                offspring = mutatePopulation(offspring);
                offspring = crossOverOffspring(offspring);
                evaluatePopulation(offspring);
                population.clear();
                population.fill(offspring.getPopulation());
                offspring.clear();
                population = performSelection(population);

                //Use best
                success = bestCandidate.getFitness() == 2000;
                generations++;

                if (bestCandidate.getFitness() < population.getBestCandidate().getFitness()) {
                    bestCandidate = population.getBestCandidate();
                    stuckInLocalOptimaCounter = 0;
                }else{
                    stuckInLocalOptimaCounter ++;
                    System.out.println("Stuck in local optima for " + stuckInLocalOptimaCounter + " generations");
                }

                if (bestCandidate.getFitness() > population.getBestCandidate().getFitness()) {

                    //Elitism method.
//                    population.getPopulation().remove(population.getWorstFromPopulation());

                    //Non-elitist method.
                    population.getPopulation().remove(random.nextInt(populationSize));
                    population.getPopulation().add(new Candidate(bestCandidate));
                }

                if((stuckInLocalOptimaCounter > 100) && (mutationAdditions <= 3)){
                    mutationProbability += 0.01;
                    mutationAdditions ++;
                    stuckInLocalOptimaCounter = 0;
                    System.out.println("Mutation additions counter = " + mutationAdditions);
                    System.out.println("Mutation rate = " + mutationProbability);
                }else if((stuckInLocalOptimaCounter > 100) && (mutationAdditions >= 3) && (mutationReductions <=3)){
                    mutationProbability -= 0.01;
                    mutationReductions ++;
                    stuckInLocalOptimaCounter = 0;
                    System.out.println("Mutation reductions counter = " + mutationReductions);
                    System.out.println("Mutation rate = " + mutationProbability);
                }

                if((mutationReductions > 3) && (mutationAdditions > 3)){
                    mutationAdditions = 0;
                    mutationReductions = 0;
                    mutationProbability = 0.01;
                }

                System.out.println("Best = " + bestCandidate.getFitness());
                System.out.println("Total fitness = " + evaluatePopulation(population));
                System.out.println("Generations = " + generations);

            }

            auditValues.add((String.valueOf((evaluatePopulation(population)))));
            bestCandidate.extractRules();
            auditValues.add(String.valueOf(bestCandidate.getFitness()));
            auditValues.add(String.valueOf(generations));


            audit.add(auditValues);

            System.out.println("End of loop at : " + (float) evaluatePopulation(population) + " out of possible " + totalPossibleFitness);
//            bestCandidate.extractRules();
            System.out.println("best individual has fitness of " + bestCandidate.getFitness()  + " out of possible " + totalPossibleFitness);
            System.out.println("Generations " + generations);
            findRuleFitness(bestCandidate.getRules());
            Set<Rule> bestRules = new HashSet<>(bestCandidate.rules);
            Set<Rule> outputOfOne = bestRules.stream().filter(rule -> rule.getActual() == 1).collect(Collectors.toSet());
            Set<Rule> outputOfZero = bestRules.stream().filter(rule -> rule.getActual() == 0).collect(Collectors.toSet());

            System.out.println("\nOutput of One");
            outputOfOne.forEach(rule -> {
                System.out.println(rule.toString());
            });

            System.out.println("\nOutput of Zero");
            outputOfZero.forEach(rule -> {
                System.out.println(rule.toString());
            });

        }
            String csv = "/home/shaun/Desktop/ga.csv";
            CsvFileWriter.writeCsvFile(csv, audit);
    }

    private static void findRuleFitness(ArrayList<Rule> rules) {

        //Loop through data
        for (Data dataElement : data) {
            //Loop through candidates rules
            for (Rule rule : rules) {
                if (compareArrays(rule.getConditions(), dataElement.getConditions())) {
                    int ruleOneActual = (int) rule.getActual();
                    int dataElementOutput = (int) dataElement.getOutput();
                    if (ruleOneActual==dataElementOutput) {
                        rule.setFitness(rule.getFitness()+1);
                    }
                }
            }
        }

    }

    private static Population performSelection(Population population) {
        Population offspring = new Population(populationSize, encodingLength);
        for (int i = 0; i < populationSize; i++) {
            //select parents
            Candidate[] tournamentCandidates = selectTournamentCandidates(population,tournamentSize);

            //select individuals for next population
            Candidate bestFromTournament = getBestFromTournament(tournamentCandidates);

            //Fill up to population size with offspring obtained from above service
            offspring.getPopulation().add(bestFromTournament);
        }
        return offspring;
    }

    public static Population mutatePopulation(Population offspring) {
        for (Candidate candidate : offspring.getPopulation()) {
            for (int i = 0; i < candidate.encoding.length; i++) {
                float rand = random.nextFloat();
                boolean mutateThisGene = rand <= mutationProbability;
                if (mutateThisGene) {
                    //If output
                    if (!((i + 1) % 13 == 0)) {

                        double mutationChange = random.nextDouble();

                        //Mutate this gene
                        while(!(mutationChange < mutationBound)){
                            mutationChange = random.nextDouble();
                        }
                        boolean addChange = random.nextBoolean();

                        if (addChange){
                            candidate.encoding[i] = candidate.encoding[i] + mutationChange;
                        }else{
                            candidate.encoding[i] = candidate.encoding[i] - mutationChange;
                        }

                        if(candidate.encoding[i] < 0.0){
                            candidate.encoding[i] = 0.0;
                        }else if (candidate.encoding[i] > 1.0){
                            candidate.encoding[i] = 1.0;
                        }

                    }
                }
            }
        }
        return offspring;
    }

    private static Candidate getBestFromTournament(Candidate[] parents) {
        Candidate fittestIndividual = null;

        for (Candidate candidate : parents) {
            if (fittestIndividual == null) {
                fittestIndividual = candidate;
            } else if (candidate.getFitness() > fittestIndividual.getFitness()) {
                fittestIndividual = candidate;
            }
        }

        return new Candidate(fittestIndividual);
    }

    public static double evaluateCandidate(Candidate candidate) {
        double fitness = 0;
        candidate.extractRules();
        ArrayList<Rule> candidateRules = candidate.getRules();

        if (candidateRules == null) {
            System.out.println("Rules are null");
        }
        //Loop through data
        for (Data dataElement : data) {
            //Loop through candidates rules
            for (int j = 0; j < candidateRules.size(); j++) {
                Rule rule = candidateRules.get(j);
                if (compareArrays(rule.getConditions(), dataElement.getConditions())) {
                    int ruleActual = (int) rule.getActual();
                    int dataOutput = (int) dataElement.getOutput();

                    if (ruleActual == dataOutput) {
                        fitness++;
                        rule.setFitness(rule.getFitness() + 1);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }

        candidate.setFitness(fitness);
        return fitness;
    }


    /**
     * Compares two arrays and checks the elements match, if a wildcard is found we simply count it as a match.
     *
     * @return : Boolean result of check
     */
    private static boolean compareArrays(Range[] array1, double[] array2) {
        boolean b = true;
        if (array1 != null && array2 != null) {
            //check length matches
            if (array1.length != array2.length) {
                b = false;
            } else for (int i = 0; i < array2.length; i++) {

                double evaluating = array2[i];
                double lowerBound = array1[i].getValues()[0];
                double higherBound = array1[i].getValues()[1];


                if ((evaluating <= lowerBound) || (evaluating >= higherBound)) {
                    b = false;
                    break;
                }

            }
        } else {
            b = false;
        }
        return b;
    }

    public static Population crossOverOffspring(Population offspring) {
        for (int i = 0; i < populationSize / 2 ; i+=2) {

            float rand = random.nextFloat();
            boolean crossoverParents = rand <= crossoverProbability;

            if (crossoverParents) {
                int crossoverPoint = random.nextInt(encodingLength);
                double[] firstParentTemp = new double[encodingLength];
                double[] secondParentTemp = new double[encodingLength];

                System.arraycopy(offspring.getPopulation().get(i).encoding, 0, firstParentTemp, 0, encodingLength);
                System.arraycopy(offspring.getPopulation().get(i + 1).encoding, 0, secondParentTemp, 0, encodingLength);

                for (int j = crossoverPoint; j < encodingLength; j++) {
                    offspring.getPopulation().get(i).encoding[j] = secondParentTemp[j];
                    offspring.getPopulation().get(i + 1).encoding[j] = firstParentTemp[j];
                }
            }


        }

        return offspring;
    }

    /**
     * Return a random array of individuals from the population to use for tournament selection.
     * @param population : population used to obtain individuals
     * @param tournamentSize : size of tournament, and individuals to return.
     * @return : array of individuals selected randomly.
     */
    private static Candidate[] selectTournamentCandidates(Population population, int tournamentSize) {
        Candidate[] toReturn = new Candidate[tournamentSize];
        int populationSize = population.getPopulation().size();

        for (int i = 0; i < tournamentSize; i++) {

//          Tournament selection
            int randomInt = random.nextInt(populationSize);
            toReturn[i] = population.getPopulation().get(randomInt);
//            Roulette wheel selection
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

    private static float evaluatePopulation(Population population) {
        int populationFitness = 0;

//        Incorrect mapping rules against data rather than the opposite
        for (int i = 0; i < population.getPopulation().size(); i++) {
            populationFitness += evaluateCandidate(population.getPopulation().get(i));
        }

        return (float) populationFitness / (float) populationSize;
    }


    private static int evaluatePopulationGetMean(Population population) {
        int populationFitness = 0;

        for (int i = 0; i < population.getPopulation().size(); i++) {
            populationFitness += evaluateCandidate(population.getPopulation().get(i));
        }
        return populationFitness / populationSize;
    }
}
