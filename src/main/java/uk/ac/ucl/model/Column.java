package uk.ac.ucl.model;

import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private final String name;
    private final List<String> rows;
    public Column(String name) {
        this.name = name;
        rows = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public int getSize() {
        return rows.size();
    }
    public String getRowValue(int n) {
        return rows.get(n);
    }
    public void setRowValue(int n, @Nullable String value) {
        rows.set(n, value);
    }
    public void addRowValue(@Nullable String value) {
        rows.add(value);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Column c) && c.name.equals(name);
    }
    @Override
    public String toString() {
        return "Column [name=" + name + ", row size=" + rows.size() + "]";
    }
}
