package com.example.trackerexpense.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String icon; // Icon name or resource ID reference
    public String color; // Hex color code
    public String type; // "INCOME" or "EXPENSE"

    public Category(String name, String icon, String color, String type) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.type = type;
    }
}
