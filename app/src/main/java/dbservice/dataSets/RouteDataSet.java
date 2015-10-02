package dbservice.dataSets;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ivan on 01.10.15
 */
@DatabaseTable(tableName = "Languages")
public class RouteDataSet {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "from")
    private String from;

    @DatabaseField(canBeNull = false, dataType = DataType.STRING, columnName = "to")
    private String to;

    public RouteDataSet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String language) {
        this.from = language;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

