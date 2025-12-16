package com.example.trackerexpense.data;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TransactionWithCategory {
    @Embedded
    public Expense expense;

    @Relation(
            parentColumn = "categoryId",
            entityColumn = "id"
    )
    public Category category;
}
