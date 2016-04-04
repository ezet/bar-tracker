package ezet.bartracker.activities.adapters;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import ezet.bartracker.models.BarEvent;
import ezet.bartracker.models.SetAnalyzer;
import ezet.bartracker.models.SensorData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by larsk on 30-Mar-16.
 */
public class MpAndroidChartAdapter {

    private int eventRate;

    private void analyzeData(List<SensorData> data) {

        SetAnalyzer stats = new SetAnalyzer(data);
        stats.analyze();

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
            forceValues.add(new Entry((float) event.value, i / eventRate));

            //power
            event = stats.power.get(i);
            powerValues.add(new Entry((float) event.value, i / eventRate));


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

//        accelerationChart.setData(new LineData(xValues, acceleration));
//        accelerationChart.notifyDataSetChanged();
//        accelerationChart.invalidate();
//
//        velocityChart.setData(new LineData(xValues, velocitySet));
//        velocityChart.notifyDataSetChanged();
//        velocityChart.invalidate();
//
//        forceChart.setData(new LineData(xValues, force));
//        forceChart.notifyDataSetChanged();
//        forceChart.invalidate();
//
//        powerChart.setData(new LineData(xValues, power));
//        powerChart.notifyDataSetChanged();
//        powerChart.invalidate();
//
//        avgVelocity.setText(getString(R.string.average_velocity, stats.avgVelocity));
//        maxVelocity.setText(getString(R.string.max_velocity, stats.maxVelocity));
//        minVelocity.setText(getString(R.string.min_velocity, stats.minVelocity));
    }
}
