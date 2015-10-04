package dbservice.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;

import dbservice.dataSets.LanguageDataSet;
import dbservice.dataSets.RouteDataSet;

/**
 * Created by ivan on 01.10.15
 */
public class LanguageDAO extends BaseDaoImpl<LanguageDataSet, Integer> {

    public LanguageDAO(ConnectionSource connectionSource,
                       Class<LanguageDataSet> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public String getFullName(String reduced) throws SQLException {
        return queryBuilder().where().eq(LanguageDataSet.REDUCED, reduced).queryForFirst().getLanguage();
    }

    public String getReduced(String lang) throws SQLException {
        return queryBuilder().where().eq(LanguageDataSet.LANGUAGE, lang).queryForFirst().getReduced();
    }

    public ArrayList<LanguageDataSet> getAllLangs() throws SQLException {
        return (ArrayList<LanguageDataSet>)queryBuilder().query();
    }

    public void saveLanguage(String reduced, String language) throws SQLException{
        create(new LanguageDataSet(reduced, language));
    }
}
