package yandex;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by ivansemenov on 04.10.15.
 */
public class YandexResponseLanguage {
    private ArrayList<String> dirs;
    public YandexResponseLanguage(ArrayList<String> dirs){
        this.dirs = dirs;
    }

    public ArrayList getArray(){
        return dirs;
    }
}
