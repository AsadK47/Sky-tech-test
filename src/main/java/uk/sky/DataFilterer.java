package uk.sky;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DataFilterer {
    private static final String REQUEST = "REQUEST";

    public static Collection<String> filterByCountry(BufferedReader source, String country) throws IOException {
        String line = source.readLine();
        Collection<String> collection = new ArrayList<>();
        while (line != null) {
            String[] attributes = line.split(",");
            if (line.startsWith(REQUEST)) {
                collection.add(line);
            }
            if (line.contains(country)) {
                collection.add(String.join(",", attributes));
            }
            line = source.readLine();
        }
        return collection;
    }

    public static Collection<String> filterByCountryWithResponseTimeAboveLimit(BufferedReader source, String country, long limit) throws IOException {
        String line = source.readLine();
        Collection<String> collection = new ArrayList<>();
        while (line != null) {
            String[] attributes = line.split(",");
            if (line.startsWith(REQUEST)) {
                collection.add(line);
            } else if (limit < Integer.parseInt(attributes[2]) && line.contains(country)) {
                collection.add(String.join(",", attributes));
            }
            line = source.readLine();
        }
        return collection;
    }

    public static Collection<String> filterByResponseTimeAboveAverage(BufferedReader source) throws IOException {
        String line = source.readLine();
        ArrayList<String[]> arrayOfValues = new ArrayList<>();
        Collection<String> collection = new ArrayList<>();
        double total = 0;
        int counter = 0;

        while (line != null) {
            String[] attributes = line.split(",");
            if (line.startsWith(REQUEST)) {
                collection.add(line);
            } else {
                counter++;
                total += Integer.parseInt(attributes[2]);
                arrayOfValues.add(attributes);
            }
            line = source.readLine();
        }

        if (counter != 0) {
            double average = total / counter;
            arrayOfValues.toArray();

            for (String[] array : arrayOfValues) {
                if (average < Integer.parseInt(array[2])) {
                    collection.add(String.join(",", array));
                }
            }
        }

        return collection;
    }
}