package com.example.trackerexpense.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackerexpense.R;
import com.example.trackerexpense.data.Category;
import com.example.trackerexpense.viewmodel.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CategoryFragment extends Fragment {

    private ExpenseViewModel mExpenseViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        CategoryAdapter adapter = new CategoryAdapter(category -> {
            // Handle delete
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Category")
                    .setMessage("Are you sure you want to delete " + category.name + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        mExpenseViewModel.deleteCategory(category);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        recyclerView.setAdapter(adapter);

        mExpenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        mExpenseViewModel.getAllCategories().observe(getViewLifecycleOwner(), adapter::setCategories);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_category);
        fab.setOnClickListener(v -> showAddCategoryDialog());

        return view;
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.edit_category_name);
        RadioGroup typeGroup = dialogView.findViewById(R.id.radio_group_type);

        builder.setPositiveButton("Add", (dialog, id) -> {
            String name = editName.getText().toString().trim();
            if (!name.isEmpty()) {
                int selectedId = typeGroup.getCheckedRadioButtonId();
                String type = (selectedId == R.id.radio_income) ? "INCOME" : "EXPENSE";
                // Default color/icon for now
                Category newCategory = new Category(name, "ic_default", "#9E9E9E", type);
                mExpenseViewModel.insertCategory(newCategory);
            } else {
                Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
