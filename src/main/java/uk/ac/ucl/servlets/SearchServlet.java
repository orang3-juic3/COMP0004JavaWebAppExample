package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.*;

import java.io.IOException;
import java.util.Objects;

/**
 * The SearchServlet handles HTTP requests for performing patient searches.
 * It is mapped to the URL "/runsearch".
 * <p>
 * This servlet demonstrates:
 * 1. Handling both GET and POST requests.
 * 2. Interacting with a Model via a Factory pattern.
 * 3. Input validation.
 * 4. Error handling and forwarding to error pages.
 * 5. Request-scoped attribute passing to JSPs for rendering results.
 */
@WebServlet("/runsearch")
public class SearchServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests.
     * <p>
     * By calling doPost, this allows search results to be bookmarked and refreshed
     * (since many browsers default to GET for URL-based navigation).
     *
     * @param req the HttpServletRequest object that contains the request the client has made of the servlet
     * @param res the HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException      if an input or output error is detected when the servlet handles the GET request
     */
    private void send404(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = context.getRequestDispatcher("/404.jsp");
        dispatch.forward(req, res);
        doPost(req, res);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        send404(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // 1. Retrieve the search term from the request parameter.
        // The "searchstring" parameter name matches the 'name' attribute of the input field in search.html.
        String searchString = req.getParameter("searchstring");
        final Model model = Model.getInstance();
        // This try catch block corresponds to errors caused by the server
        try {
            // 2. Get the singleton instance of the Model.
            // The Model handles the actual data processing and search logic.
            HospitalDataType dataType;
            DataFrame data = null;
            // This try catch block corresponds to errors caused by user malformed queries
            try {
                if (searchString == null || searchString.trim().isEmpty()) {
                    throw new IllegalArgumentException("Search string is empty");
                }
                dataType = HospitalDataType.valueOf(req.getParameter("dataType"));
                if (dataType == HospitalDataType.SEARCH) {
                    throw new IllegalArgumentException("Searching within search results is not supported");
                }
                if (dataType == HospitalDataType.TRANSIENT) {
                    final String id = Objects.requireNonNull(req.getParameter("id"));
                    final HospitalDataType targetType = HospitalDataType.valueOf(req.getParameter("targetType"));
                    data = model.searchDataFrame(targetType, id, StringMatcher.EXACT);
                    data = model.searchDataFrame(data, searchString, StringMatcher.DEFAULT);
                }
            } catch (IllegalArgumentException | NullPointerException | IOException e) {
                send404(req, res);
                return;
            }
            req.setAttribute("data", data == null ? model.searchDataFrame(dataType, searchString, StringMatcher.DEFAULT) : data);
            req.setAttribute("dataTypeReadable", "Search Results");
            req.setAttribute("dataTypeRaw", HospitalDataType.SEARCH);

            // 5. Forward the request to the JSP page for display.
            // RequestDispatcher.forward() is used to send the request/response objects to another resource (JSP).
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/dataFrameDisplay.jsp");
            dispatch.forward(req, res);

        } catch (IOException e) {
            // 6. Exception Handling.
            // If there is an issue loading the model or data, log the error and forward to a dedicated error page.
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/500.jsp");
            dispatch.forward(req, res);
        }
    }
}
