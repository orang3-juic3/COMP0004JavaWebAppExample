package uk.ac.ucl.servlets;

import jakarta.servlet.http.HttpServletRequest;
import uk.ac.ucl.model.DataFrame;

public interface DataFrameDisplayer {
    DataFrame computeDataFrame(HttpServletRequest request);
}
