package yandex;

import java.util.Vector;

/**
 * Created by neikila on 25.09.15.
 */
public class YandexCommunicatorStubImpl implements YandexCommunicator {
    private Vector<String> result;

    public YandexCommunicatorStubImpl() {
        result = new Vector<>();
        result.add("Second word");
        result.add("First word");
    }

    @Override
    public String translate(String input) {
        return "TestValue";
    }

    @Override
    public Vector<String> translateSeparately(String input) {
        return result;
    }
}
