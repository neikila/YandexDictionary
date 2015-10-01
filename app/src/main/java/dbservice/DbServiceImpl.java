package dbservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

/**
 * Created by ivan on 30.09.15.
 */
public class DbServiceImpl extends SQLiteOpenHelper implements DbService {

    public DbServiceImpl(Context context) {
        super(context, "Dictionary DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Languages (id integer primary key autoincrement, language text, reduced text);"
                + "create Dictionary (id integer primary key autoincrement, word text, translate text, language text;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public Map<String, String> getLanguages() {
        return null;
    }

    @Override
    public String translate(String input) {
        return null;
    }

    @Override
    public void save(String input, String result) {

    }

    @Override
    public void clear() {

    }

    @Override
    public String getReduced(String language) {
        return null;
    }
}


