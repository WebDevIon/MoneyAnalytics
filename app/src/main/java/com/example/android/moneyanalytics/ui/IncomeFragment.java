package com.example.android.moneyanalytics.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.android.moneyanalytics.R;
import com.example.android.moneyanalytics.chart.PieChartData;
import com.example.android.moneyanalytics.model.Entry;
import com.example.android.moneyanalytics.model.IncomeAdapter;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IncomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public IncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_income, container, false);

        Spinner spinner = rootView.findViewById(R.id.income_fragment_period_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.spinner_period_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        //TODO: Remove dummy data for testing purposes only
        AnimatedPieView mAnimatedPieView = rootView.findViewById(R.id.income_fragment_pie_view);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)
                .addData(new SimplePieInfo(10, getResources().getColor(R.color.colorPrimary), "Other"))
                .addData(new SimplePieInfo(60, getResources().getColor(R.color.colorPrimaryDark), "Food"))
                .addData(new PieChartData(30, getResources().getColor(R.color.colorAccent), "Car"))
                .strokeWidth(200)
                .canTouch(false)
                .drawText(true)
                .textSize(80)
                .textMargin(8)
                .guidePointRadius(8)
                .guideLineWidth(6)
                .textGravity(AnimatedPieViewConfig.ECTOPIC)
                .duration(700);

        mAnimatedPieView.start(config);

        RecyclerView recyclerView = rootView.findViewById(R.id.income_fragment_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // TODO: Remove dummy data
        List<Entry> incomes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            incomes.add(new Entry("Salary", Calendar.getInstance().getTime(), 2000d));
        }

        IncomeAdapter incomeAdapter = new IncomeAdapter(incomes, getContext());
        recyclerView.setAdapter(incomeAdapter);

        return rootView;
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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