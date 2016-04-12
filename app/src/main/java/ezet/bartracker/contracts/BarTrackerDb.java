package ezet.bartracker.contracts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ezet.bartracker.models.Exercise;
import ezet.bartracker.models.ExerciseSet;
import ezet.bartracker.models.SensorData;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by larsk on 05-Apr-16.
 */
public class BarTrackerDb extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "BarTracker.db";

    public static final String SQL_INSERT_EXERCISE =
            "INSERT INTO Exercise (name) VALUES ('Deadlift'), ('Squat'), ('Benchpress')";

    public static final String SQL_CREATE_EXERCISE = "CREATE TABLE `Exercise` (" +
            "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  `name` TEXT NOT NULL" +
            ");";

    public static final String SQL_CREATE_SET = "CREATE TABLE `Set` (" +
            "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  `date` DATETIME NOT NULL," +
            "  `weight` REAL NOT NULL," +
            "  `repetitions` INTEGER NOT NULL," +
            "  `notes` TEXT," +
            "  `exercise` INTEGER NOT NULL REFERENCES `Exercise` (`id`) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");" +
            "CREATE INDEX `idx_set__exercise` ON `Set` (`exercise`);";

    public static final String SQL_CREATE_SENSORDATA = "CREATE TABLE `SensorData` (" +
            "  `id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  `timestamp` INTEGER NOT NULL," +
            "  `x` REAL NOT NULL," +
            "  `y` REAL NOT NULL," +
            "  `z` REAL NOT NULL," +
            "  `set` INTEGER NOT NULL REFERENCES `Set` (`id`) ON DELETE CASCADE ON UPDATE CASCADE" +
            ");" +
            "CREATE INDEX `idx_sensordata__set` ON `SensorData` (`set`)";

    public static final String SQL_DROP_EXERCISE =
            "DROP TABLE IF EXISTS " + BarTrackerContract.ExerciseDef.TABLE;

    public static final String SQL_DROP_SET = "DROP TABLE IF EXISTS " + BarTrackerContract.SetDef.TABLE;

    public static final String SQL_DROP_SENSORDATA = "DROP TABLE IF EXISTS " + BarTrackerContract.SensorDataDef.TABLE;


    public BarTrackerDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EXERCISE);
        db.execSQL(SQL_INSERT_EXERCISE);
        db.execSQL(SQL_CREATE_SET);
        db.execSQL(SQL_CREATE_SENSORDATA);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_SENSORDATA);
        db.execSQL(SQL_DROP_SET);
        db.execSQL(SQL_DROP_EXERCISE);
        onCreate(db);
    }

    public Cursor getAllExercises() {
        String sql = "select t.* from " + BarTrackerContract.ExerciseDef.TABLE + " t";
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public Cursor getSetsByExerciseId(long exerciseId) {
        String sql = "SELECT * FROM " + BarTrackerContract.SetDef.TABLE + " WHERE exercise = " + exerciseId;
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public void insertSet(ExerciseSet set) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues setValues = new ContentValues();
            setValues.put(BarTrackerContract.SetDef.COL_DATE, SimpleDateFormat.getInstance().format(set.date));
            setValues.put(BarTrackerContract.SetDef.COL_WEIGHT, set.weight);
            setValues.put(BarTrackerContract.SetDef.COL_EXERCISE, set.exerciseId);
            // TODO: Calculate repetitions
            setValues.put(BarTrackerContract.SetDef.COL_REPETITIONS, set.repetitions);
            setValues.put(BarTrackerContract.SetDef.COL_NOTES, set.notes);
            set.id = db.insertOrThrow(BarTrackerContract.SetDef.TABLE, null, setValues);
            ContentValues sensorValues = new ContentValues();
            for (SensorData data : set.data) {
                sensorValues.put(BarTrackerContract.SensorDataDef.COL_TIMESTAMP, data.timestamp);
                sensorValues.put(BarTrackerContract.SensorDataDef.COL_X, data.values[0]);
                sensorValues.put(BarTrackerContract.SensorDataDef.COL_Y, data.values[1]);
                sensorValues.put(BarTrackerContract.SensorDataDef.COL_Z, data.values[2]);
                sensorValues.put(BarTrackerContract.SensorDataDef.COL_SET, set.id);
                data.id = db.insertOrThrow(BarTrackerContract.SensorDataDef.TABLE, null, sensorValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void loadSensorData(ExerciseSet set) {
        List<SensorData> data = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(false, BarTrackerContract.SensorDataDef.TABLE, null, BarTrackerContract.SensorDataDef.COL_SET + " = ?", new String[]{String.valueOf(set.id)}, null, null, null, null);
        while (cursor.moveToNext()) {
            data.add(SensorData.fromCursor(cursor));
        }
        set.data = data;
    }

    public boolean deleteSet(ExerciseSet set) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(BarTrackerContract.SetDef.TABLE, "id = ?", new String[] {String.valueOf(set.id)}) == 1;
    }

    public boolean insertExercise(Exercise exercise) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BarTrackerContract.ExerciseDef.COL_NAME, exercise.name);
        exercise.id = db.insertOrThrow(BarTrackerContract.ExerciseDef.TABLE, null, values);
        return exercise.id != -1;
    }

    public boolean deleteExercise(Exercise exercise) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(BarTrackerContract.ExerciseDef.TABLE, "id = ?", new String[] {String.valueOf(exercise.id)}) == 1;
    }
}
