package com.example.trackerexpense.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackerexpense.R;
import com.example.trackerexpense.data.CategoryTotal;

import java.util.ArrayList;
import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {

    private List<CategoryTotal> mCategoryTotals = new ArrayList<>();
    private double mTotalAmount = 0;

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        CategoryTotal current = mCategoryTotals.get(position);
        holder.nameView.setText(current.categoryName);
        holder.amountView.setText(String.format("$%.2f", current.totalAmount));

        double percentage = (mTotalAmount > 0) ? (current.totalAmount / mTotalAmount) * 100 : 0;
        holder.percentageView.setText(String.format("%.1f%%", percentage));

        try {
            int color = Color.parseColor(current.color);
            GradientDrawable bg = (GradientDrawable) holder.iconView.getBackground();
            bg.setColor(color);
        } catch (Exception e) {
            // Fallback
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryTotals.size();
    }

    public void setCategoryTotals(List<CategoryTotal> categoryTotals) {
        mCategoryTotals = categoryTotals;
        mTotalAmount = 0;
        for (CategoryTotal ct : categoryTotals) {
            mTotalAmount += ct.totalAmount;
        }
        notifyDataSetChanged();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final TextView percentageView;
        private final TextView amountView;
        private final ImageView iconView;

        private ReportViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.report_category_name);
            percentageView = itemView.findViewById(R.id.report_percentage);
            amountView = itemView.findViewById(R.id.report_amount);
            iconView = itemView.findViewById(R.id.report_category_icon);
        }
    }
}
