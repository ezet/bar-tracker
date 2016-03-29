package ezet.bartracker.activities.fragments;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import ezet.bartracker.models.BarEvent;
import ezet.bartracker.models.BarStats;
import ezet.bartracker.R;
import ezet.bartracker.models.SensorData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

@SuppressWarnings("Duplicates")
public class AnalyzerFragment extends Fragment implements SensorEventListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final int eventRate = 20;



    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor linearAccelerometer;
    private Sensor gravitySensor;

    private TextView avgVelocity;
    private TextView minVelocity;
    private TextView maxVelocity;

    private LineChart accelerationChart;
    private ToggleButton button;

    private List<SensorData> sensorData = new LinkedList<>();


    private double liftStartThreshold = 1;
    private LineChart velocityChart;
    private LineChart powerChart;
    private LineChart forceChart;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AnalyzerFragment newInstance() {
        AnalyzerFragment fragment = new AnalyzerFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analyzer, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        // get views

        button = (ToggleButton) rootView.findViewById(R.id.button_toggle);
        button.setOnClickListener(clickListener);
        accelerationChart = (LineChart) rootView.findViewById(R.id.acceleration_chart);
        velocityChart = (LineChart) rootView.findViewById(R.id.velocity_chart);
        powerChart = (LineChart) rootView.findViewById(R.id.power_chart);
        forceChart = (LineChart) rootView.findViewById(R.id.force_chart);


        avgVelocity = (TextView) rootView.findViewById(R.id.average_velocity);
        maxVelocity = (TextView) rootView.findViewById(R.id.max_velocity);
        minVelocity = (TextView) rootView.findViewById(R.id.min_velocity);

        resetData();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(v instanceof ToggleButton)) return;
            ToggleButton button = (ToggleButton) v;
            if (button.isChecked()) {
                resetData();
                registerListener();
            } else {
                unregisterListener();
                analyzeData(sensorData);
            }
        }
    };


    private void resetData() {
        sensorData.clear();
    }

    private void registerListener() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    private void analyzeData(List<SensorData> data) {

        BarStats stats = new BarStats();
        stats.analyze(data);

        List<Entry> rawAccelerationValues = new ArrayList<>();
        List<Entry> velocityValues = new ArrayList<>();
        List<Entry> adjustedVelocityValues = new ArrayList<>();
        List<Entry> forceValues = new ArrayList<>();
        List<Entry> powerValues = new ArrayList<>();
        List<String> xValues = new ArrayList<>();

        int limit = stats.rawAcceleration.size();
        for (int i = 0; i < limit; ++i) {
            if (i % eventRate != 0) continue;

            /* rawAcceleration */
            BarEvent event = stats.rawAcceleration.get(i);
            rawAccelerationValues.add(new Entry((float) event.value, i / eventRate));


            /* rawVelocity */
            event = stats.rawVelocity.get(i);
            velocityValues.add(new Entry((float) event.value, i / eventRate));

            //adjusted velocity
            event = stats.adjustedVelocity.get(i);
            adjustedVelocityValues.add(new Entry((float) event.value, i / eventRate));

            // force
            event = stats.force.get(i);
            forceValues.add(new Entry((float) event.value, i/eventRate));

            //power
            event = stats.power.get(i);
            powerValues.add(new Entry((float) event.value, i/eventRate));


            /* x axis */
            xValues.add(String.format("%.2f", event.timestamp / 1000000000.0f));
        }

        LineDataSet acceleration = new LineDataSet(rawAccelerationValues, "rawAcceleration (m/s^2)");
        LineDataSet velocity = new LineDataSet(velocityValues, "rawVelocity (m/s)");
        LineDataSet adjustedVelocity = new LineDataSet(adjustedVelocityValues, "adjustedVelocity (m/s)");
        adjustedVelocity.setAxisDependency(YAxis.AxisDependency.RIGHT);

        LineDataSet force = new LineDataSet(forceValues, "Force (N)");
        LineDataSet power = new LineDataSet(powerValues, "Power (Watt)");

        List<ILineDataSet> velocitySet = new LinkedList<>();
        velocitySet.add(velocity);
        velocitySet.add(adjustedVelocity);

        accelerationChart.setData(new LineData(xValues, acceleration));
        accelerationChart.notifyDataSetChanged();
        accelerationChart.invalidate();

        velocityChart.setData(new LineData(xValues, velocitySet));
        velocityChart.notifyDataSetChanged();
        velocityChart.invalidate();

        forceChart.setData(new LineData(xValues, force));
        forceChart.notifyDataSetChanged();
        forceChart.invalidate();

        powerChart.setData(new LineData(xValues, power));
        powerChart.notifyDataSetChanged();
        powerChart.invalidate();

        avgVelocity.setText(getString(R.string.average_velocity, stats.avgVelocity));
        maxVelocity.setText(getString(R.string.max_velocity, stats.maxVelocity));
        minVelocity.setText(getString(R.string.min_velocity, stats.minVelocity));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        sensorData.add(new SensorData(event.values.clone(), event.timestamp));
    }

    private void startLift() {
        Log.v("Lift", "Start");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
