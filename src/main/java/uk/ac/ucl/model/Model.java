package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    private static Model instance = null;
    private final Map<HospitalDataType, DataFrame> frames = new HashMap<>();
    // todo replace


    private Model() {}
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public DataFrame getFrame(HospitalDataType type) throws IOException {
        if (type == HospitalDataType.TRANSIENT) {
            throw new UnsupportedOperationException("Cannot load data from a file for a transient DataFrame!");
        }
        if (!frames.containsKey(type)) {
            try (DataLoader d = DataLoader.loadFromFile(dataPathMapping.get(type))) {
                frames.put(type, d.getDataFrame());
                return d.getDataFrame();
            }
        }
        return frames.get(type);
    }

    // Searches a DataFrame for a term. Returns the results as a new DataFrame
    public DataFrame searchDataFrame(@Nonnull HospitalDataType dataType, String searchTerm, StringMatcher stringMatcher) throws IOException {
        if (dataType == HospitalDataType.TRANSIENT) {
            throw new IllegalArgumentException("Cannot search data from a file for a transient DataFrame!");
        }
        if (searchTerm.isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty!");
        }
        final DataFrame sourceDataFrame = getFrame(dataType);
        final DataFrame result = DataFrame.withColumnNames(sourceDataFrame);
        final List<String> columnNames = sourceDataFrame.getColumnNames();
        final int rowCount = sourceDataFrame.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            final int tempI = i;
            // If any members of the row contain the string add the entire row to the DataFrame result
            // TODO This silently fails if column size mismatch
            if (columnNames.stream().anyMatch(name -> stringMatcher.isMatch(searchTerm, sourceDataFrame.getValue(name, tempI)))) {
                columnNames.forEach(name -> result.addValue(name, sourceDataFrame.getValue(name, tempI)));
            }
        }
        return result;
    }

}
