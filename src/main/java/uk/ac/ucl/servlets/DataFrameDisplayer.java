package uk.ac.ucl.servlets;

import jakarta.servlet.http.HttpServletRequest;
import uk.ac.ucl.model.DataFrame;

/**
 * This interface is implemented by servlets that specifically show DataFrames.
 * The name computeDataFrame suggests that calling this method is expensive.
 */
public interface DataFrameDisplayer {
    DataFrame computeDataFrame(HttpServletRequest request);
}
