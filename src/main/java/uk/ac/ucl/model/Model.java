package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Model {
    private static Model instance = null;
    private final Map<HospitalDataType, DataFrame> frames = new HashMap<>();
    // todo replace
    private final Map<HospitalDataType, Path> dataPathMapping = Map.of(HospitalDataType.GENERAL, Path.of("data", "patients100.csv"));

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
    private static class StringComparisonUtils {
        // Strips both strings, and looks case-insensitively if either string contains the other
        private static boolean isQuasiExactMatch(String source, String target) {
            final String source2 = source.strip().toLowerCase();
            final String target2 = target.strip().toLowerCase();
            return target2.contains(source2) || source2.contains(target2);
        }
        //TODO
        private static int calculateLevenshteinDistance(String source, String target) {
            return 0;
        }
    }

    // Searches a DataFrame for a term. Returns the results as a new DataFrame
    public DataFrame searchDataFrame(@Nonnull DataFrame dataFrame, String searchTerm) {
        if (searchTerm.isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty!");
        }
        final DataFrame result = DataFrame.withColumnNames(dataFrame);
        final List<String> columnNames = dataFrame.getColumnNames();
        for (int i = 0; i < dataFrame.getRowCount(); i++) {
            final int tempI = i;
            // If any members of the row contain the string add the entire row to the DataFrame result
            // TODO This silently fails if column size mismatch
            if (columnNames.stream().anyMatch(name -> StringComparisonUtils.isQuasiExactMatch(searchTerm, dataFrame.getValue(name, tempI)))) {
                columnNames.forEach(name -> dataFrame.addValue(name, dataFrame.getValue(name, tempI)));
            }
        }
        return result;
    }

}
