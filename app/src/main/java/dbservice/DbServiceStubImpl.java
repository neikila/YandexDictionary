package dbservice;

import java.util.ArrayList;

/**
 * Created by neikila on 25.09.15.
 */
public class DbServiceStubImpl implements DbService{
    private ArrayList<String> languages;

    public DbServiceStubImpl() {
        languages = new ArrayList<>();
        languages.add("Russian");
        languages.add("English");
        languages.add("German");
        languages.add("Spanish");
    }

    @Override
    public ArrayList<String> getLanguages() {
        return languages;
    }
}
