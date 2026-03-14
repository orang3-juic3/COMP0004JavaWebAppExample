package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class DataFrame implements Iterable<Column> {
    private final Map<String, Column> columns = new LinkedHashMap<>(); // Order of keys is preserved, and fast lookup

    // Create a new skeleton DataFrame which creates columns in the same order and with the same name as in the argument
    public static DataFrame withColumnNames(Iterable<Column> columns) {
        final DataFrame df = new DataFrame();
        columns.forEach(it -> df.addColumn(new Column(it.getName())));
        return df;
    }

    public int getRowCount() {
        return columns.values().stream().map(Column::getSize)
                .max(Integer::compareTo) // In case there are partially filled rows
                .orElse(0); // In case there are no rows
    }


    /**
     * The addColumn method will attempt to add a column into the backing collection in the DataFrame.
     * If there is a column with the same name, this method will throw an IllegalArgumentException.
     * Furthermore, it is the controller's responsibility to ensure Column instances passed as arguments
     * are not null.
     * It is also up to the controller to decide how to handle partially filled rows.
     */
    public void addColumn(@Nonnull Column column) {
        if (columns.isEmpty()) {
            columns.put(column.getName(), column);
            return;
        }
        if (columns.containsKey(column.getName())) {
            throw new IllegalArgumentException("Column already exists: " + column);
        }
        columns.put(column.getName(), column);
    }

    public List<String> getColumnNames() {
        return new ArrayList<>(columns.keySet()); // use of LinkedHashMap ensures order.
    }

    private Column getOrThrow(@Nonnull String columnName) {
        if (!columns.containsKey(columnName)) {throw new IllegalArgumentException("No such column: " + columnName);}
        return columns.get(columnName);
    }

    public String getValue(@Nonnull String columnName, int row) {
        final Column column = getOrThrow(columnName);
        try {
            return column.getRowValue(row);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(e); // Wrap underlying exception for easier handling
        }
    }

    public void putValue(@Nonnull String columnName, int row, @Nullable String value) {
        final Column column = getOrThrow(columnName);
        column.setRowValue(row, value);
    }
    public void addValue(@Nonnull String columnName, @Nullable String value) {
        final Column column = getOrThrow(columnName);
        column.addRowValue(value);
    }

    @Override @Nonnull
    public Iterator<Column> iterator() {
        return columns.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super Column> action) {
        columns.values().forEach(action);
    }

    @Override
    public Spliterator<Column> spliterator() {
        return columns.values().spliterator();
    }
}
