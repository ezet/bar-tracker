package ezet.bartracker.activities.view_set;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import ezet.bartracker.R;
import ezet.bartracker.activities.SetAnalyzerHost;
import ezet.bartracker.models.SetAnalyzer;
import io.github.sporklibrary.Spork;
import io.github.sporklibrary.annotations.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetStatsFragment extends Fragment {

    private SetAnalyzer setAnalyzer;

    @BindView(R.id.max_velocity)
    private TextView max_velocity;

    @BindView(R.id.avg_velocity)
    private TextView avg_velocity;

    @BindView(R.id.max_power)
    private TextView max_power;

    @BindView(R.id.avg_power)
    private TextView avg_power;

    @BindView(R.id.max_force)
    private TextView max_force;

    @BindView(R.id.avg_force)
    private TextView avg_force;


    public SetStatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SetStatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SetStatsFragment newInstance() {
        SetStatsFragment fragment = new SetStatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_stats, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof SetAnalyzerHost)) {
            throw new RuntimeException(context.toString()
                    + " must implement SetAnalyzerHost");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Spork.bind(this);
        setAnalyzer = ((SetAnalyzerHost) getActivity()).getSetAnalyzer();
        setContent();
    }

    private void setContent() {
        max_power.setText(getString(R.string.field_max_power, setAnalyzer.maxPower));
        avg_power.setText(getString(R.string.field_avg_power, setAnalyzer.avgPower));
        max_velocity.setText(getString(R.string.field_max_velocity, setAnalyzer.maxVelocity));
        avg_velocity.setText(getString(R.string.field_avg_velocity, setAnalyzer.avgVelocity));
        max_force.setText(getString(R.string.field_max_force, setAnalyzer.maxForce));
        avg_force.setText(getString(R.string.field_avg_force, setAnalyzer.avgForce));
    }
}
