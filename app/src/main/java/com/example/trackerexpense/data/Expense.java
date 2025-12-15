package com.example.trackerexpense.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "expenses",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.SET_NULL))
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public double amount;
    public Integer categoryId; // Nullable if category is deleted
    public Long date; // Stored as timestamp
    public String note;

    public Expense(double amount, Integer categoryId, Long date, String note) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
        this.note = note;
    }
}
