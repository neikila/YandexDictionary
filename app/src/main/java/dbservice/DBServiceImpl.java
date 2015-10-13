package dbservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import dbservice.dao.DictionaryDAO;
import dbservice.dao.LanguageDAO;
import dbservice.dao.RouteDAO;
import dbservice.dataSets.DictionaryDataSet;
import dbservice.dataSets.LanguageDataSet;
import dbservice.dataSets.RouteDataSet;
import dictionary.Dictionary;

/**
 * Created by ivan on 30.09.15.
 */
public class DBServiceImpl extends OrmLiteSqliteOpenHelper implements DBService {

    private static final String DATABASE_NAME = "Dictionary.db";
    private static final int DATABASE_VERSION = 1;

    private LanguageDAO languageDAO = null;
    private DictionaryDAO dictionaryDAO = null;
    private RouteDAO routeDAO = null;

    public DBServiceImpl(Context context) throws SQLException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setDictionaryDAO();
        setRoutesDAO();
        setLanguagesDAO();
//        dictionaryDAO.getAll();
    }

    private void presetLanguages() throws SQLException {
        if (languageDAO == null) {
            setLanguagesDAO();
        }
        languageDAO.saveLanguage("en", "Английский");
        languageDAO.saveLanguage("sq", "Албанский");
        languageDAO.saveLanguage("ar", "Арабский");
        languageDAO.saveLanguage("hy", "Армянский");
        languageDAO.saveLanguage("az", "Азербайджанский");
        languageDAO.saveLanguage("af", "Африкаанс");
        languageDAO.saveLanguage("eu", "Баскский");
        languageDAO.saveLanguage("be", "Белорусский");
        languageDAO.saveLanguage("bg", "Болгарский");
        languageDAO.saveLanguage("bs", "Боснийский");
        languageDAO.saveLanguage("cy", "Валлийский");
        languageDAO.saveLanguage("vi", "Вьетнамский");
        languageDAO.saveLanguage("hu", "Венгерский");
        languageDAO.saveLanguage("mg", "Малагасийский");
        languageDAO.saveLanguage("gl", "Галисийский");
        languageDAO.saveLanguage("ht", "Гаитянский");
        languageDAO.saveLanguage("nl", "Голландский");
        languageDAO.saveLanguage("el", "Греческий");
        languageDAO.saveLanguage("ka", "Грузинский");
        languageDAO.saveLanguage("da", "Датский");
        languageDAO.saveLanguage("he", "Иврит");
        languageDAO.saveLanguage("id", "Индонезийский");
        languageDAO.saveLanguage("ga", "Ирландский");
        languageDAO.saveLanguage("it", "Итальянский");
        languageDAO.saveLanguage("is", "Исландский");
        languageDAO.saveLanguage("es", "Испанский");
        languageDAO.saveLanguage("kk", "Казахский");
        languageDAO.saveLanguage("ca", "Каталанский");
        languageDAO.saveLanguage("ky", "Киргизский");
        languageDAO.saveLanguage("zh", "Китайский");
        languageDAO.saveLanguage("ko", "Корейский");
        languageDAO.saveLanguage("la", "Латынь");
        languageDAO.saveLanguage("lv", "Латышский");
        languageDAO.saveLanguage("lt", "Литовский");
        languageDAO.saveLanguage("ms", "Малайский");
        languageDAO.saveLanguage("mt", "Мальтийский");
        languageDAO.saveLanguage("mk", "Македонский");
        languageDAO.saveLanguage("mn", "Монгольский");
        languageDAO.saveLanguage("de", "Немецкий");
        languageDAO.saveLanguage("no", "Норвежский");
        languageDAO.saveLanguage("fa", "Персидский");
        languageDAO.saveLanguage("pl", "Польский");
        languageDAO.saveLanguage("pt", "Португальский");
        languageDAO.saveLanguage("ro", "Румынский");
        languageDAO.saveLanguage("ru", "Русский");
        languageDAO.saveLanguage("sr", "Сербский");
        languageDAO.saveLanguage("sk", "Словацкий");
        languageDAO.saveLanguage("sl", "Словенский");
        languageDAO.saveLanguage("sw", "Суахили");
        languageDAO.saveLanguage("tg", "Таджикский");
        languageDAO.saveLanguage("th", "Тайский");
        languageDAO.saveLanguage("tl", "Тагальский");
        languageDAO.saveLanguage("tt", "Татарский");
        languageDAO.saveLanguage("tr", "Турецкий");
        languageDAO.saveLanguage("uz", "Узбекский");
        languageDAO.saveLanguage("uk", "Украинский");
        languageDAO.saveLanguage("fi", "Финский");
        languageDAO.saveLanguage("fr", "Французский");
        languageDAO.saveLanguage("hr", "Хорватский");
        languageDAO.saveLanguage("cs", "Чешский");
        languageDAO.saveLanguage("sv", "Шведский");
        languageDAO.saveLanguage("et", "Эстонский");
        languageDAO.saveLanguage("ja", "Японский");

        if (routeDAO == null) {
            setRoutesDAO();
        }
        routeDAO.saveRoute(new RouteDataSet("ru", "en"));
        routeDAO.saveRoute(new RouteDataSet("en", "ru"));
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DictionaryDataSet.class);
            TableUtils.createTable(connectionSource, RouteDataSet.class);
            TableUtils.createTable(connectionSource, LanguageDataSet.class);
            presetLanguages();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    @Override
    public String translate(String input, String sourceLanguage, String lang) throws SQLException {
        return dictionaryDAO.translate(input, lang);
    }

    @Override
    public void saveTranslate(String input, String result, String lang) throws SQLException {
        dictionaryDAO.saveTranslate(input, result, lang);
    }

    @Override
    public void clearDictionary() throws SQLException {
        dictionaryDAO.clearDictionary();
    }

    @Override
    public String getReduced(String language) throws SQLException{
        return languageDAO.getReduced(language);
    }

    @Override
    public void saveRoutes(ArrayList<String> routes) throws SQLException {
        for (String route: routes) {
            String from = route.substring(0,2);
            String to = route.substring(3,5);
            RouteDataSet temp = new RouteDataSet(from, to);
            if (languageDAO.isLanguageExist(from) &&
                    languageDAO.isLanguageExist(to) &&
                    !routeDAO.isExist(temp)) {
                routeDAO.saveRoute(temp);
            }
        }
    }

    @Override
    public ArrayList<String> getToLangPairedWithGivenLang(String lang) throws SQLException {
        ArrayList<RouteDataSet> temp = routeDAO.getToLangPairedWithFivenLang(lang);
        ArrayList <String> result = new ArrayList<>();
        for (RouteDataSet el: temp) {
            result.add(languageDAO.getFullName(el.getTo()));
        }
        Collections.sort(result);
        return result;
    }

    @Override
    public ArrayList<String> getAllFromLangs() throws SQLException {
        ArrayList <LanguageDataSet> temp = languageDAO.getAllLangs();
        ArrayList <String> result = new ArrayList<>();
        for (LanguageDataSet el: temp) {
            if (routeDAO.isExistRouteWithFrom(el.getReduced())) {
                result.add(el.getLanguage());
            }
        }
        return result;
    }

    @Override
    public ArrayList<Translate> getTranslations(String query) throws SQLException {
        ArrayList<Translate> result = new ArrayList<>();
        for (DictionaryDataSet translation: dictionaryDAO.translate(query)) {
            result.add(translation);
        }
        return result;
    }

    private synchronized void setDictionaryDAO() throws SQLException {
        dictionaryDAO = new DictionaryDAO(getConnectionSource(), DictionaryDataSet.class);
    }

    private synchronized void setRoutesDAO() throws SQLException {
        routeDAO = new RouteDAO(getConnectionSource(), RouteDataSet.class);
    }

    private synchronized void setLanguagesDAO() throws SQLException {
        languageDAO = new LanguageDAO(getConnectionSource(), LanguageDataSet.class);
    }
}