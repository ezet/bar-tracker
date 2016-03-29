package ezet.bartracker.models;

import android.hardware.*;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import ezet.bartracker.R;

import static android.content.Context.SENSOR_SERVICE;

@SuppressWarnings("Duplicates")
public class SensorFragment extends Fragment implements SensorEventListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    static final double mass = 1;
    static final double lowPassFilterAlpha = 0.8;
    static final double exponentialSmoothingAlpha = 0.99;
    static final double acceleration_threshold = 0.05d;
    static final int calibrationEventThreshold = 100;

    BarStats stats = new BarStats();
    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor linearAccelerometer;
    Sensor gravitySensor;

    TextView[] accelerationView = new TextView[3];
    TextView[] maxAccelerationView = new TextView[3];
    TextView[] minAccelerationView = new TextView[3];
//    TextView[] velocityVectorView = new TextView[3];
//    TextView[] smoothedVelocityView = new TextView[3];
    TextView avgSmoothedTotalVelocityView;
    TextView avgTotalVelocityView;
    TextView totalAccelerationView;
    TextView velocityView;
    TextView rawVelocityView;
//    TextView minTotalAccelerationView;
    TextView maxTotalAccelerationView;
    TextView distanceView;
    TextView forceView;
    ToggleButton button;

    int sensorEvents = 0;
    long timestamp;

    double distance;
    double force;

    double minTotalAcceleration;
    double maxTotalAcceleration;
    double avgSmoothedTotalVelocity = 0;
    double avgTotalVelocity = 0;
    double[] gravity = new double[3];
    double[] velocity = new double[3];
    double[] minAcceleration = new double[3];
    double[] maxAcceleration = new double[3];
    double[] smoothedVelocity = new double[3];
    private double liftStartThreshold = 1;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SensorFragment newInstance() {
        SensorFragment fragment = new SensorFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        // get views
        accelerationView[0] = (TextView)view.findViewById(R.id.accel_x);
        accelerationView[1] = (TextView)view.findViewById(R.id.accel_y);
        accelerationView[2] = (TextView)view.findViewById(R.id.accel_z);

        maxAccelerationView[0] = (TextView)view.findViewById(R.id.accel_x_max);
        maxAccelerationView[1] = (TextView)view.findViewById(R.id.accel_y_max);
        maxAccelerationView[2] = (TextView)view.findViewById(R.id.accel_z_max);

        minAccelerationView[0] = (TextView)view.findViewById(R.id.accel_x_min);
        minAccelerationView[1] = (TextView)view.findViewById(R.id.accel_y_min);
        minAccelerationView[2] = (TextView)view.findViewById(R.id.accel_z_min);

//        velocityVectorView[0] = (TextView)rootView.findViewById(R.id.velocity_x);
//        velocityVectorView[1] = (TextView)rootView.findViewById(R.id.velocity_y);
//        velocityVectorView[2] = (TextView)rootView.findViewById(R.id.velocity_z);
//
//        smoothedVelocityView[0] = (TextView)rootView.findViewById(R.id.velocity_x_avg);
//        smoothedVelocityView[1] = (TextView)rootView.findViewById(R.id.velocity_y_avg);
//        smoothedVelocityView[2] = (TextView)rootView.findViewById(R.id.velocity_z_avg);

//        minTotalAccelerationView = (TextView)rootView.findViewById(R.id.acceleration_min);
        maxTotalAccelerationView = (TextView)view.findViewById(R.id.acceleration_max);
        totalAccelerationView = (TextView)view.findViewById(R.id.acceleration_total);
        avgSmoothedTotalVelocityView = (TextView)view.findViewById(R.id.total_velocity_smoothed_avg);
        avgTotalVelocityView = (TextView)view.findViewById(R.id.total_velocity_avg);
        distanceView = (TextView)view.findViewById(R.id.distance);
        velocityView = (TextView)view.findViewById(R.id.velocity);
        rawVelocityView = (TextView)view.findViewById(R.id.raw_velocity);
        forceView = (TextView)view.findViewById(R.id.force);


        button = (ToggleButton) view.findViewById(R.id.button_toggle);
        button.setOnClickListener(clickListener);

        resetData();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init sensors
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.i("Accelerometer", "MinDelay: " + accelerometer.getMinDelay());
        linearAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (linearAccelerometer == null) {
            Log.i("Linear", "No linear accelerometer available");
        }
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (gravitySensor == null) {
            Log.i("Gravity", "No gravity sensor available");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        button.setChecked(false);
        unregisterListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetData();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(v instanceof ToggleButton)) return;
            ToggleButton button = (ToggleButton) v;
            if (button.isChecked()) {
                resetData();
                registerListener();
            }
            else unregisterListener();
        }
    };


    private void resetData() {
        stats.reset();
        maxTotalAcceleration = Double.MIN_VALUE;
        minTotalAcceleration = Double.MAX_VALUE;
        avgSmoothedTotalVelocityView.setText("Avg Smoothed Velocity: " + 0);
        avgTotalVelocityView.setText("Avg Velocity: " + 0);
        timestamp = 0;
        avgSmoothedTotalVelocity = 0;
        avgTotalVelocity = 0;
        sensorEvents = 0;
        distance = 0;
        for (int i = 0; i < 3; ++i) {
            String axis = map(i);
            velocity[i] = 0;
            gravity[i] = 0;
            smoothedVelocity[i] = 1;
//            smoothedVelocityView[i].setText(axis + 0);
            maxAcceleration[i] = Double.MIN_VALUE;
            minAcceleration[i] = Double.MAX_VALUE;
            maxAccelerationView[i].setText(axis + 0);
            minAccelerationView[i].setText(axis + 0);
//            velocityVectorView[i].setText(axis + 0);
            maxTotalAccelerationView.setText("" + 0);
//            minTotalAccelerationView.setText("" + 0);
        }
    }

    private void registerListener() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    private String map(int i) {
        switch (i) {
            case 0: return "X: ";
            case 1: return "Y: ";
            case 2: return "Z: ";
        }
        return null;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        ++sensorEvents;

        stats.analyze(new SensorData(event.values, event.timestamp));
        int index = stats.data.size()-1;

        accelerationView[0].setText("X: " + event.values[0]);
        accelerationView[1].setText("Y: " + event.values[1]);
        accelerationView[2].setText("Z: " + event.values[2]);

        if (sensorEvents < calibrationEventThreshold) return;

        maxTotalAccelerationView.setText("Max Acceleration: " + stats.maxRawAcceleration);

        totalAccelerationView.setText("Acceleration: " + stats.rawAcceleration.get(index).value);

        // total rawVelocity, running average
        double totalVelocity = stats.rawVelocity.get(index).value;
        avgTotalVelocity = MathHelper.runningAverage(avgTotalVelocity, totalVelocity, sensorEvents);
        avgTotalVelocityView.setText("Avg Raw Velocity: " + avgTotalVelocity);

        // smoothed total rawVelocity, running average
        double totalSmoothedVelocity = stats.adjustedVelocity.get(index).value;
        avgSmoothedTotalVelocity = MathHelper.runningAverage(avgSmoothedTotalVelocity, totalSmoothedVelocity, sensorEvents);
        avgSmoothedTotalVelocityView.setText("Avg Adjusted Velocity: " + avgSmoothedTotalVelocity);


        rawVelocityView.setText("Raw Velocity: " + stats.rawVelocity.get(index).value);
        velocityView.setText("Adjusted Velocity: " + stats.adjustedVelocity.get(index).value);

        forceView.setText("Force: " + stats.force.get(index).value);

        distanceView.setText("Distance: " + stats.distance);

    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
//
//        ++sensorEvents;
//
//        // Isolate the force of gravity with the low-pass filter.
//        MathHelper.lowPassFilter(gravity, event.values, gravity, lowPassFilterAlpha);
//
//        if (sensorEvents < calibrationEventThreshold) return;
//
//        // Remove the gravity contribution with the high-pass filter.
//        double[] linearAcceleration = new double[3];
//        MathHelper.highPassFilter(linearAcceleration, event.values, gravity);
//
//        int i;
//        int iLimit = linearAcceleration.length;
//        for (i = 0; i < iLimit; ++i) {
//            linearAcceleration[i] = event.values[i];
//            if (Math.abs(linearAcceleration[i]) < acceleration_threshold) linearAcceleration[i] = 0;
//            if (linearAcceleration[i] > maxRawAcceleration[i])  {
//                maxRawAcceleration[i] = linearAcceleration[i];
//                maxAccelerationView[i].setText("AccMax " + map(i) + linearAcceleration[i]);
//            }
//            if (linearAcceleration[i] < minRawAcceleration[i]) {
//                minRawAcceleration[i] = linearAcceleration[i];
//                minAccelerationView[i].setText("AccMin " + map(i) + linearAcceleration[i]);
//            }
//        }
//
//        double totalAcceleration = Math.sqrt(Math.pow(linearAcceleration[0],2)
//                + Math.pow(linearAcceleration[1], 2) + Math.pow(linearAcceleration[2], 2));
//
//        if (totalAcceleration > liftStartThreshold) {
//            startLift();
//        }
//
//        if (totalAcceleration < minTotalAcceleration) {
//            minTotalAcceleration = totalAcceleration;
////            minTotalAccelerationView.setText("Min Acceleration: " + minTotalAcceleration);
//        }
//        if (totalAcceleration > maxTotalAcceleration) {
//            maxTotalAcceleration = totalAcceleration;
//            maxTotalAccelerationView.setText("Max Acceleration: " + maxTotalAcceleration);
//        }
//        totalAccelerationView.setText("Acceleration: " + totalAcceleration);
//        accelerationView[0].setText("X: " + linearAcceleration[0]);
//        accelerationView[1].setText("Y: " + linearAcceleration[1]);
//        accelerationView[2].setText("Z: " + linearAcceleration[2]);
//
//
//        /* Velocity */
//        long dt = event.timestamp - timestamp;
////        Log.v("DT", "" + dt);
//        if (timestamp > 0) {
//            for (i = 0; i < 3; ++i) {
//                rawVelocity[i] = Math.abs(linearAcceleration[i]) * dt /1000000000.0d;
//                if (rawVelocity[i] == 0 && linearAcceleration[i] != 0) Log.w("Velocity", "error");
////                velocityVectorView[i].setText("Velocity " + map(i) + rawVelocity[i]);
//            }
//
//        }
//
//        // smooth rawVelocity
//        MathHelper.smoothExponential(adjustedVelocity, rawVelocity, adjustedVelocity, exponentialSmoothingAlpha);
//
//        // total rawVelocity, running average
//        double totalVelocity = rawVelocity[0] + rawVelocity[1] + rawVelocity[2];
//        velocityView.setText("Velocity: " + totalVelocity);
//        avgTotalVelocity = MathHelper.runningAverage(avgTotalVelocity, totalVelocity, sensorEvents);
//        avgTotalVelocityView.setText("Avg Velocity: " + avgTotalVelocity);
//
//        force = mass * totalAcceleration;
//        forceView.setText("Force: " + force);
//
//        // smoothed total rawVelocity, running average
//        double totalSmoothedVelocity = adjustedVelocity[0] + adjustedVelocity[1] + adjustedVelocity[2];
//        avgSmoothedTotalVelocity = MathHelper.runningAverage(avgSmoothedTotalVelocity, totalSmoothedVelocity, sensorEvents);
//        avgSmoothedTotalVelocityView.setText("Avg Smoothed Velocity: " + avgSmoothedTotalVelocity);
//
//        distance += totalVelocity * dt / 1000000000.0d;
//        distanceView.setText("Distance: " + distance);
//
//        for (i = 0; i < 3; ++i) {
////            smoothedVelocityView[i].setText("Smoothed Velocity " + map(i) + adjustedVelocity[i]);
//        }
//
//        timestamp = event.timestamp;
//    }

    private void startLift() {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
