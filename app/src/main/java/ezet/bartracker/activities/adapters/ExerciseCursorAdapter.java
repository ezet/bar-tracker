package ezet.bartracker.activities.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ezet.bartracker.R;
import ezet.bartracker.activities.main.ExercisesFragment.OnListFragmentInteractionListener;
import ezet.bartracker.contracts.BarTrackerContract;
import ezet.bartracker.models.Exercise;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Exercise} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ExerciseCursorAdapter extends CursorRecyclerViewAdapter<ExerciseCursorAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;

    public Exercise contextItem;
    private View.OnCreateContextMenuListener contextMenuListener;

    public ExerciseCursorAdapter(Context context, Cursor cursor, OnListFragmentInteractionListener listener) {
        super(context, cursor, BarTrackerContract.ExerciseDef.COL_ID);
        mListener = listener;
        setHasStableIds(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exercises, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        holder.item = Exercise.fromCursor(cursor);
        holder.nameView.setText(holder.item.name);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.item);
                }
            }
        });
        holder.mView.setOnCreateContextMenuListener(contextMenuListener);


    }

    public void setContextMenuListener(View.OnCreateContextMenuListener contextMenuListener) {
        this.contextMenuListener = contextMenuListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public final View mView;
        public final TextView nameView;
        public Exercise item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameView = (TextView) view.findViewById(R.id.name);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            ExerciseCursorAdapter.this.contextItem = item;
            return false;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
