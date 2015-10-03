package dbservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;

import dbservice.dao.DictionaryDAO;
import dbservice.dao.RouteDAO;
import dbservice.dataSets.DictionaryDataSet;
import dbservice.dataSets.RouteDataSet;

/**
 * Created by ivan on 30.09.15.
 */
public class DBServiceImpl extends OrmLiteSqliteOpenHelper implements DBService {

    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;

    private DictionaryDAO dictionaryDAO = null;
    private RouteDAO routeDAO = null;

    public DBServiceImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DictionaryDataSet.class);
            TableUtils.createTable(connectionSource, RouteDataSet.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public String translate(String input, String language) throws SQLException {
        if (dictionaryDAO == null) setDictionaryDAO();
        return dictionaryDAO.translate(input, language);
    }

    @Override
    public void saveTranslate(String input, String result, String language) throws SQLException {
        if (dictionaryDAO == null) setDictionaryDAO();
        dictionaryDAO.saveTranslate(input, result, language);
    }

    @Override
    public void clearDictionary() throws SQLException {
        if (dictionaryDAO == null) setDictionaryDAO();
        dictionaryDAO.clearDictionary();
    }

    @Override
    public void saveRoutes(ArrayList<RouteDataSet> routes) throws SQLException {
        if (routeDAO == null) setRoutesDAO();
        routeDAO.saveRoutes(routes);
    }

    public synchronized void setDictionaryDAO() throws SQLException {
        dictionaryDAO = new DictionaryDAO(getConnectionSource(), DictionaryDataSet.class);
    }

    public synchronized void setRoutesDAO() throws SQLException {
        routeDAO = new RouteDAO(getConnectionSource(), RouteDataSet.class);
    }
}