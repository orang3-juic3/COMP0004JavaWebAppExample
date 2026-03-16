# Patient Data Browser

A Java web application that allows a user to browse through a hospital database.  
The user can search for data across many domains, view individual patient profiles
and run certain tools such as finding household members of the patient or exporting data 
to JSON.  
The project interprets the dataframe in spreadsheet style - this is primarily how data is displayed in the browser.  
There can be a lot of data to browse through, particularly if `patients100000.csv` is used.   
As a result, the web app implements pagination and caches search results.


## Prerequisites

- Java 25 (as configured in `pom.xml`)
- Maven 3.9+

## Project Structure

- `src/main/java` — Java source code (including the embedded Tomcat bootstrap in `uk.ac.ucl.main.Main`)
- `src/main/webapp` — Static web resources and JSPs
- `target` — Build output (created by Maven)
- `war-file` — Packaged WAR output (created by Maven)
- `data` — The source data csv files used to populate data frames.

## Compile

Build the project and produce a WAR file:

```bash
mvn clean package
```

This writes the WAR to `war-file/`.

## Run (Embedded Tomcat)

First compile the project, then run the main class via Maven:

```bash
mvn clean compile exec:exec
```  
You can also run the debug profile if you wish to attach a debugger:
```bash
mvn clean compile exec:exec -Pdebug
```

By default the server starts on port `8080`. Open:

```
http://localhost:8080
```

## Configuration

You can configure the server using system properties or environment variables:

- `SERVER_PORT` — Port to bind (default: `8080`)
- `WEBAPP_DIR` — Web resources directory (default: `src/main/webapp/`)
- `CLASSES_DIR` — Compiled classes directory (default: `target/classes`)

Example (using environment variables):

```bash
SERVER_PORT=9090 mvn clean compile exec:exec
```
If you would like to use different data files, edit the mapping in Config.java