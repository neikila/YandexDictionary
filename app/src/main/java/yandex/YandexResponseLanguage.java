package yandex;

import java.util.ArrayList;

/**
 * Created by ivansemenov on 04.10.15.
 */
public class YandexResponseLanguage {
    private ArrayList<String> dirs;
    private ArrayList<Dir> dirObjects;
    public YandexResponseLanguage(ArrayList<String> dirs){
        this.dirs = dirs;
        dirObjects = new ArrayList<Dir>();
        for (String dir: this.dirs) {
            dirObjects.add(new Dir(dir));
        }
    }

    public ArrayList getArray(){
        return dirs;
    }
}
