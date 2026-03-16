package uk.ac.ucl.model;

import java.util.List;

public class Search {
    private final List<SearchComponent> components;
    private final HospitalDataType type;

    protected Search(List<SearchComponent> components, HospitalDataType type) {
        this.components = components;
        this.type = type;
    }

    public DataFrame execute() {
        final Model model = Model.getInstance();
        DataFrame currentFrame = model.getFrame(type);
        for (SearchComponent component : components) {
            final DataFrame result = DataFrame.withColumnNames(currentFrame);
            if (component.getSearchTerm().isEmpty()) {
                throw new IllegalArgumentException("Search term cannot be empty!");
            }
            final List<String> columnNames = component.getColumns().isEmpty() ? result.getColumnNames() : component.getColumns();
            final int rowCount = currentFrame.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                final int tempI = i;
                final DataFrame tempCurrentFrame = currentFrame;
                // If any members of the row contain the string add the entire row to the DataFrame result
                // TODO This silently fails if column size mismatch
                if (columnNames.stream().anyMatch(name -> (component.getMatcher())
                        .isMatch(component.getSearchTerm(), tempCurrentFrame.getValue(name, tempI)))) {
                    result.getColumnNames()
                            .forEach(name -> result.addValue(name, tempCurrentFrame.getValue(name, tempI)));
                }
            }
            currentFrame = result;
        }
        return currentFrame;
    }
}
