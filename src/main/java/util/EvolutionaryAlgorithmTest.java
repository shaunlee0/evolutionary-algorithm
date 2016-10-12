package util;

import application.EvolutionaryAlgorithm;
import model.Candidate;
import model.Population;
import org.junit.Test;

/**
 * Created by shaun on 12/10/2016.
 */
public class EvolutionaryAlgorithmTest {

    @Test
    public void shouldCrossOverIndividuals(){
        Population population = new Population(2,4);
        Candidate candidate1 = new Candidate(4,true);
        Candidate candidate2 = new Candidate(4,true);
        population.getPopulation().add(candidate1);
        population.getPopulation().add(candidate2);
        EvolutionaryAlgorithm.crossOverOffspring(population);
        System.out.println(population);
    }

    @Test
    public void shouldMutateIndividuals(){

    }
}
