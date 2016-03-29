package ezet.bartracker.models;

/**
 * Created by larsk on 26-Mar-16.
 */
public class SensorData {

    public final float[] values;
    public final long timestamp;

    public SensorData(float[] values, long timestamp) {
        this.values = values;
        this.timestamp = timestamp;
    }

}
