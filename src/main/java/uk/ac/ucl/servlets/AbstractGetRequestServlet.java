package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.DataFrame;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.UserErrorException;

import java.io.IOException;

public abstract class AbstractGetRequestServlet extends HttpServlet {

    // Refuse any request that is not a GET
    @Override
    protected final void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("GET")) {
            super.service(req, res);
        } else {
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/404.jsp");
            dispatch.forward(req, res);
        }
    }
    protected String getTargetJspPage() {
        return "/dataFrameDisplay.jsp";
    }
    protected abstract void handleRequest(HttpServletRequest req);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ServletContext context = getServletContext();
        RequestDispatcher dispatch;
        try {
            handleRequest(req);
            handleDataFrame(req);
            dispatch = context.getRequestDispatcher(getTargetJspPage());
        } catch (UserErrorException e) {
            dispatch = context.getRequestDispatcher("/404.jsp");
        } catch (RuntimeException e) {
            dispatch = context.getRequestDispatcher("/500.jsp");
        }
        dispatch.forward(req, res);
    }
    private void handleDataFrame(HttpServletRequest req) {
        if (this instanceof DataFrameDisplayer) {
            final DataFrame toDisplay = ((DataFrameDisplayer) this).computeDataFrame(req);
            req.setAttribute("data", toDisplay);
            if (req.getParameter("page") == null) {
                req.setAttribute("page", 0);
            } else {
                try {
                    int page = Integer.parseInt(req.getParameter("page"));
                    req.setAttribute("page", page);
                    if (page < 0 || page * Model.RESULTS_PER_PAGE> toDisplay.getRowCount() - 1 ) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    throw new UserErrorException("Invalid page number");
                }
            }
        }
    }
}
