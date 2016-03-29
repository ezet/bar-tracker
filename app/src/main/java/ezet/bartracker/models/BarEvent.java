package ezet.bartracker.models;

/**
 * Created by larsk on 26-Mar-16.
 */
public class BarEvent {

    public final double value;
    public final float timestamp;

    public BarEvent(double value, float timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }
}
