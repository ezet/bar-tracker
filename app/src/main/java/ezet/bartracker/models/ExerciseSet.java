package ezet.bartracker.models;

import android.database.Cursor;
import ezet.bartracker.contracts.BarTrackerContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A dummy item representing a piece of name.
 */
public class ExerciseSet {
    public long id;
    public double weight = 100;
    public Date date = Calendar.getInstance().getTime();
    public String notes;
    public long duration = 21;
    public List<SensorData> data;
    public long exerciseId;
    public int repetitions;

    public ExerciseSet() {
    }

    public ExerciseSet(int id) {
        this.id = id;
    }

    public ExerciseSet(List<SensorData> data) {
        this.data = data;
        this.duration = (data.get(data.size()-1).timestamp - data.get(0).timestamp) / 1000000000;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public static ExerciseSet fromCursor(Cursor cursor) {
        ExerciseSet s = new ExerciseSet(cursor.getInt(cursor.getColumnIndexOrThrow(BarTrackerContract.SetDef.COL_ID)));
        s.weight = cursor.getFloat(cursor.getColumnIndexOrThrow(BarTrackerContract.SetDef.COL_WEIGHT));
        String dateString = cursor.getString(cursor.getColumnIndexOrThrow(BarTrackerContract.SetDef.COL_DATE));
        try {
            s.date = new SimpleDateFormat().parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }
}
