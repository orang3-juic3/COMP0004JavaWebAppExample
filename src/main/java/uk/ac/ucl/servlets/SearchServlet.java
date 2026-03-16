package uk.ac.ucl.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import uk.ac.ucl.model.*;


@WebServlet("/run-search")
public class SearchServlet extends AbstractGetRequestServlet implements DataFrameDisplayer {


    @Override
    public DataFrame computeDataFrame(HttpServletRequest request) {
        String query = request.getParameter("query");
        if (query == null || query.trim().isEmpty()) {
            throw new UserErrorException("Search string is empty");
        }
        // this corresponds to DataFrames that are due to more complex searches that include more than one query
        // E.g. ViewInhabitantsServlet
        String existingSearch = request.getParameter("search");
        if (existingSearch != null) {
            try {
                return SearchBuilder.fromSearch(Search.deserialize(existingSearch))
                        .addQuery(query)
                        .build()
                        .execute();
            } catch (IllegalArgumentException e) {
                throw new UserErrorException("Failed to deserialize search");
            }
        } else {
            String id = request.getParameter("id");
            HospitalDataType type;
            try {
                type = HospitalDataType.valueOf(request.getParameter("type"));
            } catch (NullPointerException | IllegalArgumentException e) {
                throw new UserErrorException("Invalid target type or no target type provided");
            }
            // We can run a search with a valid data type and id
            final SearchBuilder builder = new SearchBuilder(type);
            if (id != null) {
                    builder.addQuery(id)
                        .usingMatcher(StringMatcher.EXACT)
                        .includingColumns(getAppropriateIDColumn(type));
            }
            return builder.addQuery(query)
                    .build().execute();
        }
    }
    // the column corresponding to the patient id has different names depending on data type
    private String getAppropriateIDColumn(HospitalDataType type) {
        return type == HospitalDataType.GENERAL ? "ID" : "PATIENT";
    }

    @Override
    protected void handleRequest(HttpServletRequest req) {
        if (req.getParameter("search") == null && (req.getParameter("type")) == null) {
            throw new UserErrorException("Wrong parameters");
        }
        req.setAttribute("search", true); // so that we can't search within search results
    }
}
