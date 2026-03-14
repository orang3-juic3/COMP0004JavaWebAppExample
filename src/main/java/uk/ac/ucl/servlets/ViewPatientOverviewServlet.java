package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.DataFrame;
import uk.ac.ucl.model.HospitalDataType;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.StringMatcher;

import java.io.IOException;

@WebServlet("/patient-overview")
public class ViewPatientOverviewServlet extends HttpServlet {

    // Refuse any request that is not a GET
    @Override
    protected final void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("GET")) {
            super.service(req, res);
        } else {
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/404.html");
            dispatch.forward(req, res);
        }
    }
    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            final Model model = Model.getInstance();
            String id = req.getParameter("id");
            DataFrame patientDetails = model.searchDataFrame(HospitalDataType.GENERAL, id, StringMatcher.EXACT_MATCHER);
            if (patientDetails.getRowCount() != 1) {
                throw new IOException("Could not find a unique person with id " + id);
            }
            String firstName = patientDetails.getValue("FIRST", 0);
            String lastName = patientDetails.getValue("LAST", 0);
            req.setAttribute("firstName", firstName);
            req.setAttribute("lastName", lastName);
            req.setAttribute("id", id);
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/patient-overview.jsp");
            dispatch.forward(req, res);
        } catch (IOException e) {
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/500.html");
            dispatch.forward(req, res);
        }

    }
}
