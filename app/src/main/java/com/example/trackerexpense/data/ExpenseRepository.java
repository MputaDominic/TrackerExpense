package com.example.trackerexpense.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseRepository {

    private ExpenseDao mExpenseDao;
    private CategoryDao mCategoryDao;
    private LiveData<List<Expense>> mAllExpenses;
    private LiveData<List<Category>> mAllCategories;
    private LiveData<Double> mTotalExpense;

    public ExpenseRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mExpenseDao = db.expenseDao();
        mCategoryDao = db.categoryDao();
        mAllExpenses = mExpenseDao.getAllExpenses();
        mAllCategories = mCategoryDao.getAllCategories();
        mTotalExpense = mExpenseDao.getTotalExpense();
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
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mExpenseDao.insert(expense);
        });
    }

    public void insertCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.insert(category);
        });
    }
}
