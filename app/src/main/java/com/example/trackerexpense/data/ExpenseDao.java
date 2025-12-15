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

    @Query("SELECT SUM(amount) FROM expenses")
    LiveData<Double> getTotalExpense();
}
