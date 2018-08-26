package com.example.android.moneyanalytics.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moneyanalytics.R;

import java.util.List;

/**
 * This is a custom Adapter which is responsible for loading the income details in
 * the Income Fragment.
 */

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private final static String LOG_TAG = IncomeAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<Income> mIncomes;

    /**
     * This is the constructor for the IncomeAdapter
     * @param incomes the List of Income objects received from the database.
     * @param context the context of the application.
     */
    public IncomeAdapter(List<Income> incomes, Context context) {
        mIncomes = incomes;
        mContext = context;
    }

    /**
     * Inner class used for caching the child view of the IncomeAdapter.
     */
    class IncomeViewHolder extends RecyclerView.ViewHolder {

        final TextView incomeName;
        final TextView incomeDate;
        final TextView incomeAmount;

        IncomeViewHolder(View itemView) {
            super(itemView);

            incomeName = itemView.findViewById(R.id.income_item_name_tv);
            incomeDate = itemView.findViewById(R.id.income_item_date_tv);
            incomeAmount = itemView.findViewById(R.id.income_item_amount);
        }

    }

    /**
     * This method creates each ViewHolder.
     * @param parent the ViewGroup that contains these ViewHolders.
     * @param viewType the int which define which kind of viewType we want to populate.
     * @return the IncomeViewHolder that holds the View for each list item.
     */
    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.list_item_income;

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new IncomeViewHolder(view);
    }

    /**
     * In this method we update the contents of the ViewHolder for that given position.
     * @param holder the ViewHolder that will have it's contents updated.
     * @param position the position within the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull final IncomeViewHolder holder, int position) {
        holder.incomeName.setText(mIncomes.get(position).getName());
        holder.incomeDate.setText(mIncomes.get(position).getDate().toString());
        holder.incomeAmount.setText(mIncomes.get(position).getAmount().toString());
    }

    /**
     * This item returns the number of items to display.
     * @return the number of items available in our Income list.
     */
    @Override
    public int getItemCount() {
        return mIncomes.size();
    }

}

