package yandex;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import retrofit.RestAdapter;
import retrofit.client.Response;

/**
 * Created by ivansemenov on 02.10.15.
 */
public class YandexCommunicatorImpl implements YandexCommunicator {
    private Vector<String> result;
    private List<String> listLang;
    final private String YANDEX_KEY = "trnsl.1.1.20150930T163208Z.45fb59a5f5708995.975b4f03544ec40e09e125fcdd1668501bc3398e";
    private YandexAPI service;
    private RestAdapter retrofit;
    public YandexCommunicatorImpl(){
        retrofit = new RestAdapter.Builder().setEndpoint("https://translate.yandex.net/api/v1.5/tr.json").build();
        result = new Vector<>();
        listLang = new ArrayList<>();
        service = retrofit.create(YandexAPI.class);
    }

    @Override
    public String translate(String inLang, String outLang, String input) {

        String lang = inLang+"-"+outLang;
        YandexResponseTranslated tmp = service.translate(YANDEX_KEY,input,lang);
        return tmp.getText();
        //TODO Для проверки выводил полученный список направлений
        /*YandexResponseLanguage tmp = service.langList(YANDEX_KEY);

        return Arrays.toString(tmp.getArray().toArray());*/
    }
    @Override
    public Vector<String> translateSeparately(String inputLanguage, String outLanguage, String text) {

        return result;
    };
}
