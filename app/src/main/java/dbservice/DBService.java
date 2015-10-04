package dbservice;

import java.sql.SQLException;
import java.util.ArrayList;

import dbservice.dataSets.RouteDataSet;

/**
 * Created by neikila on 25.09.15.
 */

public interface DBService {
    String translate(String input, String sourceLanguage, String targetLanguage) throws SQLException;

    void saveTranslate(String input, String result, String language) throws SQLException;

    void clearDictionary() throws SQLException;

    String getReduced(String language) throws SQLException;

    void saveRoutes(ArrayList<String> routes) throws SQLException;

    ArrayList<String> getToLangPairedWithGivenLang(String language) throws SQLException;

    ArrayList<String> getAllLangs() throws SQLException;

    ArrayList<String> getTranslations(String query) throws SQLException;
}
