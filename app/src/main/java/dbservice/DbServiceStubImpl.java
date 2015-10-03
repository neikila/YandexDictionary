package dbservice;

import java.util.ArrayList;
import java.util.HashMap;

import yandex.YandexCommunicator;
import yandex.YandexCommunicatorImpl;
import yandex.YandexCommunicatorStubImpl;

/**
 * Created by neikila on 25.09.15.
 */
public class DbServiceStubImpl implements DbService{
    private ArrayList<String> languages;
    private HashMap <String, String> dictionary;

    public DbServiceStubImpl() {

        languages = new ArrayList<>();
        languages.add("Russian");
        languages.add("English");
        languages.add("German");
        languages.add("Spanish");

        dictionary = new HashMap<>();
        dictionary.put("Hello", "Привет");
        dictionary.put("hello", "привет");
        dictionary.put("friend", "друг");
        dictionary.put("Привет", "Hello");
        dictionary.put("привет", "hello");
    }

    @Override
    public ArrayList<String> getLanguages() {
        return languages;
    }

    @Override
    public String translate(String input) {
        return dictionary.get(input);
    }

    @Override
    public void save(String input, String result) {
        dictionary.put(input, result);
    }
}