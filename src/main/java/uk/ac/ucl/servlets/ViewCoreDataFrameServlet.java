package uk.ac.ucl.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import uk.ac.ucl.model.HospitalDataType;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.UserErrorException;

@WebServlet("/data")
public class ViewCoreDataFrameServlet extends AbstractGetRequestServlet {
    @Override
    protected void handleRequest(HttpServletRequest req) {
        final String typeName = req.getParameter("type");
        if (typeName == null) {
            throw new UserErrorException("No url parameter for type!");
        }
        final HospitalDataType type = HospitalDataType.valueOf(typeName);
        req.setAttribute("type", type);
        req.setAttribute("data", Model.getInstance().getFrame(type));
        req.setAttribute("dataTypeReadable", type.toReadableName());
    }
}
