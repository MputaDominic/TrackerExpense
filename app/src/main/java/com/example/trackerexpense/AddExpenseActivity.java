package com.example.trackerexpense;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.trackerexpense.data.Category;
import com.example.trackerexpense.data.Expense;
import com.example.trackerexpense.viewmodel.ExpenseViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {

    private TextInputEditText mEditAmount;
    private TextInputEditText mEditDate;
    private TextInputEditText mEditNote;
    private Spinner mSpinnerCategory;
    private ExpenseViewModel mExpenseViewModel;
    private long mSelectedDate;
    private List<Category> mCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        mEditAmount = findViewById(R.id.edit_amount);
        mEditDate = findViewById(R.id.edit_date);
        mEditNote = findViewById(R.id.edit_note);
        mSpinnerCategory = findViewById(R.id.spinner_category);
        Button buttonSave = findViewById(R.id.button_save);

        mExpenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // Set default date to today
        Calendar calendar = Calendar.getInstance();
        mSelectedDate = calendar.getTimeInMillis();
        updateDateLabel();

        // Date picker
        mEditDate.setOnClickListener(v -> {
            new DatePickerDialog(AddExpenseActivity.this, (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mSelectedDate = calendar.getTimeInMillis();
                updateDateLabel();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Setup Category Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(adapter);

        mExpenseViewModel.getAllCategories().observe(this, categories -> {
            mCategories = categories;
            List<String> names = new ArrayList<>();
            for (Category c : categories) {
                names.add(c.name);
            }
            adapter.clear();
            adapter.addAll(names);
            adapter.notifyDataSetChanged();
        });

        buttonSave.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mEditAmount.getText())) {
                Toast.makeText(getApplicationContext(), "Please enter an amount", Toast.LENGTH_LONG).show();
                return;
            }

            double amount = Double.parseDouble(mEditAmount.getText().toString());
            String note = mEditNote.getText().toString();
            int categoryPosition = mSpinnerCategory.getSelectedItemPosition();

            if (categoryPosition >= 0 && categoryPosition < mCategories.size()) {
                 Category selectedCategory = mCategories.get(categoryPosition);
                 Expense expense = new Expense(amount, selectedCategory.id, mSelectedDate, note);
                 mExpenseViewModel.insert(expense);
                 finish();
            } else {
                 Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateDateLabel() {
        mEditDate.setText(DateFormat.getDateInstance().format(new Date(mSelectedDate)));
    }
}
