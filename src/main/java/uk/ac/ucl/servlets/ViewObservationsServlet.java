package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.annotation.WebServlet;
import uk.ac.ucl.model.HospitalDataType;

@WebServlet("/observations")
public class ViewObservationsServlet extends AbstractViewDataFrameServlet {
    @Nonnull
    @Override
    public HospitalDataType getHospitalDataType() {
        return HospitalDataType.OBSERVATIONS;
    }
}
