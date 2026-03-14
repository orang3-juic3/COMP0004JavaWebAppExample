<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="uk.ac.ucl.model.DataFrame" %>
<%@ page import="java.util.List" %>

<% String dataType = (String) request.getAttribute("dataType");%>
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
    </style>
</head>
<body>

<h2><%= dataType %></h2>
<a href="index.html">&larr; Back to Home</a>

<%
    DataFrame df = (DataFrame) request.getAttribute("patientData");
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