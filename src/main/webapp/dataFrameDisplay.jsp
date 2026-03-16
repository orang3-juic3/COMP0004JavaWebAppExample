<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="uk.ac.ucl.model.DataFrame" %>
<%@ page import="java.util.List" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="uk.ac.ucl.main.Config" %>

<% String title = (String) request.getAttribute("dataTypeReadable");
String serialisedSearch = (String) request.getAttribute("search");
if (title == null) {
    title = "Search Results";
}
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= title %></title>
    <link rel="stylesheet" href="style.css">

    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #2c2c2c;
        }
        th, td {
            padding: 12px;
            border: 1px solid #444;
            text-align: left;
        }
        th {
            background-color: #1a1a1a;
            color: #66b2ff;
            position: sticky;
            top: 0;
        }
        tr:nth-child(even) {
            background-color: #333;
        }
        tr:hover {
            background-color: #444;
        }
        .pagination {
            margin-top: 15px;
            display: flex;
            gap: 10px;
        }
        .pagination a, .pagination button {
            padding: 8px 15px;
            background-color: #66b2ff;
            color: #111;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
        }
        .pagination a:hover, .pagination button:hover {
            background-color: #99ccff;
        }
        .search-container {
            background-color: #333;
            padding: 15px;
            margin: 20px 0;
            border-radius: 5px;
            border: 1px solid #444;
        }
        .search-container input[type="text"] {
            padding: 8px;
            width: 300px;
            border: 1px solid #555;
            border-radius: 4px;
            background-color: #222;
            color: #f0f0f0;
        }
        .search-container input[type="submit"] {
            padding: 8px 15px;
            background-color: #66b2ff;
            color: #111;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
        }
        .search-container input[type="submit"]:hover {
            background-color: #99ccff;
        }
    </style>
</head>
<body>

<h2><%= title %></h2>
<a href="<%=request.getContextPath() + "/"%>">&larr; Back to Home</a>
<% if (!"Search Results".equals(title)) {%>
<div class="search-container">
    <form method="GET" action="/run-search">
        <label>
            <input type="text" name="query" placeholder="Search patients..." required />
        </label>
        <%
            if (request.getAttribute("search") == null) {
        %>
        <% if (request.getParameter("id") != null) { %>
        <input type="hidden" name="id" value="<%= request.getParameter("id") %>" />
        <% } %>
        <input type="hidden" name="type" value="<%= request.getParameter("type") %>" />
        <%
            } else {
        %>
        <input type="hidden" name="search" value="<%= request.getAttribute("search") %>" />
        <% } %>
        <input type="submit" value="Search" />
    </form>
</div>
<%}%>
<%
    DataFrame df = (DataFrame) request.getAttribute("data");
    if (df != null && df.getRowCount() > 0) {
        int currentPage = 0;
        if (request.getAttribute("page") != null) {
            currentPage = (Integer) request.getAttribute("page");
        }
        int startRow = currentPage * Config.RESULTS_PER_PAGE;
        int endRow = Math.min(startRow + Config.RESULTS_PER_PAGE, df.getRowCount());
        int maxPage = Math.ceilDiv(df.getRowCount() ,Config.RESULTS_PER_PAGE) - 1;
%>
<table>
    <thead>
    <tr>
        <%
            List<String> names = df.getColumnNames();
            for (String name : names) {
        %>
        <th><%= name %></th>
        <% } %>
    </tr>
    </thead>
    <tbody>
    <%
        String clickableName = null;
        if (names.contains("PATIENT")) {
            clickableName = "PATIENT";
        } else if (names.contains("ID")) {
            clickableName = "ID";
        }
        for (int rowIndex = startRow; rowIndex < endRow; rowIndex++) {
    %>
    <tr>
        <%

            for (String name : names) {
                String cellValue = df.getValue(name, rowIndex);

                if (name.equalsIgnoreCase(clickableName) && cellValue != null && !cellValue.isEmpty()) {
        %>
        <td><a href="patient-overview?id=<%= cellValue %>"><%= cellValue %></a></td>
        <%
        } else {
        %>
        <td><%= cellValue %></td>
        <%
                }
            }
        %>
    </tr>
    <% } %>
    </tbody>
</table>
<%
    StringBuilder queryParams = new StringBuilder();
    java.util.Enumeration<String> paramNames = request.getParameterNames();
    while (paramNames.hasMoreElements()) {
        String pName = paramNames.nextElement();
        if (!"page".equals(pName)) {
            String[] pValues = request.getParameterValues(pName);
            for (String pValue : pValues) {
                if (pValue != null) {
                    queryParams.append(pName).append("=").append(java.net.URLEncoder.encode(pValue, StandardCharsets.UTF_8)).append("&");
                }
            }
        }
    }
    String baseParams = queryParams.toString();
%>
<div class="pagination" style="display: flex; align-items: center; gap: 15px; margin-bottom: 20px;">
    <% if (currentPage > 0) { %>
    <a href="?<%= baseParams %>page=<%= currentPage - 1 %>">&larr; Previous Page</a>
    <% } %>

    <% if (endRow < df.getRowCount()) { %>
    <a href="?<%= baseParams %>page=<%= currentPage + 1 %>">Next Page &rarr;</a>
    <% } %>

    <form method="GET" action="" style="display: flex; align-items: center; gap: 5px; margin: 0;">
        <%
            java.util.Enumeration<String> formParamNames = request.getParameterNames();
            while (formParamNames.hasMoreElements()) {
                String pName = formParamNames.nextElement();
                if (!"page".equals(pName)) {
                    String[] pValues = request.getParameterValues(pName);
                    for (String pValue : pValues) {
                        if (pValue != null) {
        %>
        <input type="hidden" name="<%= pName %>" value="<%= pValue %>">
        <%
                        }
                    }
                }
            }
        %>
        <label for="goToPage" style="color: #f0f0f0; margin-left: 10px;">Go to page (0 - <%= maxPage %>):</label>
        <input type="number" id="goToPage" name="page" min="0" max="<%= maxPage %>" value="<%= currentPage %>"
               style="width: 60px; padding: 6px; border-radius: 4px; border: 1px solid #555; background-color: #222; color: #f0f0f0;" required>
        <button type="submit" style="padding: 6px 15px; background-color: #66b2ff; color: #111; border: none; border-radius: 4px; cursor: pointer; font-weight: bold;">Go</button>
    </form>

    <form method="GET" action="/save-to-json" style="display: inline-block; margin: 0; margin-left: auto;">
        <%
            if (serialisedSearch == null) {
                java.util.Enumeration<String> pageDlParams = request.getParameterNames();
                while (pageDlParams.hasMoreElements()) {
                    String pName = pageDlParams.nextElement();
                    String[] pValues = request.getParameterValues(pName);
                    for (String pValue : pValues) {
                        if (pValue != null) {
        %>
        <input type="hidden" name="<%= pName %>" value="<%= pValue %>">
        <%
                        }
                    }
                }
            } else { %>
        <input type="hidden" name="search" value="<%=serialisedSearch%>">
        <% } %>
            <% if (request.getParameter("page") == null || serialisedSearch != null) {
        %>
        <input type="hidden" name="page" value="<%= currentPage %>">
        <%
            } %>
        <button type="submit" style="padding: 8px 15px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold;">
            Export Page to JSON
        </button>
    </form>

    <form method="GET" action="/save-to-json" style="display: inline-block; margin: 0;">
        <%
            if (serialisedSearch == null) {
                java.util.Enumeration<String> allDlParams = request.getParameterNames();
                while (allDlParams.hasMoreElements()) {
                    String pName = allDlParams.nextElement();
                    if (!"page".equals(pName)) {
                        String[] pValues = request.getParameterValues(pName);
                        for (String pValue : pValues) {
                            if (pValue != null) {

        %>
        <input type="hidden" name="<%= pName %>" value="<%= pValue %>">
        <%
                            }
                        }
                    }
                }
            } else {
        %>
        <input type="hidden" name="search" value="<%=serialisedSearch%>">
        <%}%>
        <button type="submit" style="padding: 8px 15px; background-color: #17a2b8; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold;">
            Export All to JSON
        </button>
    </form>
</div>

<%
} else {
%>
<p style="color: #ff6b6b; margin-top: 20px;">No patient data is currently available to display.</p>

<%}
%>

</body>
</html>