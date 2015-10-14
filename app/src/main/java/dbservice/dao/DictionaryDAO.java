package dbservice.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dbservice.dataSets.DictionaryDataSet;
import dbservice.dataSets.LanguageDataSet;
import dictionary.Dictionary;

/**
 * Created by ivan on 01.10.15
 */
public class DictionaryDAO extends BaseDaoImpl<DictionaryDataSet, Integer> {

    public DictionaryDAO(ConnectionSource connectionSource,
                         Class<DictionaryDataSet> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public void getAll() throws SQLException {
        List<DictionaryDataSet> result = queryBuilder().query();
        // Todo delete it
        return;
    }

    public String translate(String word, String language) throws SQLException {
        QueryBuilder<DictionaryDataSet, Integer> queryBuilder = queryBuilder();
        DictionaryDataSet data = queryBuilder.where()
                .eq(DictionaryDataSet.WORD, word).and()
                .eq(DictionaryDataSet.LANGUAGE, language)
                .queryForFirst();
        if (data != null) {
            return data.getTranslate();
        } else {
            return null;
        }
    }

    public ArrayList<DictionaryDataSet> translate(String word) throws SQLException {
        ArrayList<DictionaryDataSet> dataList = (ArrayList<DictionaryDataSet>)queryBuilder().where().eq("word", word).query();
        if (dataList != null) {
            return dataList;
        } else {
            return null;
        }
    }

    public void saveTranslate(String word, String translate, String language) throws SQLException {
        create(new DictionaryDataSet(word, translate, language));
    }

    public void clearDictionary() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), DictionaryDataSet.class);
    }
}
