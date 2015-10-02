package dbservice.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dbservice.dataSets.DictionaryDataSet;

/**
 * Created by ivan on 01.10.15
 */
public class DictionaryDAO extends BaseDaoImpl<DictionaryDataSet, Integer> {

    private Map<String, String> languages = new HashMap<>();

    public DictionaryDAO(ConnectionSource connectionSource,
                         Class<DictionaryDataSet> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        languages.put("en", "английский");
        languages.put("sq", "албанский");
        languages.put("ar", "арабский");
        languages.put("hy", "армянский");
        languages.put("az", "азербайджанский");
        languages.put("af", "африкаанс");
        languages.put("eu", "баскский");
        languages.put("be", "белорусский");
        languages.put("bg", "болгарский");
        languages.put("bs", "боснийский");
        languages.put("cy", "валлийский");
        languages.put("vi", "вьетнамский");
        languages.put("hu", "венгерский");
        languages.put("ht", "гаитянский");
        languages.put("id", "индонезийский");
        languages.put("mg", "малагасийский");
        languages.put("pt", "португальский");
        languages.put("gl", "галисийский");
        languages.put("nl", "голландский");
        languages.put("el", "греческий");
        languages.put("ka", "грузинский");
        languages.put("da", "датский");
        languages.put("he", "иврит");
        languages.put("ga", "ирландский");
        languages.put("it", "итальянский");
        languages.put("is", "исландский");
        languages.put("es", "испанский");
        languages.put("kk", "казахский");
        languages.put("ca", "каталанский");
        languages.put("ky", "киргизский");
        languages.put("zh", "китайский");
        languages.put("ko", "корейский");
        languages.put("la", "латынь");
        languages.put("lv", "латышский");
        languages.put("lt", "литовский");
        languages.put("ms", "малайский");
        languages.put("mt", "мальтийский");
        languages.put("mk", "македонский");
        languages.put("mn", "монгольский");
        languages.put("de", "немецкий");
        languages.put("no", "норвежский");
        languages.put("fa", "персидский");
        languages.put("pl", "польский");
        languages.put("ro", "румынский");
        languages.put("ru", "русский");
        languages.put("sr", "сербский");
        languages.put("sk", "словацкий");
        languages.put("sl", "словенский");
        languages.put("sw", "суахили");
        languages.put("tg", "таджикский");
        languages.put("th", "тайский");
        languages.put("tl", "тагальский");
        languages.put("tt", "татарский");
        languages.put("tr", "турецкий");
        languages.put("uz", "узбекский");
        languages.put("uk", "украинский");
        languages.put("fi", "финский");
        languages.put("fr", "французский");
        languages.put("hr", "хорватский");
        languages.put("cs", "чешский");
        languages.put("sv", "шведский");
        languages.put("et", "эстонский");
        languages.put("ja", "японский");
    }

    public Map<String, String> getLanguages() {
        return languages;
    }
}
