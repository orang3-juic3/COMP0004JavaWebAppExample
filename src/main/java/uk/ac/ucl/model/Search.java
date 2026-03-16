package uk.ac.ucl.model;

import java.nio.charset.StandardCharsets;
import java.util.*;
/**
 * The final immutable search object that can be used as a key in a Map for caching.
 */
public class Search {
    private final List<SearchComponent> components;
    private final HospitalDataType type;

    protected Search(List<SearchComponent> components, HospitalDataType type) {
        this.components = components;
        this.type = type;
    }

    public DataFrame execute() {
        final Model model = Model.getInstance();
        final DataFrame cachedFrame = model.getSearchFrame(this);
        if (cachedFrame != null ) {
            return cachedFrame;
        }
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
                if (columnNames.stream().anyMatch(name -> (component.getMatcher())
                        .isMatch(component.getSearchTerm(), tempCurrentFrame.getValue(name, tempI)))) {
                    result.getColumnNames()
                            .forEach(name -> result.addValue(name, tempCurrentFrame.getValue(name, tempI)));
                }
            }
            currentFrame = result;
        }
        model.cacheSearchFrame(this, currentFrame);
        return currentFrame;
    }

    /* Creates a base64 string representation of this object. This string is then passed in url parameters to preserve
    state where necessary.
    To prevent delimiter mangling, each query and column name is encoded to base64 before being delimited.
    To prevent URL encoding from replacing delimiters, the final string is encoded again to base64
    */
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.type.name());
        // so that the illegal character = is not included in URLS
        Base64.Encoder b64Encoder = Base64.getUrlEncoder().withoutPadding();
        for (SearchComponent component : components) {
            sb.append("|");
            String matcherName = (component.getMatcher() == StringMatcher.EXACT) ? "EXACT" : "DEFAULT";
            sb.append(matcherName).append(",");
            String b64Term = b64Encoder.encodeToString(component.getSearchTerm().getBytes(StandardCharsets.UTF_8));
            sb.append(b64Term);
            for (String col : component.getColumns()) {
                String b64Col = b64Encoder.encodeToString(col.getBytes(StandardCharsets.UTF_8));
                sb.append(",").append(b64Col);
            }
        }
        return b64Encoder.encodeToString(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
    // Reconstructs a search object from the string representation
    public static Search deserialize(String base64UrlString) {
        if (base64UrlString == null || base64UrlString.isEmpty()) {
            throw new IllegalArgumentException("Serialized string cannot be empty!");
        }
        Base64.Decoder b64Decoder = Base64.getUrlDecoder();
        byte[] decodedBytes = b64Decoder.decode(base64UrlString);
        String serializedStr = new String(decodedBytes, StandardCharsets.UTF_8);
        String[] parts = serializedStr.split("\\|");
        HospitalDataType type = HospitalDataType.valueOf(parts[0]);
        List<SearchComponent> parsedComponents = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String[] compParts = parts[i].split(",");
            SearchComponent comp = new SearchComponent();
            comp.setMatcher(compParts[0].equals("EXACT") ? StringMatcher.EXACT : StringMatcher.DEFAULT);
            String decodedTerm = new String(b64Decoder.decode(compParts[1]), StandardCharsets.UTF_8);
            comp.setSearchTerm(decodedTerm);
            List<String> cols = new ArrayList<>();
            for (int j = 2; j < compParts.length; j++) {
                String decodedCol = new String(b64Decoder.decode(compParts[j]), StandardCharsets.UTF_8);
                cols.add(decodedCol);
            }
            comp.setColumns(cols);
            parsedComponents.add(comp);
        }

        return new Search(parsedComponents, type);
    }
    protected HospitalDataType getType() {
        return type;
    }
    protected List<SearchComponent> getComponents() {
        return components;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Search search = (Search) o;
        return type == search.type && Objects.equals(components, search.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(components, type);
    }
}
