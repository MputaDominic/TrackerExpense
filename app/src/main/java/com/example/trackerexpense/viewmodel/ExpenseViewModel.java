package com.example.trackerexpense.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.trackerexpense.data.Category;
import com.example.trackerexpense.data.CategoryTotal;
import com.example.trackerexpense.data.Expense;
import com.example.trackerexpense.data.ExpenseRepository;
import com.example.trackerexpense.data.TransactionWithCategory;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository mRepository;

    private final LiveData<List<Expense>> mAllExpenses;
    private final LiveData<List<TransactionWithCategory>> mAllTransactions;
    private final LiveData<List<Category>> mAllCategories;
    private final LiveData<Double> mTotalExpense;
    private final LiveData<Double> mTotalIncome;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ExpenseRepository(application);
        mAllExpenses = mRepository.getAllExpenses();
        mAllTransactions = mRepository.getAllTransactions();
        mAllCategories = mRepository.getAllCategories();
        mTotalExpense = mRepository.getTotalExpense();
        mTotalIncome = mRepository.getTotalIncome();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return mAllExpenses;
    }

    public LiveData<List<TransactionWithCategory>> getAllTransactions() {
        return mAllTransactions;
    }

    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    public LiveData<List<Category>> getCategoriesByType(String type) {
        return mRepository.getCategoriesByType(type);
    }

    public LiveData<Double> getTotalExpense() {
        return mTotalExpense;
    }

    public LiveData<Double> getTotalIncome() {
        return mTotalIncome;
    }

    public LiveData<List<CategoryTotal>> getCategoryTotals(long startDate, long endDate, String type) {
        return mRepository.getCategoryTotals(startDate, endDate, type);
    }

    public void insert(Expense expense) {
        mRepository.insert(expense);
    }

    public void insertCategory(Category category) {
        mRepository.insertCategory(category);
    }

    public void deleteCategory(Category category) {
        mRepository.deleteCategory(category);
    }
}
