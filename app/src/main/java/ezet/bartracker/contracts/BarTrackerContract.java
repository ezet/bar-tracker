package ezet.bartracker.contracts;

import android.provider.BaseColumns;

/**
 * Created by larsk on 05-Apr-16.
 */
public class BarTrackerContract {

    private BarTrackerContract() {

    }

    public static abstract class ExerciseDef implements BaseColumns {
        public static final String TABLE = "`Exercise`";
        public static final String COL_ID = "id";
        public static final String COL_NAME = "name";
    }

    public static abstract class SetDef implements BaseColumns {
        public static final String TABLE = "`Set`";
        public static final String COL_ID = "id";
        public static final String COL_WEIGHT = "weight";
        public static final String COL_REPETITIONS = "repetitions";
        public static final String COL_EXERCISE = "exercise";
        public static final String COL_DATE = "date";
        public static final String COL_NOTES = "notes";
    }

    public static abstract class SensorDataDef implements BaseColumns {
        public static final String TABLE = "`SensorData`";
        public static final String COL_ID = "id";
        public static final String COL_TIMESTAMP = "timestamp";
        public static final String COL_X = "x";
        public static final String COL_Y = "y";
        public static final String COL_Z = "z";
        public static final String COL_SET = "`set`";
    }
}
