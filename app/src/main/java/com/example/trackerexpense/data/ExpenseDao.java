package com.example.trackerexpense.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();

    @androidx.room.Transaction
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<TransactionWithCategory>> getAllTransactions();

    // Assuming we want to sum ONLY expenses for TotalExpense, and ONLY income for TotalIncome
    // But existing code just summed everything.
    // I will update to filter by category type.

    @Query("SELECT SUM(expenses.amount) FROM expenses INNER JOIN categories ON expenses.categoryId = categories.id WHERE categories.type = 'EXPENSE'")
    LiveData<Double> getTotalExpense();

    @Query("SELECT SUM(expenses.amount) FROM expenses INNER JOIN categories ON expenses.categoryId = categories.id WHERE categories.type = 'INCOME'")
    LiveData<Double> getTotalIncome();

    @Query("SELECT categories.name as categoryName, SUM(expenses.amount) as totalAmount, categories.color as color, categories.type as type " +
           "FROM expenses INNER JOIN categories ON expenses.categoryId = categories.id " +
           "WHERE expenses.date >= :startDate AND expenses.date <= :endDate AND categories.type = :type " +
           "GROUP BY categories.id")
    LiveData<List<CategoryTotal>> getCategoryTotals(long startDate, long endDate, String type);
}
