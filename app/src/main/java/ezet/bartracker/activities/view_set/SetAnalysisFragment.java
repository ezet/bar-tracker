package ezet.bartracker.activities.view_set;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import ezet.bartracker.R;
import ezet.bartracker.activities.SetAnalyzerHost;
import ezet.bartracker.activities.view_rep.ViewRepActivity;
import ezet.bartracker.events.ViewRepEvent;
import ezet.bartracker.models.RepAnalyzer;
import ezet.bartracker.models.SetAnalyzer;
import io.github.sporklibrary.Spork;
import io.github.sporklibrary.annotations.BindView;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetAnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetAnalysisFragment extends Fragment implements OnChartValueSelectedListener {


    @BindView(R.id.acceleration_chart)
    private BarChart accelerationChart;
    @BindView(R.id.velocity_chart)
    private BarChart velocityChart;
    @BindView(R.id.force_chart)
    private BarChart forceChart;
    @BindView(R.id.power_chart)
    private BarChart powerChart;
    private SetAnalyzer analyzer;


    public SetAnalysisFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SetAnalysisFragment.
     */

    public static SetAnalysisFragment newInstance() {
        SetAnalysisFragment fragment = new SetAnalysisFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_analysis, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spork.bind(this);
        configureChart(velocityChart);
        configureChart(powerChart);
        configureChart(forceChart);
        configureChart(accelerationChart);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.analyzer = ((SetAnalyzerHost) getActivity()).getSetAnalyzer();
        setContent();
    }

    @Override
    public void onResume() {
        super.onResume();
        velocityChart.highlightValues(null);
        accelerationChart.highlightValues(null);
        powerChart.highlightValues(null);
        forceChart.highlightValues(null);
    }

     private void setContent() {
        int size = analyzer.reps.size();
        List<BarEntry> maxAccelerationValues = new ArrayList<>(size);
        List<BarEntry> maxVelocityValues = new ArrayList<>(size);
        List<BarEntry> avgVelocityValues = new ArrayList<>(size);
        List<BarEntry> avgAccelerationValues = new ArrayList<>(size);
//        List<BarEntry> adjustedVelocityValues = new ArrayList<>();
        List<BarEntry> maxForceValues = new ArrayList<>(size);
        List<BarEntry> avgForceValues = new ArrayList<>(size);
        List<BarEntry> avgPowerValues = new ArrayList<>(size);
        List<BarEntry> maxPowerValues = new ArrayList<>(size);
        List<String> xValues = new ArrayList<>(size);
        for (RepAnalyzer rep : analyzer.reps) {
            xValues.add(String.valueOf(rep.id));

            maxAccelerationValues.add(new BarEntry((float) rep.maxRawAcceleration, rep.id - 1, rep));
            avgAccelerationValues.add(new BarEntry((float) rep.avgRawAcceleration, rep.id - 1, rep));

            maxVelocityValues.add(new BarEntry((float) rep.maxVelocity, rep.id - 1, rep));
            avgVelocityValues.add(new BarEntry((float) rep.avgVelocity, rep.id - 1, rep));

            maxForceValues.add(new BarEntry((float) rep.maxForce, rep.id - 1, rep));
            avgForceValues.add(new BarEntry((float) rep.avgForce, rep.id - 1, rep));

            maxPowerValues.add(new BarEntry((float) rep.maxPower, rep.id - 1, rep));
            avgPowerValues.add(new BarEntry((float) rep.avgPower, rep.id - 1, rep));
        }

//        LineDataSet acceleration = new BarDataSet(rawAccelerationValues, "rawAcceleration (m/s^2)");


//        LineDataSet adjustedVelocity = new LineDataSet(adjustedVelocityValues, "adjustedVelocity (m/s)");
//        adjustedVelocity.setAxisDependency(YAxis.AxisDependency.RIGHT);

        BarDataSet maxVelocity = new BarDataSet(maxVelocityValues, "Max Velocity");
        BarDataSet avgVelocity = new BarDataSet(avgVelocityValues, "Avg. Velocity");

        BarDataSet maxForce = new BarDataSet(maxForceValues, "Max Force");
        BarDataSet avgForce = new BarDataSet(avgForceValues, "Avg. Force");
        BarDataSet maxPower = new BarDataSet(maxPowerValues, "Max Power");
        BarDataSet avgPower = new BarDataSet(avgPowerValues, "Avg. Power");

        BarDataSet maxAcceleration = new BarDataSet(maxAccelerationValues, "Max acceleration");
        BarDataSet avgAcceleration = new BarDataSet(avgAccelerationValues, "Avg. acceleration");

        configureDataSet(maxVelocity);
        configureDataSet(avgVelocity);
        configureDataSet(maxForce);
        configureDataSet(avgForce);
        configureDataSet(maxPower);
        configureDataSet(avgPower);
        configureDataSet(maxAcceleration);
        configureDataSet(avgAcceleration);

        maxVelocity.setColor(R.color.colorPrimaryDark);
        maxAcceleration.setColor(R.color.colorPrimaryDark);
        maxForce.setColor(R.color.colorPrimaryDark);
        maxPower.setColor(R.color.colorPrimaryDark);

        accelerationChart.setData(new BarData(xValues, Arrays.asList((IBarDataSet) maxAcceleration, avgAcceleration)));
        accelerationChart.notifyDataSetChanged();
        accelerationChart.invalidate();

        velocityChart.setData(new BarData(xValues, Arrays.asList((IBarDataSet) maxVelocity, avgVelocity)));
        velocityChart.notifyDataSetChanged();
        velocityChart.invalidate();

        forceChart.setData(new BarData(xValues, Arrays.asList((IBarDataSet) maxForce, avgForce)));
        forceChart.notifyDataSetChanged();
        forceChart.invalidate();

        powerChart.setData(new BarData(xValues, Arrays.asList((IBarDataSet) maxPower, avgPower)));
        powerChart.notifyDataSetChanged();
        powerChart.invalidate();
    }

    private void configureChart(BarChart chart) {
        chart.disableScroll();
//        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);
        chart.setDragEnabled(false);
        chart.setOnChartValueSelectedListener(this);
        chart.setDescription(null);
    }

    private void configureDataSet(BarDataSet dataSet) {
//        dataSet.setHighlightEnabled(false);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        RepAnalyzer rep = (RepAnalyzer) e.getData();
        Intent intent = new Intent(getContext(), ViewRepActivity.class);
        EventBus.getDefault().postSticky(new ViewRepEvent(rep));
        startActivity(intent);
    }

    @Override
    public void onNothingSelected() {

    }
}
