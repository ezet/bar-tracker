package ezet.bartracker.activities.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ezet.bartracker.R;
import ezet.bartracker.activities.view_exercise.ExerciseHistoryFragment.OnListFragmentInteractionListener;
import ezet.bartracker.contracts.BarTrackerContract;
import ezet.bartracker.models.ExerciseSet;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ExerciseSet} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class SetCursorAdapter extends CursorRecyclerViewAdapter<SetCursorAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    public ExerciseSet contextItem;
    private View.OnCreateContextMenuListener contextMenuListener;


    public SetCursorAdapter(Context context, Cursor cursor, OnListFragmentInteractionListener listener) {
        super(context, cursor, BarTrackerContract.SetDef.COL_ID);
        mListener = listener;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_set, parent, false);
        return new ViewHolder(view);
    }

    public void setContextMenuListener(View.OnCreateContextMenuListener contextMenuListener) {
        this.contextMenuListener = contextMenuListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        holder.mItem = ExerciseSet.fromCursor(cursor);
        holder.dateView.setText(mContext.getString(R.string.listfield_date_time, DateFormat.getLongDateFormat(mContext).format(holder.mItem.date), DateFormat.getTimeFormat(mContext).format(holder.mItem.date)));
        holder.repsView.setText(mContext.getString(R.string.listfield_reps, holder.mItem.repetitions));
        holder.weightView.setText(mContext.getString(R.string.listfield_weight, holder.mItem.weight));

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

        holder.mView.setOnCreateContextMenuListener(contextMenuListener);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final View mView;
        public final TextView dateView;
        public final TextView repsView;
        public final TextView weightView;

        public ExerciseSet mItem;

        // TODO: Add reps, weight
        public ViewHolder(View view) {
            super(view);
            mView = view;
            dateView = (TextView) view.findViewById(R.id.date);
            repsView = (TextView) view.findViewById(R.id.reps);
            weightView = (TextView) view.findViewById(R.id.weight);
            mView.setOnLongClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + dateView.getText() + "'";
        }

        @Override
        public boolean onLongClick(View v) {
            SetCursorAdapter.this.contextItem = mItem;
            return false;
        }
    }
}
