package ezet.bartracker.activities.view_exercise;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.ToggleButton;
import ezet.bartracker.R;
import ezet.bartracker.activities.adapters.AccelerometerListener;
import ezet.bartracker.activities.fragments.dummy.ExerciseSetProvider;
import ezet.bartracker.activities.view_set.ViewSetActivity;
import ezet.bartracker.contracts.BarTrackerDb;
import ezet.bartracker.events.NewSetEvent;
import ezet.bartracker.events.ViewExerciseEvent;
import ezet.bartracker.events.ViewSetEvent;
import ezet.bartracker.models.Exercise;
import ezet.bartracker.models.ExerciseSet;
import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackExerciseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackExerciseFragment extends Fragment {

    private NumberPicker numberPicker;
    private ToggleButton toggleButton;

    private Date date;
    private BarTrackerDb db;
    private Exercise exercise;

    private AccelerometerListener accelerometerListener;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (((ToggleButton) v).isChecked()) {
                date = Calendar.getInstance().getTime();
                accelerometerListener.register();
            } else {
                accelerometerListener.unregister();
                trackStop();
            }
        }
    };

    public TrackExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TrackExerciseFragment.
     */
    public static TrackExerciseFragment newInstance() {
        TrackExerciseFragment fragment = new TrackExerciseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accelerometerListener = new AccelerometerListener((SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE));
        exercise = EventBus.getDefault().getStickyEvent(ViewExerciseEvent.class).exercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track_exercise, container, false);
        numberPicker = (NumberPicker) view.findViewById(R.id.weight_picker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(1000);
        numberPicker.setValue(100);
        toggleButton = (ToggleButton) view.findViewById(R.id.track_toggle_button);
        toggleButton.setChecked(false);
        toggleButton.setOnClickListener(clickListener);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getActivity().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

     private void trackStop() {
        ExerciseSet set = new ExerciseSet(accelerometerListener.sensorData);
        set.id = 1;
        set.exerciseId = exercise.id;
        set.date = date;
        set.repetitions = 5;
        set.duration = 0;
        set.weight = numberPicker.getValue();
        ExerciseSetProvider.ITEMS.set((int) set.id, set);
        ExerciseSetProvider.ITEM_MAP.put(set.id, set);
        db.insertSet(set);
        EventBus.getDefault().post(new NewSetEvent());
        Intent intent = new Intent(getActivity(), ViewSetActivity.class);
        EventBus.getDefault().postSticky(new ViewSetEvent(set));
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        db = new BarTrackerDb(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement BarStatsHost");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
