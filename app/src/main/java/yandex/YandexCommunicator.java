package yandex;

import java.util.Vector;

/**
 * Created by neikila on 25.09.15.
 */
public interface YandexCommunicator {
    // TODO тут еще придется разобраться ибо асинхроненько нужно это сделать (см лекция
    String translate(String input);

    Vector<String> translateSeparately(String input);
}