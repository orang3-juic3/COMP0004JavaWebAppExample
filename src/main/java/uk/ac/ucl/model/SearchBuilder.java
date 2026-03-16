package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class SearchBuilder {
    private final List<SearchComponent> components = new ArrayList<>();
    private SearchComponent current;
    private HospitalDataType dataType;

    public SearchBuilder(@Nonnull HospitalDataType dataType) {
        this.dataType = dataType;
    }
    public static SearchBuilder fromSearch(@Nonnull Search search) {
        final SearchBuilder sb = new SearchBuilder(search.getType());
        sb.components.addAll(search.getComponents());
        return sb;
    }

    public SearchBuilder addQuery(@Nonnull String query) {
        if (current != null) {
            components.add(current);
        }
        current = new SearchComponent();
        current.setSearchTerm(query);
        return this;
    }

    public SearchBuilder usingMatcher(StringMatcher matcher) {
        if (current == null) {
            throw new IllegalArgumentException("No query specified!");
        }
        current.setMatcher(matcher);
        return this;
    }

    public SearchBuilder includingColumns(List<String> columnNames) {
        if (current == null) {
            throw new IllegalArgumentException("No query specified!");
        }
        current.setColumns(columnNames);
        return this;
    }

    public SearchBuilder includingColumns(String... columnNames) {
        if (current == null) {
            throw new IllegalArgumentException("No query specified!");
        }
        current.setColumns(List.of(columnNames));
        return this;
    }
    public SearchBuilder matchingExactly(String column, String value) {
        addQuery(value).includingColumns(column).usingMatcher(StringMatcher.EXACT);
        return this;
    }
    public SearchBuilder changeType(HospitalDataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public Search build() {
        if (current == null) {
            throw new IllegalArgumentException("No queries to use in search!");
        }
        components.add(current);
        return new Search(components, dataType);
    }
}
