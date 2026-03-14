package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.annotation.WebServlet;
import uk.ac.ucl.model.HospitalDataType;

@WebServlet("/immunizations")
public class ViewImmunizationsServlet extends AbstractViewDataFrameServlet {
    @Nonnull
    @Override
    public HospitalDataType getHospitalDataType() {
        return HospitalDataType.IMMUNIZATIONS;
    }
}
