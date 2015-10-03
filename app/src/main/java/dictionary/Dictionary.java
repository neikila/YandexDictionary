package dictionary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import com.development.forty_two.myapplication.ApplicationModified;
import com.development.forty_two.myapplication.MainActivity;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import dbservice.DbService;
import dbservice.DbServiceStubImpl;
import yandex.YandexCommunicator;
import yandex.YandexCommunicatorImpl;
import yandex.YandexCommunicatorStubImpl;

/**
 * Created by neikila on 25.09.15.
 */
public class Dictionary {
    private YandexCommunicator communicator;
    private DbService dbService;
    private Bus bus;

    public Dictionary() {
        communicator = new YandexCommunicatorImpl();
        dbService = new DbServiceStubImpl();
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

    public ArrayList <String> getLanguages() {
        return dbService.getLanguages();
    }

    public class TranslateWordAsyncTask extends AsyncTask <String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String input = params[0];
            String result;
            if ((result = dbService.translate(input)) == null) {
                try {
                    result = communicator.translate("en","ru",input); //TODO Hardcode
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // TODO analyse of result
                dbService.save(input, result);
            }
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.KEY, MainActivity.TRANSLATE_IS_READY);
            bundle.putString(MainActivity.TRANSLATE_RESULT, result);
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