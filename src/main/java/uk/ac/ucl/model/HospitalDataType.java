package uk.ac.ucl.model;

public enum HospitalDataType {
    GENERAL("General Patient Information"),
    ALLERGIES("Allergies"),
    CAREPLANS("Care Plans"),
    CONDITIONS("Conditions"),
    ENCOUNTERS("Encounters"),
    IMAGINGSTUDIES("Imaging Studies"),
    IMMUNIZATIONS("Immunizations"),
    MEDICATIONS("Medications"),
    OBSERVATIONS("Observations"),
    PROCEDURES("Procedures"),
    TRANSIENT("");
    private final String readableName;
    HospitalDataType(String readableName) {
        this.readableName = readableName;
    }
    public String toReadableName() {
        return readableName;
    }
}
