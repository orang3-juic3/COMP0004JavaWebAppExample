package uk.ac.ucl.model;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// A POJO internal to the model to facilitate building searches
// Using a record does not align with the modular 'building' style
public class SearchComponent {

    private StringMatcher matcher = StringMatcher.DEFAULT;
    private String searchTerm;
    // The columns included in the search term, empty means all columns
    private List<String> columns = new ArrayList<>();

    protected SearchComponent() {}

    public StringMatcher getMatcher() {
        return matcher;
    }

    protected void setMatcher(@Nonnull StringMatcher matcher) {
        this.matcher = matcher;
    }

    protected String getSearchTerm() {
        return searchTerm;
    }

    protected void setSearchTerm(@Nonnull String searchTerm) {
        this.searchTerm = searchTerm;
    }

    protected List<String> getColumns() {
        return columns;
    }

    protected void setColumns(@Nonnull List<String> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchComponent that = (SearchComponent) o;
        return matcher == that.matcher &&
                Objects.equals(searchTerm, that.searchTerm) &&
                Objects.equals(columns, that.columns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matcher, searchTerm, columns);
    }
}
