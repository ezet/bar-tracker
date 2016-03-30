package ezet.bartracker.models;

/**
 * A dummy item representing a piece of name.
 */
public class Exercise {
    public final int id;
    public final String name;
    public final String details;

    public Exercise(int id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    @Override
    public String toString() {
        return name;
    }
}
