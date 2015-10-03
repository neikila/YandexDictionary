package yandex;

import java.util.List;
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
    public String translate(String inLang, String outLang,String input) { return "Test value";}

    @Override
    public Vector<String> translateSeparately(String inputLanguage, String outLanguage, String text) {
        return result;
    }

}
