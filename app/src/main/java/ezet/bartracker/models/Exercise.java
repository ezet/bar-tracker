package ezet.bartracker.models;

import android.database.Cursor;

/**
 * A dummy item representing a piece of name.
 */
public class Exercise {
    public long id;
    public String name;

    public Exercise(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Exercise fromCursor(Cursor cursor) {
        return new Exercise(cursor.getInt(0), cursor.getString(1));
    }
}
