package ezet.bartracker.activities.view_rep;

import android.content.Context;
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
 * Use the {@link RepPowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepPowerFragment extends Fragment {

    private RepAnalyzer repAnalyzer;

    @BindView(R.id.power_chart)
    private LineChart powerChart;
    private int eventRate = 1;

    public RepPowerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RepPowerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepPowerFragment newInstance() {
        RepPowerFragment fragment = new RepPowerFragment();
        return fragment;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RepAnalyzerHost) {
            repAnalyzer = ((RepAnalyzerHost) getActivity()).getRepAnalyzer();
        } else throw new RuntimeException(context.toString() + " must implement RepAnalyzerHost");
    }

    private void updateChart() {
        int lim = repAnalyzer.power.size();
        List<Entry> powerValues = new ArrayList<>();
        List<String> xValues = new ArrayList<>();
        for (int i = 0; i < lim; i += eventRate) {
            //power
            BarEvent event = repAnalyzer.power.get(i);
            powerValues.add(new Entry((float) event.value, i / eventRate));


            /* x axis */
            xValues.add(String.format("%.2f", event.timestamp / 1000000000.0f));
        }
        LineDataSet power = new LineDataSet(powerValues, "Power (Watt)");
        powerChart.setData(new LineData(xValues, power));
        powerChart.notifyDataSetChanged();
        powerChart.invalidate();
    }

    public interface RepAnalyzerHost {
        RepAnalyzer getRepAnalyzer();
    }
}
