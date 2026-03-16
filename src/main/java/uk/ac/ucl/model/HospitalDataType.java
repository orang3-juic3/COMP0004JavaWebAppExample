package uk.ac.ucl.model;

/**
 * An enum that contains all the different types of data displayed by the website. All other DataFrames shown by the
 * website are subsets of the DataFrame corresponding to an arbitrary enum value.
 */
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
    PROCEDURES("Procedures");
    private final String readableName;
    HospitalDataType(String readableName) {
        this.readableName = readableName;
    }
    public String toReadableName() {
        return readableName;
    }
}
