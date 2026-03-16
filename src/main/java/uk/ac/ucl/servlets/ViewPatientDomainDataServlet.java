package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.*;

import java.io.IOException;

@WebServlet("/patient-info")
public class ViewPatientDomainDataServlet extends AbstractGetRequestServlet implements DataFrameDisplayer {

    @Override
    protected void handleRequest(HttpServletRequest req) {}

    private String searchFullName(String id) {
        SearchBuilder searchBuilder = new SearchBuilder(HospitalDataType.GENERAL)
                .addQuery(id)
                .usingMatcher(StringMatcher.EXACT)
                .includingColumns("ID");
        final Search nameSearch = searchBuilder.build();
        DataFrame patientDetails = nameSearch.execute();
        if (patientDetails.getRowCount() != 1) {
            throw new IllegalStateException("Could not find a unique person with id " + id);
        }
        String firstName = patientDetails.getValue("FIRST", 0);
        String lastName = patientDetails.getValue("LAST", 0);
        return lastName + ", " + firstName + ": ";
    }

    @Override
    public DataFrame computeDataFrame(HttpServletRequest req) {
        final String typeName = req.getParameter("type");
        if (typeName == null) {
            throw new IllegalArgumentException("No url parameter for type!");
        }
        final HospitalDataType type = HospitalDataType.valueOf(typeName);
        req.setAttribute("type", type);
        String id = req.getParameter("id");
        final String fullName = searchFullName(id);
        SearchBuilder searchBuilder = new SearchBuilder(type)
                .addQuery(id)
                .usingMatcher(StringMatcher.EXACT);
        if (type == HospitalDataType.GENERAL) {
            searchBuilder.includingColumns("ID");
        } else {
            searchBuilder.includingColumns("PATIENT");
        }
        req.setAttribute("dataTypeReadable", fullName + type.toReadableName());
        return searchBuilder.build().execute();
    }
}
