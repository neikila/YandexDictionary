package dbservice;

import java.util.ArrayList;

/**
 * Created by neikila on 25.09.15.
 */
public interface DbService {
    ArrayList <String> getLanguages();

    String translate(String input);

    void save(String input, String result);
}
