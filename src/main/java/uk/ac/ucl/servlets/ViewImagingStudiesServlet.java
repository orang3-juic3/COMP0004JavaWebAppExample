package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.annotation.WebServlet;
import uk.ac.ucl.model.HospitalDataType;
@WebServlet("/imaging-studies")
public class ViewImagingStudiesServlet extends AbstractViewDataFrameServlet {
    @Nonnull
    @Override
    public HospitalDataType getHospitalDataType() {
        return HospitalDataType.IMAGINGSTUDIES;
    }
}
