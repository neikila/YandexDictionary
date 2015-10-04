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
            return dbService.getToLangPairedWithGivenLang(dbService.getReduced(lang));
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

    public void translate(String input, String sourceLanguage, String targetLanguage) {
        input = reduce(input);
        new TranslateWordAsyncTask().execute(input, sourceLanguage, targetLanguage);
    }

    public void translateSeparately(String input) {
    }

    public class TranslateWordAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String input = params[0];
            String sourceLanguage = params[1];
            String targetLanguage = params[2];
            String result;

            Message msg = new Message();
            Bundle bundle = new Bundle();


            try {
                String from = dbService.getReduced(sourceLanguage);
                String to = dbService.getReduced(targetLanguage);

                if ((result = dbService.translate(input, from, to)) == null) {
                    try {
                        result = communicator.translate(from, to, input);
                        // TODO analyse of result
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dbService.saveTranslate(input, result, to);
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