package dbservice;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by ivan on 02.10.15
 */
public class DBHelperFactory {
    //TODO изменить DBService так, чтобы его можно было здесь использовать?
    private static DbServiceImpl dbService;

    public static DbServiceImpl getHelper() {
        return dbService;
    }

    public static void setHelper(Context context) {
        dbService = OpenHelperManager.getHelper(context, DbServiceImpl.class);
    }

    public static void releaseHelper() {
        OpenHelperManager.releaseHelper();
        dbService = null;
    }
}
