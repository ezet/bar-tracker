package ezet.bartracker.activities.view_set;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import ezet.bartracker.R;
import ezet.bartracker.activities.SetAnalyzerHost;
import ezet.bartracker.models.RepAnalyzer;
import ezet.bartracker.models.SetAnalyzer;
import io.github.sporklibrary.Spork;
import io.github.sporklibrary.annotations.BindView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetAnalysisFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetAnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetAnalysisFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        this.analyzer = ((SetAnalyzerHost)getActivity()).getSetAnalyzer();
        setContent();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setContent() {

//        List<Entry> rawAccelerationValues = new ArrayList<>();
        List<BarEntry> maxVelocity = new ArrayList<>();
//        List<BarEntry> adjustedVelocityValues = new ArrayList<>();
        List<BarEntry> maxForceValues = new ArrayList<>();
        List<BarEntry> avgForceValues = new ArrayList<>();
        List<BarEntry> avgPowerValues = new ArrayList<>();
        List<BarEntry> maxPowerValues = new ArrayList<>();
        List<String> xValues = new ArrayList<>();
        for (RepAnalyzer rep : analyzer.reps) {
            xValues.add(String.valueOf(rep.id));
            maxVelocity.add(new BarEntry((float) rep.maxVelocity, rep.id -1));
            maxForceValues.add(new BarEntry((float) rep.maxForce, rep.id-1));
            avgForceValues.add(new BarEntry((float) rep.avgForce, rep.id-1));

            maxPowerValues.add(new BarEntry((float) rep.maxPower, rep.id-1));
            avgPowerValues.add(new BarEntry((float) rep.avgPower, rep.id-1));
        }

//        LineDataSet acceleration = new BarDataSet(rawAccelerationValues, "rawAcceleration (m/s^2)");
        BarDataSet velocity = new BarDataSet(maxVelocity, "Raw Velocity");
//        LineDataSet adjustedVelocity = new LineDataSet(adjustedVelocityValues, "adjustedVelocity (m/s)");
//        adjustedVelocity.setAxisDependency(YAxis.AxisDependency.RIGHT);

        BarDataSet maxForce = new BarDataSet(maxForceValues, "Max Force");
        BarDataSet avgForce = new BarDataSet(avgForceValues, "Avg. Force");
        BarDataSet maxPower = new BarDataSet(maxPowerValues, "Max Power");
        BarDataSet avgPower = new BarDataSet(avgPowerValues, "Avg. Power");

//        List<ILineDataSet> velocitySet = new LinkedList<>();
//        velocitySet.add(velocity);
//        velocitySet.add(adjustedVelocity);

//        accelerationChart.setData(new BarData(xValues, acceleration));
//        accelerationChart.notifyDataSetChanged();
//        accelerationChart.invalidate();

        velocityChart.setData(new BarData(xValues, velocity));
        velocityChart.notifyDataSetChanged();
        velocityChart.invalidate();

        forceChart.setData(new BarData(xValues, Arrays.asList((IBarDataSet)maxForce, avgForce)));
        forceChart.notifyDataSetChanged();
        forceChart.invalidate();

        powerChart.setData(new BarData(xValues, Arrays.asList((IBarDataSet)maxPower, avgPower)));
        powerChart.notifyDataSetChanged();
        powerChart.invalidate();
    }

    private void configureChart(BarChart chart) {
        chart.disableScroll();
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
