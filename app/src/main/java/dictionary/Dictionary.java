package dictionary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import com.development.forty_two.myapplication.MainActivity;
import com.squareup.otto.Bus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dbservice.DBService;
import dbservice.DBServiceStubImpl;
import utils.MessageKey;
import yandex.YandexCommunicator;
import yandex.YandexCommunicatorStubImpl;
import utils.ErrorTypes;

/**
 * Created by neikila on 25.09.15.
 */
public class Dictionary {
    private YandexCommunicator communicator;
    private DBService dbService;
    private Bus bus;
    private Map<String, String> languages = new HashMap<>();

    public Dictionary() {
        communicator = new YandexCommunicatorStubImpl();
        dbService = new DBServiceStubImpl();
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

    public ArrayList<String> getLanguages() {
        return new ArrayList<>(languages.values());
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    private String reduce(String input) {
        // TODO убрать из слов знаки препинания слева и справа. Хотя возможно это впринципе избыточно
        return input;
    }

    public void translate(String input) {
        input = reduce(input);
        new TranslateWordAsyncTask().execute(input);
    }

    public void translateSeparately(String input) {
    }

    public class TranslateWordAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String input = params[0];
            String targetLanguage = params[1];
            String result;
            Message msg = new Message();
            Bundle bundle = new Bundle();

            try {
                if ((result = dbService.translate(input, targetLanguage)) == null) {
                    result = communicator.translate(input);
                    // TODO analyse of result
                    dbService.saveTranslate(input, result, targetLanguage);
                }
                bundle.putString(MessageKey.KEY.toString(), MessageKey.TRANSLATE_IS_READY.toString());
                bundle.putString(MessageKey.TRANSLATE_RESULT.toString(), result);
            } catch (SQLException e) {
                bundle.putString(MessageKey.KEY.toString(), MessageKey.ERROR.toString());
                bundle.putString(MessageKey.ERROR_TYPE.toString(), ErrorTypes.SqlError.toString());
            }

            msg.setData(bundle);
            bus.post(msg);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}