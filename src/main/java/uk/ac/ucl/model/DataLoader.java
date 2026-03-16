package uk.ac.ucl.model;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Path;

/**
 * A class that loads the core CSV files into memory as {@link DataFrame}s.
 */
public class DataLoader implements Closeable {

    private Reader fileReader;
    private DataFrame data;
    private final CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .build();

    // Creates and loads data. Static method to emphasise that the class is instantiated and data is loaded in one step.
    public static DataLoader loadFromFile(Path path) throws IOException {
        final DataLoader dataLoader = new DataLoader();
        dataLoader.fileReader = new FileReader(path.toFile());
        dataLoader.data = new DataFrame();
        try (CSVParser parser = dataLoader.csvFormat.parse(dataLoader.fileReader)){
            for (String name : parser.getHeaderNames()) {
                dataLoader.data.addColumn(new Column(name)); // initialise empty columns
            }
            for (CSVRecord record : parser) { // for every row, add column values to corresponding columns
                dataLoader.data.forEach(column -> column.addRowValue(record.get(column.getName())));
            }
        } catch (IllegalArgumentException e) {
            throw new IOException(e.getMessage());
        }
        return dataLoader;
    }

    @Override
    public void close() throws IOException {
        fileReader.close();
    }

    public DataFrame getDataFrame() {
        return data;
    }
}
