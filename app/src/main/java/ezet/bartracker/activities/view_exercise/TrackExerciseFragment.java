package ezet.bartracker.activities.view_exercise;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.NumberPicker;
import android.widget.ToggleButton;
import ezet.bartracker.R;
import ezet.bartracker.activities.view_set.ViewSetActivity;
import ezet.bartracker.activities.adapters.AccelerometerListener;
import ezet.bartracker.activities.fragments.dummy.ExerciseSetProvider;
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

    private OnFragmentInteractionListener mListener;

    private NumberPicker numberPicker;
    private ToggleButton toggleButton;

    private Date date;

    private AccelerometerListener accelerometerListener;

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

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (((ToggleButton)v).isChecked()) {
                date = Calendar.getInstance().getTime();
                accelerometerListener.register();
            } else {
                accelerometerListener.unregister();
                trackStop();
            }
        }
    };

    private void trackStop() {
        ExerciseSet set = new ExerciseSet(accelerometerListener.sensorData);
        set.id = 1;
        set.name = "Test Set";
        set.date = date;
        set.duration = 0;
        set.weight = numberPicker.getValue();
//        ExerciseSetProvider.ITEMS.remove(set.id);
        ExerciseSetProvider.ITEMS.set(set.id, set);
        ExerciseSetProvider.ITEM_MAP.put(set.id, set);
        Intent intent = new Intent(getActivity(), ViewSetActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt(ViewSetActivity.ARG_SET_ID, set.id);
//        intent.putExtras(bundle);
        EventBus.getDefault().postSticky(set);
        startActivity(intent);
    }

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
            throw new RuntimeException(context.toString()
                    + " must implement BarStatsHost");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
