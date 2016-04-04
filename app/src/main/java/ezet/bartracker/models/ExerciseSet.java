package ezet.bartracker.models;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A dummy item representing a piece of name.
 */
public class ExerciseSet {
    public int id;
    public String name;
    public String details;
    public double weight = 100;
    public Date date = Calendar.getInstance().getTime();
    public String notes;
    public long duration = 21;
    public List<SensorData> data;

    public ExerciseSet(List<SensorData> data) {
        this.data = data;
        this.duration = (data.get(data.size()-1).timestamp - data.get(0).timestamp) / 1000000000;
    }

    public ExerciseSet(int id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }
}
