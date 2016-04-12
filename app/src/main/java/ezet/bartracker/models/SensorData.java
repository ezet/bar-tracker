package ezet.bartracker.models;

import android.database.Cursor;
import ezet.bartracker.contracts.BarTrackerContract;

/**
 * Created by larsk on 26-Mar-16.
 */
public class SensorData {

    public final float[] values;
    public final long timestamp;
    public long id;

    public SensorData(long id, long timestamp, float[] values) {
        this.id = id;
        this.values = values;
        this.timestamp = timestamp;
    }

    public SensorData(long timestamp, float[] values) {
        this.id = 0;
        this.values = values;
        this.timestamp = timestamp;
    }

    public static SensorData fromCursor(Cursor cursor) {
        float[] values = new float[3];
        values[0] = cursor.getFloat(cursor.getColumnIndexOrThrow(BarTrackerContract.SensorDataDef.COL_X));
        values[1] = cursor.getFloat(cursor.getColumnIndexOrThrow(BarTrackerContract.SensorDataDef.COL_Y));
        values[2] = cursor.getFloat(cursor.getColumnIndexOrThrow(BarTrackerContract.SensorDataDef.COL_Z));
        long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(BarTrackerContract.SensorDataDef.COL_TIMESTAMP));
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(BarTrackerContract.SensorDataDef.COL_ID));
        return new SensorData(id, timestamp, values);
    }
}
