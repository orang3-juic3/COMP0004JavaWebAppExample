package uk.ac.ucl.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.main.Config;
import uk.ac.ucl.model.DataFrame;
import uk.ac.ucl.model.HospitalDataType;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.Search;
import uk.ac.ucl.model.SearchBuilder;
import uk.ac.ucl.model.StringMatcher;
import uk.ac.ucl.model.UserErrorException;

import java.io.IOException;
import java.util.List;

@WebServlet("/save-to-json")
public class SaveToJSONServlet extends AbstractGetRequestServlet {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void handleRequest(HttpServletRequest req) {}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        ServletContext context = getServletContext();
        RequestDispatcher dispatch = null;
        try {
            DataFrame targetDataFrame = null;
            String searchParam = req.getParameter("search");
            String typeParam = req.getParameter("type");
            String idParam = req.getParameter("id");
            // Prioritise saving data if we can reconstruct a DataFrame from the search parameter
            if (searchParam != null && !searchParam.isEmpty()) {
                targetDataFrame = Search.deserialize(searchParam).execute();
            } else if (typeParam != null) {
                HospitalDataType type = HospitalDataType.valueOf(typeParam);
                if (idParam != null && !idParam.isEmpty()) { // if we can narrow the search to a data type and an id
                    SearchBuilder searchBuilder = new SearchBuilder(type)
                            .addQuery(idParam)
                            .usingMatcher(StringMatcher.EXACT);
                    // the column corresponding to the patient id has different names depending on data type
                    if (type == HospitalDataType.GENERAL) {
                        searchBuilder.includingColumns("ID");
                    } else {
                        searchBuilder.includingColumns("PATIENT");
                    }
                    targetDataFrame = searchBuilder.build().execute();
                } else {
                    targetDataFrame = Model.getInstance().getFrame(type);
                }
            }
            // e.g. if the only param is a patient id, since we cannot construct a DataFrame from that
            if (targetDataFrame == null) {
                throw new UserErrorException("No parameters provided to resolve DataFrame");
            }
            int page = -1;
            String pageParam = req.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 0) {
                        throw new UserErrorException("Invalid page number.");
                    }
                } catch (NumberFormatException e) {
                    throw new UserErrorException("Invalid page number format.");
                }
            }
            ArrayNode jsonArray = dataFrameToJson(targetDataFrame, page);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.setHeader("Content-Disposition", "attachment; filename=\"data.json\"");
            mapper.writeValue(res.getWriter(), jsonArray);
        } catch (UserErrorException e) {
            dispatch = context.getRequestDispatcher("/404.jsp");
        } catch (Exception e) {
            dispatch = context.getRequestDispatcher("/500.jsp");
        } finally {
            if (dispatch != null) {
                dispatch.forward(req, res);
            }
        }

    }

    private ArrayNode dataFrameToJson(DataFrame dataFrame, int page) {
        ArrayNode arrayNode = mapper.createArrayNode();
        List<String> cols = dataFrame.getColumnNames();
        int startRow = 0;
        int endRow = dataFrame.getRowCount();
        if (page >= 0) {
            int pageSize = Config.RESULTS_PER_PAGE;
            startRow = page * pageSize;
            endRow = Math.min(startRow + pageSize, dataFrame.getRowCount());
            if (startRow >= dataFrame.getRowCount() && dataFrame.getRowCount() > 0) {
                throw new UserErrorException("Page number out of bounds.");
            }
        }
        for (int i = startRow; i < endRow; i++) {
            ObjectNode rowNode = mapper.createObjectNode();
            for (String col : cols) {
                rowNode.put(col, dataFrame.getValue(col, i));
            }
            arrayNode.add(rowNode);
        }
        return arrayNode;
    }
}