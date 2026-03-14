<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="uk.ac.ucl.model.DataFrame" %>
<%@ page import="java.util.List" %>
<%@ page import="uk.ac.ucl.model.HospitalDataType" %>

<% String dataType = (String) request.getAttribute("dataTypeReadable");%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= dataType %></title>
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
        <%
            if ((request.getAttribute("dataTypeRaw") != HospitalDataType.TRANSIENT)) {
        %>
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
        <% } %>
    </style>
</head>
<body>

<h2><%= dataType %></h2>
<a href="index.html">&larr; Back to Home</a>
<%
    if ((request.getAttribute("dataTypeRaw") != HospitalDataType.TRANSIENT)) {
%>
<div class="search-container">
    <form method="POST" action="/runsearch">
        <input type="text" name="searchstring" placeholder="Search patients..." required />
        <input type="hidden" name="dataType" value="<%= request.getAttribute("dataTypeRaw").toString()%>" />
        <input type="submit" value="Search" />
    </form>
</div>
<% } %>
<%
    DataFrame df = (DataFrame) request.getAttribute("data");
    if (df != null && df.getRowCount() > 0) {
%>
<table>
    <thead>
    <tr>
        <%
            List<String> names = df.getColumnNames();
            for (String name : df.getColumnNames()) {
        %>
        <th><%= name %></th>
        <% } %>
    </tr>
    </thead>
    <tbody>
    <%
        for (int rowIndex = 0; rowIndex < df.getRowCount(); rowIndex++) {
    %>
    <tr>
        <%
            for (String name : names) {
        %>
        <td><%= df.getValue(name, rowIndex) %></td>
        <% } %>
    </tr>
    <% } %>
    </tbody>
</table>
<%
} else {
%>
<p style="color: #ff6b6b; margin-top: 20px;">No patient data is currently available to display.</p>
<%
    }
%>

</body>
</html>