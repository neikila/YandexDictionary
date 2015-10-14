package dictionary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import com.squareup.otto.Bus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import dbservice.DBHelperFactory;
import dbservice.DBService;
import dbservice.Translate;
import retrofit.RetrofitError;
import utils.MessageKey;
import yandex.YandexCommunicator;
import yandex.YandexCommunicatorImpl;
import utils.ErrorTypes;

/**
 * Created by neikila on 25.09.15.
 */
public class Dictionary {
    private static Dictionary dictionary = null;
    private YandexCommunicator communicator;
    private DBService dbService;
    private Bus bus;
    private ArrayList <String> prepared;

    private Dictionary() {
        communicator = new YandexCommunicatorImpl();
        dbService = DBHelperFactory.getHelper();
        try {
            prepared = dbService.getAllFromLangs();
        } catch (SQLException e) {
            prepared = new ArrayList<>();
        }
    }

    public static Dictionary getInstance() {
        if (dictionary == null) {
            dictionary = new Dictionary();
        }
        return dictionary;
    }

    public void clearDict() {
        new ClearAsyncTask().execute();
    }

    public void updateRoutes() {
        new GetDirectionsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public ArrayList<String> getFromLanguages() {
        return prepared;
    }

    public ArrayList<String> getToLangPairedWithGivenLang(String lang) {
        try {
            return dbService.getToLangPairedWithGivenLang(dbService.getReduced(lang));
        } catch (SQLException e) {
            // Error there is nothing we can do with it
            return null;
        }
    }

    public void setBus(Bus bus) {
        if (this.bus == null) {
            this.bus = bus;
        }
    }

    public ArrayList<Translate> getTranslations(String query) {
        try {
            return dbService.getTranslations(query);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public void translate(String input, String sourceLanguage, String targetLanguage) {
        new TranslateWordAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, input, sourceLanguage, targetLanguage);
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
                String from;
                if (sourceLanguage.equals("")) {
                    from = "";
                } else {
                    from = dbService.getReduced(sourceLanguage);
                }
                String to = dbService.getReduced(targetLanguage);

                if (from.equals("") || (result = dbService.translate(input, from, to)) == null) {
                    result = communicator.translate(from, to, input);
                    dbService.saveTranslate(input, result, to);
                }
                bundle.putString(MessageKey.KEY.toString(), MessageKey.TRANSLATE_IS_READY.toString());
                bundle.putString(MessageKey.TRANSLATE_RESULT.toString(), result);
            }
            catch (IOException e) {
                bundle.putString(MessageKey.KEY.toString(), MessageKey.ERROR.toString());
                bundle.putString(MessageKey.ERROR_TYPE.toString(), ErrorTypes.TranslationError.toString());
            }
            catch (SQLException e) {
                bundle.putString(MessageKey.KEY.toString(), MessageKey.ERROR.toString());
                bundle.putString(MessageKey.ERROR_TYPE.toString(), ErrorTypes.SqlError.toString());
            }
            catch (RetrofitError e) {
                bundle.putString(MessageKey.KEY.toString(), MessageKey.ERROR.toString());
                bundle.putString(MessageKey.ERROR_TYPE.toString(), ErrorTypes.NoInternet.toString());
            }
            msg.setData(bundle);
            bus.post(msg);
            return null;
        }
    }

    public class GetDirectionsAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Message msg = new Message();
            Bundle bundle = new Bundle();
            try {
                dbService.saveRoutes(communicator.getDirLanguages().getArray());
                prepared = dbService.getAllFromLangs();
                bundle.putString(MessageKey.KEY.toString(), MessageKey.UPDATE_ROUTES.toString());
            }
            catch (SQLException e) {
                bundle.putString(MessageKey.KEY.toString(), MessageKey.ERROR.toString());
                bundle.putString(MessageKey.ERROR_TYPE.toString(), ErrorTypes.SqlError.toString());
            }
            catch (RetrofitError e) {
                bundle.putString(MessageKey.KEY.toString(), MessageKey.ERROR.toString());
                bundle.putString(MessageKey.ERROR_TYPE.toString(), ErrorTypes.NoInternetCantUpdateRoutes.toString());
            }
            catch (IOException e) {
                bundle.putString(MessageKey.KEY.toString(), MessageKey.ERROR.toString());
                bundle.putString(MessageKey.ERROR_TYPE.toString(), ErrorTypes.TranslationError.toString());
            }
            msg.setData(bundle);
            bus.post(msg);
            return null;
        }
   }


    private class ClearAsyncTask extends AsyncTask <Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                dbService.clearDictionary();
            } catch (SQLException e) {
                // Error, There is nothing we can do with it
            }
            return null;
        }
    }
}