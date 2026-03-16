<%@ page import="uk.ac.ucl.model.HospitalDataType" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Patient Data</title>
  <link rel="stylesheet" href="style.css">
  <style>
    .home-container {
      background-color: #2c2c2c;
      padding: 20px;
      border: 1px solid #444;
      border-radius: 8px;
      max-width: 600px;
      margin-top: 20px;
    }
    .nav-list {
      list-style-type: none;
      padding: 0;
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 15px;
    }
    .nav-list li {
      background-color: #333;
      border-left: 4px solid #66b2ff;
      border-radius: 4px;
      padding: 10px;
    }
    .nav-list a {
      font-size: 16px;
      font-weight: bold;
      display: block;
    }
  </style>
</head>
<body>
<h2>Welcome to the Hospital Data App</h2>

<nav class="home-container">
  <h3 style="margin-top: 0;">Global Database Records</h3>
  <ul class="nav-list">
    <% for (HospitalDataType type : HospitalDataType.values()) {%>
    <li><a href="data?type=<%=type.toString()%>"><%=type.toReadableName()%></a></li>
    <% } %>
  </ul>
</nav>

</body>
</html>