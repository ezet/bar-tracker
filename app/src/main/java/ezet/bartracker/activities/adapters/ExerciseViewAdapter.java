package ezet.bartracker.activities.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ezet.bartracker.R;
import ezet.bartracker.activities.fragments.ExercisesFragment.OnListFragmentInteractionListener;
import ezet.bartracker.models.Exercise;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Exercise} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ExerciseViewAdapter extends RecyclerView.Adapter<ExerciseViewAdapter.ViewHolder> {

    private final List<Exercise> values;
    private final OnListFragmentInteractionListener mListener;

    public ExerciseViewAdapter(List<Exercise> items, OnListFragmentInteractionListener listener) {
        values = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_exercises, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = values.get(position);
        holder.idView.setText("" + values.get(position).id);
        holder.nameView.setText(values.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView idView;
        public final TextView nameView;
        public Exercise item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            idView = (TextView) view.findViewById(R.id.id);
            nameView = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }
}
