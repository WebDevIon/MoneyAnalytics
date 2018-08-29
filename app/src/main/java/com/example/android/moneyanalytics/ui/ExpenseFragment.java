package com.example.android.moneyanalytics.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.moneyanalytics.R;
import com.example.android.moneyanalytics.chart.DefaultPieConfig;
import com.example.android.moneyanalytics.chart.PieChartData;
import com.example.android.moneyanalytics.model.Entry;
import com.example.android.moneyanalytics.model.EntryByCategory;
import com.example.android.moneyanalytics.model.ExpenseAdapter;
import com.example.android.moneyanalytics.model.ExpenseViewModel;
import com.example.android.moneyanalytics.room.EntriesDatabase;
import com.example.android.moneyanalytics.utils.ColorUtils;
import com.example.android.moneyanalytics.utils.DateUtils;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


/**
 * The fragment responsible for displaying the expenses status.
 */
public class ExpenseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String TAG = ExpenseFragment.class.getSimpleName();
    private static final String NO_CATEGORY = "no category";
    private static final String ARG_START_DATE = "start date";
    private static final String ARG_END_DATE = "end date";
    private static final String ARG_SPINNER_ID = "spinner id";
    private static final String ARG_CATEGORY = "category";
    public static final String PREFS_NAME = "Expense Fragment";
    private TextView mTotalTextView;
    private RecyclerView mRecyclerView;
    private Spinner mSpinner;
    private String mPieCategory;
    private AnimatedPieView mAnimatedPieView;
    private EntriesDatabase mDb;
    private Long mStartDate;
    private Long mEndDate;
    private ExpenseAdapter mAdapter;
    private int mPosition;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param startDate start date for the query.
     * @param endDate end date fpr the query.
     * @return A new instance of fragment ExpenseFragment.
     */
    public static ExpenseFragment newInstance(Long startDate, Long endDate) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_START_DATE, startDate);
        args.putLong(ARG_END_DATE, endDate);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Here we set the date from the main activity for the query.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mStartDate = getArguments().getLong(ARG_START_DATE);
            mEndDate = getArguments().getLong(ARG_END_DATE);
        }
    }

    // Here we save the data that we want to keep during screen rotation,
    // changing activities or leaving the app and returning to it at another time.
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor prefs =
                getContext().getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(ARG_SPINNER_ID, mSpinner.getSelectedItemPosition());
        prefs.putLong(ARG_START_DATE, mStartDate);
        prefs.putLong(ARG_END_DATE, mEndDate);
        prefs.putString(ARG_CATEGORY, mPieCategory);
        Log.d(TAG, "Stored category: " + mPieCategory);
        prefs.apply();
    }

    // Here we restore the data that we saved in onPause.
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, 0);
        mSpinner.setSelection(prefs.getInt(ARG_SPINNER_ID, 0));
        mStartDate = prefs.getLong(ARG_START_DATE, new Date().getTime());
        mEndDate = prefs.getLong(ARG_END_DATE, new Date().getTime());
        mPieCategory = prefs.getString(ARG_CATEGORY, NO_CATEGORY);
        Log.d(TAG, "Retrieved category: " + mPieCategory);
        setupViewModel(mStartDate, mEndDate, mPieCategory);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_expense, container, false);

        mDb = EntriesDatabase.getInstance(getContext());
        mAnimatedPieView = rootView.findViewById(R.id.expense_fragment_pie_view);
        mTotalTextView = rootView.findViewById(R.id.expense_fragment_total_value_tv);

        mSpinner = rootView.findViewById(R.id.expense_fragment_period_spinner);
        mSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.spinner_period_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinner.setAdapter(adapter);

        mRecyclerView = rootView.findViewById(R.id.expense_fragment_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        // Set up a touch helper for the swipe to delete function.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                mPosition = viewHolder.getAdapterPosition();
                // Call the AsyncTask which deletes the entry.
                new AsyncCaller().execute();
            }
        }).attachToRecyclerView(mRecyclerView);

        setupViewModel(mStartDate, mEndDate, NO_CATEGORY);

        return rootView;
    }

    /**
     * Method used to set up the View Model.
     * @param startDate the start date used in the queries.
     * @param endDate the end date used in the queries.
     * @param category the category which we want to get the data from.
     */
    private void setupViewModel(Long startDate, final Long endDate, String category) {
        ExpenseViewModel viewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        if (category.equals(NO_CATEGORY)) {
            viewModel.getEntriesGroupedByCategory(startDate, endDate)
                    .observe(this, new Observer<List<EntryByCategory>>() {
                        @Override
                        public void onChanged(@Nullable List<EntryByCategory> entries) {
                            Log.d(TAG, "Updating list of tasks from LiveData in ExpenseViewModel");
                            if (entries != null) {
                                processEntries(entries);
                            } else {
                                Log.d(TAG, "Query returned no results.");
                            }
                        }
                    });
        } else {
            viewModel.getEntriesForCategory(category, startDate, endDate)
                    .observe(this, new Observer<List<Entry>>() {
                        @Override
                        public void onChanged(@Nullable List<Entry> entries) {
                            displayCategoryItems(entries);
                        }
                    });
        }
    }

    /**
     * This method is used to populate the Pie Chart and calculate the total.
     * @param entries the list of entries passed by the ViewModel.
     */
    private void processEntries(List<EntryByCategory> entries) {
        Log.d(TAG, "Expense Fragment EntryByCategory length: " + entries.size());

        ColorUtils colorUtils = new ColorUtils(getContext());
        Double totalExpense = 0d;
        AnimatedPieViewConfig mPieConfig = new DefaultPieConfig().getDefaultPieConfig(true);
        int counter = 0;
        String lastCategory = NO_CATEGORY;

        mPieConfig.selectListener(new OnPieSelectListener<IPieInfo>() {
            @Override
            public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {
                mPieCategory = pieInfo.getDesc();
                Log.d(TAG, "Selected category is: " + mPieCategory);
                setupViewModel(mStartDate, mEndDate, mPieCategory);
            }
        });

        for (EntryByCategory entry : entries) {
            Log.d(TAG, "Entry category: " + entry.getCategory());
            Log.d(TAG, "Entry amount: " + entry.getAmount());
            Log.d(TAG, "Entry type: " + entry.getType());
            if (entry.getType().equals(AddExpenseActivity.DATA_EXPENSE_TYPE_KEY)) {
                totalExpense += entry.getAmount();
                counter++;
                lastCategory = entry.getCategory();
                mPieConfig.addData(new PieChartData (entry.getAmount(),
                        colorUtils.getColor(entry.getCategory()), entry.getCategory()));

            }
        }

        mAnimatedPieView.start(mPieConfig);

        String totalExpenseStr = totalExpense.toString();
        mTotalTextView.setText(totalExpenseStr);

        Log.d(TAG, "Size of entries: " + entries.size());

        // If we have only one category then we display all of it's items.
        if (counter == 1) {
            setupViewModel(mStartDate, mEndDate, lastCategory);
        } else {
            List<Entry> entriesForAdapter = new ArrayList<>();
            entriesForAdapter.clear();
            ExpenseAdapter adapter = new ExpenseAdapter(entriesForAdapter, getContext());
            mRecyclerView.setAdapter(adapter);
        }

        Log.d(TAG, "Expense fragment totalExpense: " + totalExpense);
    }

    /**
     * Method used to display the contents of a selected category.
     * @param entries the list containing all the entries from that category.
     */
    public void displayCategoryItems (List<Entry> entries) {
        mAdapter = new ExpenseAdapter(entries, getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * If a spinner item is selected then we change the pie chart to display the
     * entries for for the selected date.
     * @param adapterView the object that was selected.
     * @param i the index of the item that was selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedItem = adapterView.getItemAtPosition(i).toString();
        if (selectedItem.equals(getResources().getString(R.string.nav_drawer_today_string))) {
            mEndDate = new Date().getTime();
            DateUtils dateUtils = new DateUtils(mEndDate);
            mStartDate = dateUtils.getMidnightDate();
            setupViewModel(mStartDate, mEndDate, NO_CATEGORY);
        } else if (selectedItem.equals(getResources().getString(R.string.nav_drawer_week_string))) {
            mEndDate = new Date().getTime();
            DateUtils dateUtils = new DateUtils(mEndDate);
            mStartDate = dateUtils.getAWeekAgoDate();
            setupViewModel(mStartDate, mEndDate, NO_CATEGORY);
        } else if (selectedItem.equals(getResources().getString(R.string.nav_drawer_month_string))) {
            mEndDate = new Date().getTime();
            DateUtils dateUtils = new DateUtils(mEndDate);
            mStartDate = dateUtils.getFirstDayOfMonthDate();
            setupViewModel(mStartDate, mEndDate, NO_CATEGORY);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    // AsyncTask used to delete the entry from the database.
    @SuppressLint("StaticFieldLeak")
    private class AsyncCaller extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            List<Entry> entries = mAdapter.getEntries();
            mDb.entriesDao().deleteTask(entries.get(mPosition));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setupViewModel(mStartDate, mEndDate, NO_CATEGORY);
        }

    }
}
