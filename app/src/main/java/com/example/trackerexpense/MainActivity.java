package com.example.trackerexpense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackerexpense.ui.ExpenseAdapter;
import com.example.trackerexpense.viewmodel.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ExpenseViewModel mExpenseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ExpenseAdapter adapter = new ExpenseAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TextView textTotalExpense = findViewById(R.id.text_total_expense);

        mExpenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        mExpenseViewModel.getAllExpenses().observe(this, expenses -> {
            adapter.setExpenses(expenses);
        });

        mExpenseViewModel.getAllCategories().observe(this, categories -> {
            adapter.setCategories(categories);
        });

        mExpenseViewModel.getTotalExpense().observe(this, total -> {
            if (total != null) {
                textTotalExpense.setText(String.format("$%.2f", total));
            } else {
                textTotalExpense.setText("$0.00");
            }
        });

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }
}
