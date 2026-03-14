package uk.ac.ucl.servlets;

import jakarta.annotation.Nonnull;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.HospitalDataType;

import java.io.IOException;

public abstract class AbstractViewDataFrameServlet extends HttpServlet {

    // This is to sidestep the problem with adding a constructor to a Servlet class
    @Nonnull
    public abstract HospitalDataType getHospitalDataType();

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
}
