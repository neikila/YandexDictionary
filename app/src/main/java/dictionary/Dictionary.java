package dictionary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import com.squareup.otto.Bus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dbservice.DBHelperFactory;
import dbservice.DBService;
import dbservice.DBServiceStubImpl;
import utils.MessageKey;
import yandex.YandexCommunicator;
import yandex.YandexCommunicatorImpl;
import yandex.YandexCommunicatorStubImpl;
import utils.ErrorTypes;

/**
 * Created by neikila on 25.09.15.
 */
public class Dictionary {
    private static Dictionary dictionary = null;
    private YandexCommunicator communicator;
    private DBService dbService;
    private Bus bus;

    private Dictionary() {
        communicator = new YandexCommunicatorImpl();
        dbService = DBHelperFactory.getHelper();
//        dbService = new DBServiceStubImpl();

    }

    public static Dictionary getInstance() {
        if (dictionary == null) {
            dictionary = new Dictionary();
        }
        return dictionary;
    }

    public ArrayList<String> getLanguages() {
        try {
            return dbService.getAllLangs();
        } catch (SQLException e) {
            // TODO обработка exception
            return null;
        }
    }

    public ArrayList<String> getToLangPairedWithGivenLang(String lang) {
        try {
            return dbService.getToLangPairedWithGivenLang(lang);
        } catch (SQLException e) {
            // TODO log
            return null;
        }
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    private String reduce(String input) {
        // TODO убрать из слов знаки препинания слева и справа. Хотя возможно это впринципе избыточно
        return input;
    }

    public void translate(String input, String targetLanguage) {
        input = reduce(input);
        new TranslateWordAsyncTask().execute(input, targetLanguage);
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
                    try {
                        result = communicator.translate("en","ru",input); //TODO Hardcode
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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