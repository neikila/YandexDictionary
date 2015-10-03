package dbservice.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;

import dbservice.dataSets.RouteDataSet;

/**
 * Created by ivan on 01.10.15
 */
public class RouteDAO extends BaseDaoImpl<RouteDataSet, Integer> {

    public RouteDAO(ConnectionSource connectionSource,
                    Class<RouteDataSet> dataClass) throws SQLException {
        super(connectionSource, dataClass);

    }

    public void saveRoutes(ArrayList<RouteDataSet> routes) throws SQLException {
        for (int i = 0; i < routes.size(); i++) {
            create(routes.get(i));
        }
    }
}
