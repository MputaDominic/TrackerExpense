package com.example.trackerexpense.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.trackerexpense.data.Category;
import com.example.trackerexpense.data.CategoryTotal;
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
        // Note: getAllExpenses in repository was probably returning mAllTransactions (TransactionWithCategory)
        // but here we are casting it or logic was slightly off.
        // However, I see I used mRepository.getAllExpenses() which doesn't exist in the Repository I read earlier?
        // Wait, ExpenseRepository.java had getAllTransactions, not getAllExpenses.
        // I need to fix this call.

        // Actually, let's fix the variable types too.
        // mAllExpenses = mRepository.getAllExpenses(); // This method might not exist in Repository.
        // mRepository has getAllTransactions().

        // I will just use getAllTransactions() logic here, assuming the fragments use ExpenseAdapter
        // which expects List<Expense>.
        // Wait, ExpenseAdapter expects Expense. TransactionWithCategory contains Expense.
        // I should probably update ExpenseAdapter to use TransactionWithCategory or map it.
        // For now, to minimize refactor, I'll rely on ExpenseDao.getAllExpenses() which still exists and returns List<Expense>.

        // But wait, ExpenseRepository I read earlier did NOT have getAllExpenses().
        // It had mAllTransactions = mExpenseDao.getAllTransactions().
        // So mRepository.getAllExpenses() will fail compilation if I don't add it to Repository.

        // I'll add getAllExpenses to Repository first.

        mAllExpenses = mRepository.getAllExpenses(); // I will add this to repo
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
