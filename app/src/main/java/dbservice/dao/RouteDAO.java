package dbservice.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;

import dbservice.dataSets.DictionaryDataSet;
import dbservice.dataSets.RouteDataSet;

/**
 * Created by ivan on 01.10.15
 */
public class RouteDAO extends BaseDaoImpl<RouteDataSet, Integer> {

    public RouteDAO(ConnectionSource connectionSource,
                    Class<RouteDataSet> dataClass) throws SQLException {
        super(connectionSource, dataClass);

    }

    public ArrayList<RouteDataSet> getToLangPairedWithFivenLang(String lang) throws SQLException{
        return (ArrayList<RouteDataSet>)queryBuilder().where().eq(RouteDataSet.FROM, lang).query();
    }

    public boolean isExist(RouteDataSet route) throws SQLException {
        return (null != queryBuilder().where().
                eq(RouteDataSet.FROM, route.getFrom()).and().
                eq(RouteDataSet.TO, route.getTo()).
                queryForFirst());
    }

    public void saveRoutes(ArrayList<RouteDataSet> routes) throws SQLException {
        for (int i = 0; i < routes.size(); i++) {
            create(routes.get(i));
        }
    }

    public void saveRoute(String from, String to) throws SQLException{
        create(new RouteDataSet(from, to));
    }

    public void saveRoute(RouteDataSet route) throws SQLException{
        create(route);
    }

}
