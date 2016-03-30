package ezet.bartracker.activities.adapters;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import ezet.bartracker.models.SensorData;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by larsk on 30-Mar-16.
 */
public class AccelerometerListener implements SensorEventListener {

    public List<SensorData> sensorData = new LinkedList<>();
    private SensorManager manager;
    private Sensor sensor;



    public AccelerometerListener(SensorManager sensorManager) {
        this.manager = sensorManager;
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void register() {
        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregister() {
        manager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        sensorData.add(new SensorData(event.values.clone(), event.timestamp));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
