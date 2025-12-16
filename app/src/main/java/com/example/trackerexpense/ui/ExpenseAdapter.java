package com.example.trackerexpense.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackerexpense.R;
import com.example.trackerexpense.data.TransactionWithCategory;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<TransactionWithCategory> mTransactions = new ArrayList<>();

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        TransactionWithCategory current = mTransactions.get(position);

        // Handle Amount Color and formatting
        boolean isIncome = current.category != null && "INCOME".equals(current.category.type);

        if (isIncome) {
             holder.amountItemView.setText(String.format("+%,.2fSh", current.expense.amount));
             holder.amountItemView.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else {
             holder.amountItemView.setText(String.format("-%,.2fSh", current.expense.amount));
             holder.amountItemView.setTextColor(Color.parseColor("#F44336")); // Red
        }

        holder.dateItemView.setText(DateFormat.getDateInstance().format(new Date(current.expense.date)));
        holder.noteItemView.setText(current.expense.note);

        if (current.category != null) {
            holder.categoryItemView.setText(current.category.name);
            try {
                int color = Color.parseColor(current.category.color);
                GradientDrawable bg = (GradientDrawable) holder.iconView.getBackground();
                bg.setColor(color);
            } catch (Exception e) {
                // Fallback color
            }
        } else {
            holder.categoryItemView.setText("Unknown Category");
        }
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }

    public void setTransactions(List<TransactionWithCategory> transactions) {
        mTransactions = transactions;
        notifyDataSetChanged();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView amountItemView;
        private final TextView categoryItemView;
        private final TextView dateItemView;
        private final TextView noteItemView;
        private final ImageView iconView;

        private ExpenseViewHolder(View itemView) {
            super(itemView);
            amountItemView = itemView.findViewById(R.id.text_amount);
            categoryItemView = itemView.findViewById(R.id.text_category);
            dateItemView = itemView.findViewById(R.id.text_date);
            noteItemView = itemView.findViewById(R.id.text_note);
            iconView = itemView.findViewById(R.id.icon_category);
        }
    }
}
