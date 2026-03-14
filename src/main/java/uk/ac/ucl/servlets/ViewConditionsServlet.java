package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.annotation.WebServlet;
import uk.ac.ucl.model.HospitalDataType;

@WebServlet("/conditions")
public class ViewConditionsServlet extends AbstractViewDataFrameServlet {
    @Nonnull
    @Override
    public HospitalDataType getHospitalDataType() {
        return HospitalDataType.CONDITIONS;
    }
}
