package ezet.bartracker.models;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by larsk on 30-Mar-16.
 */
public class Rep {

    public List<SensorData> data;

    public int id;

    public String name;

    public Timestamp timestamp;

    public Rep(List<SensorData> data) {
        this.data = data;
    }


}
