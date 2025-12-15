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
import com.example.trackerexpense.data.Category;
import com.example.trackerexpense.data.Expense;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> mExpenses = new ArrayList<>();
    private Map<Integer, Category> mCategoryMap = new HashMap<>();

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense current = mExpenses.get(position);
        Category category = mCategoryMap.get(current.categoryId);

        holder.amountItemView.setText(String.format("$%.2f", current.amount));
        holder.dateItemView.setText(DateFormat.getDateInstance().format(new Date(current.date)));
        holder.noteItemView.setText(current.note);

        if (category != null) {
            holder.categoryItemView.setText(category.name);
            try {
                int color = Color.parseColor(category.color);
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
        return mExpenses.size();
    }

    public void setExpenses(List<Expense> expenses) {
        mExpenses = expenses;
        notifyDataSetChanged();
    }

    public void setCategories(List<Category> categories) {
        mCategoryMap.clear();
        for(Category c : categories) {
            mCategoryMap.put(c.id, c);
        }
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
