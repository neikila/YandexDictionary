package yandex;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by neikila on 25.09.15.
 */
public interface YandexCommunicator {

    String translate(String inputLanguage, String outLanguage, String text) throws IOException;

    YandexResponseLanguage getDirLanguages() throws IOException;
}