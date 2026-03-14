package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.DataFrame;
import uk.ac.ucl.model.HospitalDataType;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.StringMatcher;

import java.io.IOException;

@WebServlet("patient-info")
public class ViewPatientDomainDataServlet extends AbstractViewDataFrameServlet {

    @Override
    protected void setAttributes(HttpServletRequest req) {
        req.setAttribute("dataTypeRaw", getHospitalDataType());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            final Model model = Model.getInstance();
            //TODO validate parameters
            setAttributes(req);
            String id = req.getParameter("id");
            DataFrame patientDetails = model.searchDataFrame(HospitalDataType.GENERAL, id, StringMatcher.EXACT_MATCHER);
            if (patientDetails.getRowCount() != 1) {
                throw new IOException("Could not find a unique person with id " + id);
            }
            String firstName = patientDetails.getValue("FIRST", 0);
            String lastName = patientDetails.getValue("LAST", 0);
            HospitalDataType domainToSearch = HospitalDataType.valueOf(req.getParameter("dataType"));
            req.setAttribute("data", model.searchDataFrame(domainToSearch, id, StringMatcher.EXACT_MATCHER));
            req.setAttribute("dataTypeReadable", lastName + ", " + firstName + ": " + domainToSearch.toReadableName());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/dataFrameDisplay.jsp");
            dispatch.forward(req, res);
        } catch (IOException | IllegalStateException e) {
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/500.html");
            dispatch.forward(req, res);
        }
    }

    @Nonnull
    @Override
    public HospitalDataType getHospitalDataType() {
        return HospitalDataType.TRANSIENT;
    }
}
