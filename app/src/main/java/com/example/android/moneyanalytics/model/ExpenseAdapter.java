package com.example.android.moneyanalytics.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.moneyanalytics.R;
import com.example.android.moneyanalytics.utils.DateUtils;

import java.util.List;

/**
 * This is a custom Adapter which is responsible for loading the Expense details in
 * the Expense Fragment.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private final static String LOG_TAG = ExpenseAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<Entry> mExpenses;

    /**
     * This is the constructor for the ExpenseAdapter
     * @param Expenses the List of Entry objects received from the database.
     * @param context the context of the application.
     */
    public ExpenseAdapter(List<Entry> Expenses, Context context) {
        mExpenses = Expenses;
        mContext = context;
    }

    /**
     * Inner class used for caching the child view of the ExpenseAdapter.
     */
    class ExpenseViewHolder extends RecyclerView.ViewHolder {

        final TextView ExpenseName;
        final TextView ExpenseDate;
        final TextView ExpenseAmount;

        ExpenseViewHolder(View itemView) {
            super(itemView);

            ExpenseName = itemView.findViewById(R.id.expense_item_name_tv);
            ExpenseDate = itemView.findViewById(R.id.expense_item_date_tv);
            ExpenseAmount = itemView.findViewById(R.id.expense_item_amount);
        }

    }

    /**
     * This method creates each ViewHolder.
     * @param parent the ViewGroup that contains these ViewHolders.
     * @param viewType the int which define which kind of viewType we want to populate.
     * @return the ExpenseViewHolder that holds the View for each list item.
     */
    @NonNull
    @Override
    public ExpenseAdapter.ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.list_item_expense;

        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return new ExpenseAdapter.ExpenseViewHolder(view);
    }

    /**
     * In this method we update the contents of the ViewHolder for that given position.
     * @param holder the ViewHolder that will have it's contents updated.
     * @param position the position within the adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull final ExpenseAdapter.ExpenseViewHolder holder, int position) {
        holder.ExpenseName.setText(mExpenses.get(position).getName());
        holder.ExpenseDate.setText(new DateUtils(mExpenses.get(position).getDate()).getDateToString());
        holder.ExpenseAmount.setText(mExpenses.get(position).getAmount().toString());
    }

    /**
     * This item returns the number of items to display.
     * @return the number of items available in our Expense list.
     */
    @Override
    public int getItemCount() {
        return mExpenses.size();
    }

    public List<Entry> getEntries(){
        return mExpenses;
    }

}
