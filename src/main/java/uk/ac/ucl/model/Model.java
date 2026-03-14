package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;
import uk.ac.ucl.main.Main;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Model {

    private static volatile Model instance = null;
    private final Map<HospitalDataType, DataFrame> coreFrames = new ConcurrentHashMap<>();


    private Model() {}
    // https://en.wikipedia.org/wiki/Double-checked_locking#Usage_in_Java
    public static Model getInstance() {
        Model ref = instance;
        if (ref == null) {
            synchronized (Model.class) {
                ref = instance;
                if (ref == null) {
                    instance = ref = new Model();
                }
            }
        }
        return ref;
    }

    public DataFrame getFrame(HospitalDataType type) {
        if (type == HospitalDataType.TRANSIENT) {
            throw new UnsupportedOperationException("Cannot load data from a file for a transient DataFrame!");
        }
        return coreFrames.computeIfAbsent(type, t -> {
            try (DataLoader d = DataLoader.loadFromFile(Main.DATA_TYPE_PATH_MAP.get(t))) {
                return d.getDataFrame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Searches a DataFrame for a term. Returns the results as a new DataFrame
    public DataFrame searchDataFrame(@Nonnull HospitalDataType dataType, String searchTerm, StringMatcher stringMatcher) throws IOException {
        if (dataType == HospitalDataType.TRANSIENT || dataType == HospitalDataType.SEARCH) {
            throw new IllegalArgumentException("Cannot search data from a file for a transient or search DataFrame!");
        }
        return searchDataFrame(getFrame(dataType), searchTerm, stringMatcher);

    }
    public DataFrame searchDataFrame(@Nonnull DataFrame sourceDataFrame, String searchTerm, StringMatcher stringMatcher) {
        if (searchTerm.isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty!");
        }
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
