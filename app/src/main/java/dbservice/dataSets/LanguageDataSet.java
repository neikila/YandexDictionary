package dbservice.dataSets;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ivan on 01.10.15
 */
@DatabaseTable(tableName = "Languages")
public class LanguageDataSet {
    public static final String REDUCED = "reduced";
    public static final String LANGUAGE = "to";

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = REDUCED)
    private String reduced;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = LANGUAGE)
    private String language;

    public LanguageDataSet() {
    }

    public LanguageDataSet(String reduced, String language) {
        this.reduced = reduced;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReduced() {
        return reduced;
    }

    public String getLanguage() {
        return language;
    }

    public void setReduced(String reduced) {
        this.reduced = reduced;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

