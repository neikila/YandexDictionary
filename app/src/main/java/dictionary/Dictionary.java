package dictionary;

import java.util.ArrayList;
import java.util.Vector;

import dbservice.DbService;
import dbservice.DbServiceStubImpl;
import yandex.YandexCommunicator;
import yandex.YandexCommunicatorStubImpl;

/**
 * Created by neikila on 25.09.15.
 */
public class Dictionary {
    private YandexCommunicator communicator;
    private DbService dbService;

    public Dictionary() {
        communicator = new YandexCommunicatorStubImpl();
        dbService = new DbServiceStubImpl();
    }

    private String reduce(String input) {
        // TODO убрать из слов знаки препинания слева и справа. Хотя возможно это впринципе избыточно
        return input;
    }

    public void translate(String input, Callback <String> setter) {
        input = reduce(input);
        String result;
        if ((result = dbService.translate(input)) == null) {
            result = communicator.translate(input);
            // TODO analyse of result
            dbService.save(input, result);
        }
        setter.setTranslation(result);
    }

    public void translateSeparately(String input, Callback <Vector <String>> setter) {
    }

    public ArrayList <String> getLanguages() {
        return dbService.getLanguages();
    }
}
