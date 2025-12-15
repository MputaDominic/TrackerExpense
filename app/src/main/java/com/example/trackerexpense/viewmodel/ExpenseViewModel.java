package com.example.trackerexpense.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.trackerexpense.data.Category;
import com.example.trackerexpense.data.Expense;
import com.example.trackerexpense.data.ExpenseRepository;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository mRepository;

    private final LiveData<List<Expense>> mAllExpenses;
    private final LiveData<List<Category>> mAllCategories;
    private final LiveData<Double> mTotalExpense;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ExpenseRepository(application);
        mAllExpenses = mRepository.getAllExpenses();
        mAllCategories = mRepository.getAllCategories();
        mTotalExpense = mRepository.getTotalExpense();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return mAllExpenses;
    }

    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    public LiveData<Double> getTotalExpense() {
        return mTotalExpense;
    }

    public void insert(Expense expense) {
        mRepository.insert(expense);
    }

    public void insertCategory(Category category) {
        mRepository.insertCategory(category);
    }
}
