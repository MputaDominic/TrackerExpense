package com.example.trackerexpense.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ExpenseRepository {

    private ExpenseDao mExpenseDao;
    private CategoryDao mCategoryDao;
    private LiveData<List<Expense>> mAllExpenses;
    private LiveData<List<TransactionWithCategory>> mAllTransactions;
    private LiveData<List<Category>> mAllCategories;
    private LiveData<Double> mTotalExpense;
    private LiveData<Double> mTotalIncome;

    public ExpenseRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mExpenseDao = db.expenseDao();
        mCategoryDao = db.categoryDao();
        mAllExpenses = mExpenseDao.getAllExpenses();
        mAllTransactions = mExpenseDao.getAllTransactions();
        mAllCategories = mCategoryDao.getAllCategories();
        mTotalExpense = mExpenseDao.getTotalExpense();
        mTotalIncome = mExpenseDao.getTotalIncome();
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
        return mCategoryDao.getCategoriesByType(type);
    }

    public LiveData<Double> getTotalExpense() {
        return mTotalExpense;
    }

    public LiveData<Double> getTotalIncome() {
        return mTotalIncome;
    }

    public LiveData<List<CategoryTotal>> getCategoryTotals(long startDate, long endDate, String type) {
        return mExpenseDao.getCategoryTotals(startDate, endDate, type);
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

    public void deleteCategory(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mCategoryDao.delete(category);
        });
    }
}
