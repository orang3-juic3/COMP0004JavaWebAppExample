package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class facilitates programmatically creating searches and chaining them. The vast majority of operations
 * performed on data by the server involves searching so this is an important class.
 */
public class SearchBuilder {
    private final List<SearchComponent> components = new ArrayList<>();
    private SearchComponent current;
    private final HospitalDataType dataType;

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

    public SearchBuilder usingMatcher(@Nonnull StringMatcher matcher) {
        if (current == null) {
            throw new IllegalArgumentException("No query specified!");
        }
        current.setMatcher(matcher);
        return this;
    }

    public SearchBuilder includingColumns(@Nonnull String... columnNames) {
        if (current == null) {
            throw new IllegalArgumentException("No query specified!");
        }
        current.setColumns(List.of(columnNames));
        return this;
    }
    // Shorthand for searching for an exact match in one column
    public SearchBuilder matchingExactly(@Nonnull String column, @Nonnull String value) {
        addQuery(value).includingColumns(column).usingMatcher(StringMatcher.EXACT);
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
