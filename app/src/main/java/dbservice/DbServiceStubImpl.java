package dbservice;

import java.util.HashMap;
import java.util.Map;

import yandex.YandexCommunicator;
import yandex.YandexCommunicatorStubImpl;

/**
 * Created by neikila on 25.09.15.
 */
public class DbServiceStubImpl implements DbService{
    private Map<String, String> languages;
    private HashMap <String, String> dictionary;
    private YandexCommunicator communicator;

    public DbServiceStubImpl() {
        communicator = new YandexCommunicatorStubImpl();

        languages = new HashMap<>();
        languages.put("Russian", "ru");
        languages.put("English", "en");
        languages.put("German", "de");
        languages.put("Spanish", "es");

        dictionary = new HashMap<>();
        dictionary.put("Hello", "Привет");
        dictionary.put("hello", "привет");
        dictionary.put("friend", "друг");
        dictionary.put("Привет", "Hello");
        dictionary.put("привет", "hello");
    }

    @Override
    public Map<String, String> getLanguages() {
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

    @Override
    public void clear() {
    }

    @Override
    public String getReduced(String language) {
        return null;
    }
}