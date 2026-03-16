package uk.ac.ucl.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import uk.ac.ucl.model.*;

@WebServlet("/inhabitants")
public class ViewInhabitantsServlet extends AbstractGetRequestServlet implements DataFrameDisplayer {

    @Override
    protected void handleRequest(HttpServletRequest req) {
        if (req.getParameter("id") == null) {
            throw new UserErrorException("No id provided!");
        }
    }

    @Override
    public DataFrame computeDataFrame(HttpServletRequest request) {
        final String id = request.getParameter("id");
        final Search idSearch = new SearchBuilder(HospitalDataType.GENERAL)
                .addQuery(id)
                .includingColumns("ID")
                .usingMatcher(StringMatcher.EXACT).build();
        final DataFrame idRecord= idSearch.execute();
        if (idRecord.getRowCount() != 1) {
            throw new UserErrorException("No row found!");
        }
        request.setAttribute("dataTypeReadable", idRecord.getValue("LAST", 0)+ ", " +
                idRecord.getValue("FIRST", 0) + ": Inhabitants at address");
        final String address = idRecord.getValue("ADDRESS", 0);
        final String city = idRecord.getValue("CITY", 0);
        final String state = idRecord.getValue("STATE", 0);
        final String zip = idRecord.getValue("ZIP", 0);
        final Search inhabitantSearch = new SearchBuilder(HospitalDataType.GENERAL)
                .matchingExactly("ZIP", zip)
                .matchingExactly("ADDRESS", address)
                .matchingExactly("CITY", city)
                .matchingExactly("STATE", state).build();
        request.setAttribute("search", inhabitantSearch.serialize());
        request.setAttribute("type", HospitalDataType.GENERAL);
        return inhabitantSearch.execute();
    }
}
