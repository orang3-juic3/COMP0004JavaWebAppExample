package uk.ac.ucl.model;

public enum HospitalDataType {
    GENERAL("General Patient Information"),
    ALLERGIES("Allergies"),
    TRANSIENT("");
    private final String readableName;
    HospitalDataType(String readableName) {
        this.readableName = readableName;
    }
    public String toReadableName() {
        return readableName;
    }
}
