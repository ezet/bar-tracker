package ezet.bartracker.activities.view_rep;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import ezet.bartracker.R;
import ezet.bartracker.models.BarEvent;
import ezet.bartracker.models.RepAnalyzer;
import io.github.sporklibrary.Spork;
import io.github.sporklibrary.annotations.BindView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepVelocityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepVelocityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepVelocityFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RepAnalyzer repAnalyzer;

    @BindView(R.id.power_chart)
    private LineChart powerChart;
    private int eventRate = 20;

    public RepVelocityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RepPowerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepVelocityFragment newInstance() {
        RepVelocityFragment fragment = new RepVelocityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            repMax = getArguments().getString(ARG_REP_MAX);
//            repNumber = getArguments().getString(ARG_REP_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rep_power, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spork.bind(this);
        updateChart();
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
        if (context instanceof RepAnalyzerHost) {
            repAnalyzer = ((RepAnalyzerHost) getActivity()).getRepAnalyzer();
        } else throw new RuntimeException(context.toString() + " must implement RepAnalyzerHost");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString() + " must implement BarStatsHost");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateChart() {
        int lim = repAnalyzer.adjustedVelocity.size();
        List<Entry> powerValues = new ArrayList<>();
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < lim; i += eventRate) {
            //power
            BarEvent event = repAnalyzer.adjustedVelocity.get(i);
            powerValues.add(new Entry((float) event.value, i/eventRate));

            /* x axis */
            xValues.add(String.format("%.2f", event.timestamp / 1000000000.0f));
        }
        LineDataSet power = new LineDataSet(powerValues, "Velocity");
        powerChart.setData(new LineData(xValues, power));
        powerChart.notifyDataSetChanged();
        powerChart.invalidate();
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

    public interface RepAnalyzerHost {
        // TODO: Update argument type and name
        RepAnalyzer getRepAnalyzer();
    }
}
