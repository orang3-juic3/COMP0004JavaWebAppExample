package uk.ac.ucl.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import uk.ac.ucl.model.*;


@WebServlet("/patient-overview")
public class ViewPatientOverviewServlet extends AbstractGetRequestServlet {

    @Override
    protected String getTargetJspPage() {
        return "/patient-overview.jsp";
    }

    @Override
    protected void handleRequest(HttpServletRequest req) {
        String id = req.getParameter("id");
        if (id == null) {
            throw new UserErrorException("No id parameter!");
        }
        final DataFrame patientOverview = new SearchBuilder(HospitalDataType.GENERAL)
                .addQuery(id)
                .includingColumns("ID")
                .usingMatcher(StringMatcher.EXACT)
                .build().execute();
        if (patientOverview.getRowCount() != 1) {
            throw new UserErrorException("Could not find a unique person with id " + id);
        }
        String firstName = patientOverview.getValue("FIRST", 0);
        String lastName = patientOverview.getValue("LAST", 0);
        req.setAttribute("firstName", firstName);
        req.setAttribute("lastName", lastName);
        req.setAttribute("id", req.getParameter("id"));
    }
}
