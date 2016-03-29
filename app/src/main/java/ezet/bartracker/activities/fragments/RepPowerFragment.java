package ezet.bartracker.activities.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ezet.bartracker.R;
import ezet.bartracker.models.BarStats;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RepPowerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RepPowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepPowerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_REP_MAX = "rep_max";
    private static final String ARG_REP_NUMBER = "rep_number";

    // TODO: Rename and change types of parameters
    private String repMax;
    private String repNumber;

    private OnFragmentInteractionListener mListener;
    private BarStats repStats;

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
    public static RepPowerFragment newInstance(int repMax, int repNumber) {
        RepPowerFragment fragment = new RepPowerFragment();
        Bundle bundle = fragment.getArguments();
        bundle.putInt(ARG_REP_MAX, repMax);
        bundle.putInt(ARG_REP_NUMBER, repNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setBarStats(BarStats repStats) {
        this.repStats = repStats;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            repMax = getArguments().getString(ARG_REP_MAX);
            repNumber = getArguments().getString(ARG_REP_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rep_power, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
//            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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
