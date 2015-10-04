package dbservice;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by ivan on 02.10.15
 */
public class DBHelperFactory {
    private static DBServiceImpl dbService;

    public static DBServiceImpl getHelper() {
        return dbService;
    }

    public static void setHelper(Context context) {
        dbService = OpenHelperManager.getHelper(context, DBServiceImpl.class);
    }

    public static void releaseHelper() {
        OpenHelperManager.releaseHelper();
        dbService = null;
    }
}