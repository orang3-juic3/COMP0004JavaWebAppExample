<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Patient Overview</title>
  <link rel="stylesheet" href="style.css">
  <style>
    .overview-container {
      background-color: #2c2c2c;
      padding: 20px;
      border: 1px solid #444;
      border-radius: 8px;
      max-width: 600px;
      margin-top: 20px;
    }
    .patient-header {
      border-bottom: 2px solid #66b2ff;
      padding-bottom: 10px;
      margin-bottom: 20px;
    }
    .domain-links {
      list-style-type: none;
      padding: 0;
    }
    .domain-links li {
      margin: 15px 0;
      padding: 10px;
      background-color: #333;
      border-left: 4px solid #66b2ff;
      border-radius: 4px;
    }
    .domain-links a {
      font-size: 18px;
      font-weight: bold;
      display: block;
      color: #66b2ff;
      text-decoration: none;
    }
    .domain-links a:hover {
      color: #99ccff;
      text-decoration: underline;
    }
  </style>
</head>
<body>

<h2>Patient Overview</h2>
<a href="index.html">&larr; Back to Home</a>

<%
  String id = (String) request.getAttribute("id");
  String firstName = (String) request.getAttribute("firstName");
  String lastName = (String) request.getAttribute("lastName");
%>

<div class="overview-container">
  <div class="patient-header">
    <h3><%= lastName %>, <%= firstName %></h3>
    <p style="color: #aaa; font-size: 14px;">Patient ID: <%= id %></p>
  </div>

  <p>Select a category below to view detailed records for this patient:</p>

  <ul class="domain-links">
    <li>
      <a href="patient-info?id=<%= id %>&dataType=GENERAL">View General Patient Information</a>
    </li>
    <li>
      <a href="patient-info?id=<%= id %>&dataType=ALLERGIES">View Allergy Information</a>
    </li>
  </ul>
</div>

</body>
</html>