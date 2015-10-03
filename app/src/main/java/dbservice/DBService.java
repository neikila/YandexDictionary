package dbservice;

import java.sql.SQLException;
import java.util.ArrayList;

import dbservice.dataSets.RouteDataSet;

/**
 * Created by neikila on 25.09.15.
 */

public interface DBService {
    String translate(String input, String language) throws SQLException;

    void saveTranslate(String input, String result, String language) throws SQLException;

    void clearDictionary() throws SQLException;

    void saveRoutes(ArrayList<RouteDataSet> routes) throws SQLException;
}
