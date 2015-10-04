package yandex;

import java.util.ArrayList;

/**
 * Created by ivansemenov on 04.10.15.
 */
public class Dir {
    private String inputLanguage;
    private String outputLanguage;
    public Dir(String inOutLang){

        this.inputLanguage = inOutLang.substring(0,1);
        this.outputLanguage = inOutLang.substring(3,4);
    }

    public String getInputLanguage(){
        return this.inputLanguage;
    }

    public String getOutputLanguage(){
        return this.outputLanguage;
    }
}
