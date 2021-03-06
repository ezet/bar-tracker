package ezet.bartracker.activities.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ezet.bartracker.R;
import ezet.bartracker.activities.view_exercise.ExerciseHistoryFragment.OnListFragmentInteractionListener;
import ezet.bartracker.models.ExerciseSet;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ExerciseSet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class SetViewAdapter extends RecyclerView.Adapter<SetViewAdapter.ViewHolder> {

    private final List<ExerciseSet> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SetViewAdapter(List<ExerciseSet> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nameView.setText("Set " + mValues.get(position).id);
        holder.dateView.setText(SimpleDateFormat.getDateInstance().format(mValues.get(position).date));
        holder.weightView.setText("" + mValues.get(position).weight);
        holder.repsView.setText("" + 5);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameView;
        public final TextView dateView;
        public final TextView repsView;
        public final TextView weightView;

        public ExerciseSet mItem;

        // TODO: Add reps, weight
        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.name);
            dateView = (TextView) view.findViewById(R.id.date);
            repsView = (TextView) view.findViewById(R.id.reps);
            weightView = (TextView) view.findViewById(R.id.weight);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + dateView.getText() + "'";
        }
    }
}
