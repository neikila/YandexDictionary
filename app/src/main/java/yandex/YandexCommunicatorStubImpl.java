package yandex;

import java.util.Vector;

/**
 * Created by neikila on 25.09.15.
 */
public class YandexCommunicatorStubImpl implements YandexCommunicator {
    private Vector<String> result;
    final private String YANDEX_KEY = "trnsl.1.1.20150930T163208Z.45fb59a5f5708995.975b4f03544ec40e09e125fcdd1668501bc3398e";
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
