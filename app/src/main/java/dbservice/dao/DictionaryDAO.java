package dbservice.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import dbservice.dataSets.DictionaryDataSet;

/**
 * Created by ivan on 01.10.15
 */
public class DictionaryDAO extends BaseDaoImpl<DictionaryDataSet, Integer> {

    public DictionaryDAO(ConnectionSource connectionSource,
                         Class<DictionaryDataSet> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public String translate(String word, String language) throws SQLException {
        QueryBuilder<DictionaryDataSet, Integer> queryBuilder = queryBuilder();
        DictionaryDataSet data = queryBuilder.where().eq("word", word).and().eq("language", language).queryForFirst();
        return data.getLanguage();
    }

    public void saveTranslate(String word, String translate, String language) throws SQLException {
        create(new DictionaryDataSet(word, translate, language));
    }

    public void clearDictionary() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), DictionaryDataSet.class);
    }
}
