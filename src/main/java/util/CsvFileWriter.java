package util;

import java.io.FileWriter;

import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class CsvFileWriter {

        //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "mean fitness,best fitness,generations";

    public static void writeCsvFile(String fileName, ArrayList<ArrayList<String>> csvFiles) {

        //Create new students object
        FileWriter fileWriter = null;

        try {

            fileWriter = new FileWriter(fileName);

            //Write the CSV file header

            fileWriter.append(FILE_HEADER.toString());

            //Add a new line separator after the header

            fileWriter.append(NEW_LINE_SEPARATOR);

            for (ArrayList<String> list : csvFiles) {
                for (String s : list) {
                    fileWriter.append(s).append(COMMA_DELIMITER);
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            } catch (IOException e1) {
            e1.printStackTrace();
        }


        System.out.println("CSV file was created successfully.");

            try {

                fileWriter.flush();

                fileWriter.close();

            } catch (IOException e) {

                System.out.println("Error while flushing/closing fileWriter !!!");

                e.printStackTrace();

            }



        }

    }

