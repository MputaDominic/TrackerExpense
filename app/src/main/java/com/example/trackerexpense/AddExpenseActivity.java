package com.example.trackerexpense;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
    private RadioGroup mRadioGroupType;
    private ExpenseViewModel mExpenseViewModel;
    private long mSelectedDate;
    private List<Category> mCategories = new ArrayList<>();
    private ArrayAdapter<String> mCategoryAdapter;
    private LiveData<List<Category>> mCurrentCategoryList;

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
        mRadioGroupType = findViewById(R.id.radio_group_type);
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
        mCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        mCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(mCategoryAdapter);

        // Radio Group Listener
        mRadioGroupType.setOnCheckedChangeListener((group, checkedId) -> {
            String type = (checkedId == R.id.radio_income) ? "INCOME" : "EXPENSE";
            loadCategories(type);
        });

        // Initial Load (Default to Expense)
        loadCategories("EXPENSE");

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

    private void loadCategories(String type) {
        if (mCurrentCategoryList != null) {
            mCurrentCategoryList.removeObservers(this);
        }

        mCurrentCategoryList = mExpenseViewModel.getCategoriesByType(type);
        mCurrentCategoryList.observe(this, categories -> {
            mCategories = categories;
            List<String> names = new ArrayList<>();
            for (Category c : categories) {
                names.add(c.name);
            }
            mCategoryAdapter.clear();
            mCategoryAdapter.addAll(names);
            mCategoryAdapter.notifyDataSetChanged();

            if (names.isEmpty()) {
                // If no categories for this type, maybe add a default one or warn user?
                // For now, let it be empty. The spinner will be empty.
                // Or "Add Category" activity/dialog should be triggered.
                // But Add Category is in MainActivity -> Category Fragment.
            }
        });
    }

    private void updateDateLabel() {
        mEditDate.setText(DateFormat.getDateInstance().format(new Date(mSelectedDate)));
    }
}
