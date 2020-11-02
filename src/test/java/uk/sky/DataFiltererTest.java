package uk.sky;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

public class DataFiltererTest {
    private final String GB = "GB";
    private final String sourceForMultiLinesFile = "src/test/resources/multi-lines";

    @Test
    public void whenEmpty() throws IOException {
        String sourceForEmptyFile = "src/test/resources/empty";
        assertTrue(DataFilterer.filterByCountry(openFile(sourceForEmptyFile), GB).isEmpty());
    }

    private BufferedReader openFile(String filename) throws IOException {
        return new BufferedReader(Files.newBufferedReader(Paths.get(filename)));
    }

    @Test
    public void checkFilterByCountryInSingleLineFile() throws IOException {
        String header = "REQUEST_TIMESTAMP,COUNTRY_CODE,RESPONSE_TIME";
        String expectedData = "1431592497,GB,200";
        String sourceForSingleLineFile = "src/test/resources/single-line";

        assertTrue(DataFilterer.filterByCountry(openFile(sourceForSingleLineFile), GB).contains(header));
        assertTrue(DataFilterer.filterByCountry(openFile(sourceForSingleLineFile), GB).contains(expectedData));
    }

    @Test
    public void checkFilterByCountryInMultiLineFile() throws IOException {
        String expectedData = "1432917066,GB,37";
        assertTrue(DataFilterer.filterByCountry(openFile(sourceForMultiLinesFile), "GB").contains(expectedData));
    }

    @Test
    public void filterByCountryWithResponseTimeAboveLimit() throws IOException {
        int limit = 500;
        String expectedData = "1433190845,US,539";
        String expectedData2 = "1433666287,US,789";
        String expectedData3 = "1432484176,US,850";
        Collection<String> expectedDataCollection = Arrays.asList(expectedData, expectedData2, expectedData3);

        String US = "US";
        assertTrue(DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                openFile(sourceForMultiLinesFile), US, limit).containsAll(expectedDataCollection));
    }

    @Test
    public void filterByResponseTimeAboveAverage() throws IOException {
        String expectedData = "1433190845,US,539";
        assertTrue(DataFilterer.filterByResponseTimeAboveAverage(openFile(sourceForMultiLinesFile)).contains(expectedData));
    }
}
