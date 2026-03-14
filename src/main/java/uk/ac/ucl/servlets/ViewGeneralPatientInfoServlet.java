package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.*;

import java.io.IOException;

@WebServlet("/patient_info")
public class ViewGeneralPatientInfoServlet extends AbstractViewDataFrameServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            req.setAttribute("patientData", Model.getInstance().getFrame(getHospitalDataType()));
            req.setAttribute("dataType", getHospitalDataType().toReadableName());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/patient_info.jsp");
            dispatch.forward(req, res);
        } catch (IOException e) {
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/500.html");
            dispatch.forward(req, res);
        }
    }

    @Nonnull
    @Override
    public HospitalDataType getHospitalDataType() {
        return HospitalDataType.GENERAL;
    }
}
