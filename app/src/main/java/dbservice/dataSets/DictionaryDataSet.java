package dbservice.dataSets;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ivan on 01.10.15
 */
@DatabaseTable(tableName = "Dictionary")
public class DictionaryDataSet {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "word")
    private String word;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "translate")
    private String translate;


    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "language")
    private String language;

    public DictionaryDataSet() {
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
}
