package dbservice;

import java.util.Map;

/**
 * Created by neikila on 25.09.15.
 */

//TODO заменить интерфейс на абстрактный класс и наследоваться от OrmLiteSqliteOpenHelper?
public interface DbService {
    Map<String, String> getLanguages();

    String translate(String input);

    void save(String input, String result);

    String getReduced(String language);

    void clear();
}
