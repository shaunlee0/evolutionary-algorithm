package service;

import model.Data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Created by shaun on 18/10/16.
 */
public class TextFileService {


    public ArrayList<Data> getDataFromTextFile(String fileName) {

        ArrayList<Data> inputData =  new ArrayList<>();
        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {
            //Skip header line
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] rulesAndOutput = line.split(" ");
                double[] rules = convertStringToDoubleArray(Arrays.copyOfRange(rulesAndOutput,0,rulesAndOutput.length - 1));
                double actual = Integer.parseInt(rulesAndOutput[rulesAndOutput.length - 1]);
                Data data = new Data(rules,actual);
                inputData.add(data);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputData;
    }

    private double[] convertStringToDoubleArray(String[] strings) {
        double[] results = new double[strings.length];

        for (int i = 0; i < strings.length; i++) {
            results[i] = Double.parseDouble(strings[i]);
        }

        return results;
    }

}
