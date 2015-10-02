package dbservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import dbservice.dao.DictionaryDAO;
import dbservice.dao.RouteDAO;
import dbservice.dataSets.DictionaryDataSet;
import dbservice.dataSets.RouteDataSet;

/**
 * Created by ivan on 30.09.15.
 */
public class DbServiceImpl extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;

    private DictionaryDAO dictionaryDAO = null;
    private RouteDAO languagesDAO = null;

    public DbServiceImpl(Context context) {
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

    public synchronized DictionaryDAO getDictionaryDAO() throws SQLException {
        if (dictionaryDAO == null) {
            dictionaryDAO = new DictionaryDAO(getConnectionSource(), DictionaryDataSet.class);
        }
        return dictionaryDAO;
    }

    public synchronized RouteDAO getLanguagesDAO() throws SQLException {
        if (languagesDAO == null) {
            languagesDAO = new RouteDAO(getConnectionSource(), RouteDataSet.class);
        }
        return languagesDAO;
    }
}