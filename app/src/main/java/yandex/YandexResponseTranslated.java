package yandex;

import java.util.ArrayList;

/**
 * Created by ivansemenov on 04.10.15.
 */
public class YandexResponseTranslated {
    String code;
    String lang;
    String message;
    ArrayList<String> text;

    public YandexResponseTranslated(String code, String lang, ArrayList<String> text){
        this.code = code;
        this.lang = lang;
        this.text = text;
    }

    public YandexResponseTranslated(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getText(){
        return this.text.get(0);
    }
}
