package dbservice.dataSets;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import dbservice.Translate;
import dictionary.Dictionary;

/**
 * Created by ivan on 01.10.15
 */
@DatabaseTable(tableName = "Dictionary")
public class DictionaryDataSet implements Translate {
    public final static String WORD = "word";
    public final static String TRANSLATE = "translate";
    public final static String LANGUAGE = "language";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = WORD)
    private String word;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = TRANSLATE)
    private String translate;


    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = LANGUAGE)
    private String language;

    public DictionaryDataSet() {
    }

    public DictionaryDataSet(String word, String translate, String language) {
        this.word = word;
        this.translate = translate;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }
}