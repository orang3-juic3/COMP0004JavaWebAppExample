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
import java.util.List;

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
        RequestDispatcher dispatch = context.getRequestDispatcher("/404.html");
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

        try {
            // 2. Get the singleton instance of the Model.
            // The Model handles the actual data processing and search logic.
            HospitalDataType dataType;
            try {
                dataType = HospitalDataType.valueOf(req.getParameter("dataType"));
            } catch (IllegalArgumentException | NullPointerException e) {
                send404(req, res);
                return;
            }
            if (dataType == HospitalDataType.TRANSIENT) {
                send404(req, res);
                return;
            }

            // 3. Basic validation of search input.
            if (searchString == null || searchString.trim().isEmpty()) {
                // If the user didn't enter anything, set an error message to be displayed on the result page.
                req.setAttribute("errorMessage", "Please enter a search term."); // TODO
            } else {
                req.setAttribute("data", model.searchDataFrame(dataType, searchString, StringMatcher.DEFAULT_MATCHER));
                req.setAttribute("dataTypeReadable", "Search Results");
                req.setAttribute("dataTypeRaw", HospitalDataType.TRANSIENT);
            }

            // 5. Forward the request to the JSP page for display.
            // RequestDispatcher.forward() is used to send the request/response objects to another resource (JSP).
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/dataFrameDisplay.jsp");
            dispatch.forward(req, res);

        } catch (IOException e) {
            // 6. Exception Handling.
            // If there is an issue loading the model or data, log the error and forward to a dedicated error page.
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/500.html");
            dispatch.forward(req, res);
        }
    }
}
