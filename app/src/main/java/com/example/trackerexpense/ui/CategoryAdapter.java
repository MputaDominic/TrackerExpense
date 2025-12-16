package com.example.trackerexpense.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackerexpense.R;
import com.example.trackerexpense.data.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> mCategories = new ArrayList<>();
    private final OnCategoryDeleteListener mDeleteListener;

    public interface OnCategoryDeleteListener {
        void onDelete(Category category);
    }

    public CategoryAdapter(OnCategoryDeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category current = mCategories.get(position);
        holder.nameView.setText(current.name);

        try {
            int color = Color.parseColor(current.color);
            GradientDrawable bg = (GradientDrawable) holder.iconView.getBackground();
            bg.setColor(color);
        } catch (Exception e) {
            // Fallback
        }

        holder.deleteButton.setOnClickListener(v -> {
            if (mDeleteListener != null) {
                mDeleteListener.onDelete(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final ImageView iconView;
        private final ImageButton deleteButton;

        private CategoryViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.category_name);
            iconView = itemView.findViewById(R.id.category_icon);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}
