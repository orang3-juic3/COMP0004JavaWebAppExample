package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.annotation.WebServlet;
import uk.ac.ucl.model.*;

@WebServlet("/general-info")
public class ViewGeneralPatientsInfoServlet extends AbstractViewDataFrameServlet {

    @Nonnull
    @Override
    public HospitalDataType getHospitalDataType() {
        return HospitalDataType.GENERAL;
    }
}
