package ezet.bartracker.models;

import java.util.List;

/**
 * A dummy item representing a piece of name.
 */
public class ExerciseSet {
    public int id;
    public String name;
    public String details;
    private List<SensorData> data;

    public ExerciseSet(List<SensorData> data) {
        this.data = data;
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
