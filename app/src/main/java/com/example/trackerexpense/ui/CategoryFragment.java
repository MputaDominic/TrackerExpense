package com.example.trackerexpense.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.LiveData;

import com.example.trackerexpense.R;
import com.example.trackerexpense.data.Category;
import com.example.trackerexpense.viewmodel.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class CategoryFragment extends Fragment {

    private ExpenseViewModel mExpenseViewModel;
    private CategoryAdapter adapter;
    private String currentType = "EXPENSE"; // Default
    private LiveData<List<Category>> currentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CategoryAdapter(category -> {
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

        TabLayout tabLayout = view.findViewById(R.id.tab_layout_category);

        // Select Expense default
        TabLayout.Tab expenseTab = tabLayout.getTabAt(1);
        if (expenseTab != null) {
            expenseTab.select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    currentType = "INCOME";
                } else {
                    currentType = "EXPENSE";
                }
                loadCategories();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        loadCategories();

        FloatingActionButton fab = view.findViewById(R.id.fab_add_category);
        fab.setOnClickListener(v -> showAddCategoryDialog());

        return view;
    }

    private void loadCategories() {
        if (currentList != null) {
            currentList.removeObservers(getViewLifecycleOwner());
        }
        currentList = mExpenseViewModel.getCategoriesByType(currentType);
        currentList.observe(getViewLifecycleOwner(), categories -> {
            adapter.setCategories(categories);
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_category, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.edit_category_name);
        RadioGroup typeGroup = dialogView.findViewById(R.id.radio_group_type);

        // Pre-select type based on current tab
        if (currentType.equals("INCOME")) {
            RadioButton rb = dialogView.findViewById(R.id.radio_income);
            rb.setChecked(true);
        } else {
            RadioButton rb = dialogView.findViewById(R.id.radio_expense);
            rb.setChecked(true);
        }

        builder.setPositiveButton("Add", (dialog, id) -> {
            String name = editName.getText().toString().trim();
            if (!name.isEmpty()) {
                int selectedId = typeGroup.getCheckedRadioButtonId();
                String type = (selectedId == R.id.radio_income) ? "INCOME" : "EXPENSE";
                // Default color generation (simple hash or random for demo)
                String color = generateColor(name);
                Category newCategory = new Category(name, "ic_default", color, type);
                mExpenseViewModel.insertCategory(newCategory);
            } else {
                Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private String generateColor(String name) {
        // Generate a random color or based on name
        int hash = name.hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return String.format("#%06X", (0xFFFFFF & hash));
    }
}
