package dbservice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dbservice.dataSets.RouteDataSet;
import yandex.YandexCommunicator;
import yandex.YandexCommunicatorStubImpl;

/**
 * Created by neikila on 25.09.15.
 */
public class DBServiceStubImpl implements DBService {
    private Map<String, String> dictionary;

    public DBServiceStubImpl() {
        dictionary = new HashMap<>();
        dictionary.put("Hello", "Привет");
        dictionary.put("hello", "привет");
        dictionary.put("friend", "друг");
        dictionary.put("Привет", "Hello");
        dictionary.put("привет", "hello");
    }

    @Override
    public String translate(String input, String from, String to) {
        return dictionary.get(input);
    }

    @Override
    public void saveTranslate(String input, String result, String to) {
        dictionary.put(input, result);
    }

    @Override
    public void clearDictionary() {
    }

    @Override
    public String getReduced(String language) throws SQLException {
        String result = null;
        switch (language) {
            case "Русский": result = "ru"; break;
            case "Английский": result = "en"; break;
        }
        return null;
    }

    @Override
    public void saveRoutes(ArrayList<String> routes) {
    }

    @Override
    public ArrayList<String> getToLangPairedWithGivenLang(String language) throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        switch (language) {
            case "Тагальский":
                result.add("Каталанский");
                result.add("Русский");
                break;
            case "Русский":
                result.add("Тагальский");
                result.add("Каталанский");
                break;
            case "Каталанский":
                result.add("Тагальский");
                result.add("Русский");
                break;
        }
        return result;
    }

    @Override
    public ArrayList<String> getAllLangs() {
        ArrayList<String> result = new ArrayList<>();
        result.add("Каталанский");
        result.add("Тагальский");
        result.add("Русский");
        return result;
    }

    @Override
    public ArrayList<String> getTranslations(String query) throws SQLException {
        ArrayList <String> result = new ArrayList<>();
        result.add("Word 1");
        result.add("Word 2");
        return result;
    }
}