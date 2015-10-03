package yandex;

import java.util.ArrayList;

/**
 * Created by ivansemenov on 04.10.15.
 */
public class Dir {
    String inputLanguage;
    String ouptutLanguage;
    public Dir(String inOutLang){

        this.inputLanguage = inOutLang.substring(0,1);
        this.ouptutLanguage = inputLanguage.substring(3,4);
    }
}
