package ezet.bartracker.activities.view_set;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ezet.bartracker.R;
import ezet.bartracker.activities.SetAnalyzerHost;
import ezet.bartracker.models.ExerciseSet;
import ezet.bartracker.models.SetAnalyzer;
import io.github.sporklibrary.Spork;
import io.github.sporklibrary.annotations.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SetAnalyzerHost} interface
 * to handle interaction events.
 * Use the {@link SetSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetSummaryFragment extends Fragment {

    @BindView(R.id.date)
    private TextView date;

//    @BindView(R.id.duration)
//    private TextView duration;

    @BindView(R.id.repetitions)
    private TextView repetitions;

    @BindView(R.id.weight)
    private TextView weight;

    private SetAnalyzer stats;

    private ExerciseSet set;

    public SetSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SetSummaryFragment.
     */
    public static SetSummaryFragment newInstance() {
        SetSummaryFragment fragment = new SetSummaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_set_stats, SetStatsFragment.newInstance());
        ft.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spork.bind(this);
        setContent();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SetAnalyzerHost) {
            stats = ((SetAnalyzerHost) context).getSetAnalyzer();
            set = stats.set;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SetAnalyzerHost");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setContent() {
        weight.setText(getString(R.string.field_weight, set.weight));
//        duration.setText(getString(R.string.field_duration, set.duration));
        repetitions.setText(getString(R.string.field_repetitions, stats.reps.size()));
        date.setText(getString(R.string.listfield_date_time, DateFormat.getLongDateFormat(getContext()).format(set.date), DateFormat.getTimeFormat(getContext()).format(set.date)));
    }

}
