package com.example.trackerexpense.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackerexpense.AddExpenseActivity;
import com.example.trackerexpense.R;
import com.example.trackerexpense.viewmodel.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class TransactionsFragment extends Fragment {

    private ExpenseViewModel mExpenseViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final ExpenseAdapter adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView textTotalExpense = view.findViewById(R.id.text_total_expense);

        mExpenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        mExpenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            adapter.setExpenses(expenses);
        });

        mExpenseViewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {
            adapter.setCategories(categories);
        });

        mExpenseViewModel.getTotalExpense().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                textTotalExpense.setText(String.format("$%.2f", total));
            } else {
                textTotalExpense.setText("$0.00");
            }
        });

        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
