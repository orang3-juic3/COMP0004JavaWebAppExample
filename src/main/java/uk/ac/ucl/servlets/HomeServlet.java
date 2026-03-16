package uk.ac.ucl.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;

@WebServlet("")
public class HomeServlet extends AbstractGetRequestServlet {

    @Override
    protected String getTargetJspPage() {
        return "/index.jsp";
    }

    @Override
    protected void handleRequest(HttpServletRequest req) {}
}
