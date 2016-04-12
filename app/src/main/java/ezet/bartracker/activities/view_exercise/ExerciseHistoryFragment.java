package ezet.bartracker.activities.view_exercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import ezet.bartracker.R;
import ezet.bartracker.activities.adapters.SetCursorAdapter;
import ezet.bartracker.activities.view_set.ViewSetActivity;
import ezet.bartracker.contracts.BarTrackerDb;
import ezet.bartracker.events.NewSetEvent;
import ezet.bartracker.events.ViewExerciseEvent;
import ezet.bartracker.events.ViewSetEvent;
import ezet.bartracker.models.Exercise;
import ezet.bartracker.models.ExerciseSet;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExerciseHistoryFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private BarTrackerDb db;
    private Exercise exercise;
    private RecyclerView recyclerView;
    private SetCursorAdapter adapter;
    private OnListFragmentInteractionListener mListener = new OnListFragmentInteractionListener() {
        @Override
        public void onListFragmentInteraction(ExerciseSet item) {
            Intent intent = new Intent(getActivity(), ViewSetActivity.class);
            startActivity(intent);
            EventBus.getDefault().postSticky(new ViewSetEvent(item));
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExerciseHistoryFragment() {
    }

    public static ExerciseHistoryFragment newInstance(int columnCount) {
        ExerciseHistoryFragment fragment = new ExerciseHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        EventBus.getDefault().register(this);
        this.exercise = EventBus.getDefault().getStickyEvent(ViewExerciseEvent.class).exercise;
    }

    @Subscribe
    public void newSetEventHandler(NewSetEvent event) {
        adapter.changeCursor(db.getSetsByExerciseId(exercise.id));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new SetCursorAdapter(getContext(), db.getSetsByExerciseId(exercise.id), mListener);
            adapter.setContextMenuListener(this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExerciseSet set = adapter.contextItem;
        switch (item.getItemId()) {
            case R.id.action_delete: onDelete(set); break;
        }
        return super.onContextItemSelected(item);
    }

    private void onDelete(ExerciseSet set) {
        if (db.deleteSet(set)) {
            adapter.changeCursor(db.getSetsByExerciseId(exercise.id));
        } else {
            Snackbar.make(getView(), "There was an error deleting the set.", Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        db = new BarTrackerDb(context);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ExerciseSet item);
    }
}
