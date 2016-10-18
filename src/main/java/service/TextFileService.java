package service;

import model.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
                String[] splitLine = line.split(" ");
                int[] rules = convertStringToIntArray(splitLine[0]);
                int actual = Integer.parseInt(splitLine[1]);
                Data data = new Data(rules,actual);
                inputData.add(data);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputData;
    }

    private int[] convertStringToIntArray(String s) {
        int[] results = new int[s.length()];

        for (int i = 0; i < s.length(); i++) {
            results[i] = Integer.parseInt(String.valueOf(s.charAt(i)));
        }

        return results;
    }

}
