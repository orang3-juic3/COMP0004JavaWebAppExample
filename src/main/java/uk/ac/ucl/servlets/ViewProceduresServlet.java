package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.annotation.WebServlet;
import uk.ac.ucl.model.HospitalDataType;

@WebServlet("/procedures")
public class ViewProceduresServlet extends AbstractViewDataFrameServlet {
    @Nonnull
    @Override
    public HospitalDataType getHospitalDataType() {
        return HospitalDataType.PROCEDURES;
    }
}
