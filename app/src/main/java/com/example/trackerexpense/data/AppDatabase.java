package com.example.trackerexpense.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class, Category.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ExpenseDao expenseDao();
    public abstract CategoryDao categoryDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "expense_tracker_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@androidx.annotation.NonNull androidx.sqlite.db.SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Populate the database in the background.
            databaseWriteExecutor.execute(() -> {
                // Populate the database with default categories
                CategoryDao dao = INSTANCE.categoryDao();
                dao.insert(new Category("Food", "ic_food", "#FF5722", "EXPENSE"));
                dao.insert(new Category("Transport", "ic_transport", "#2196F3", "EXPENSE"));
                dao.insert(new Category("Shopping", "ic_shopping", "#E91E63", "EXPENSE"));
                dao.insert(new Category("Health", "ic_health", "#4CAF50", "EXPENSE"));
                dao.insert(new Category("Entertainment", "ic_entertainment", "#9C27B0", "EXPENSE"));
                dao.insert(new Category("Salary", "ic_salary", "#4CAF50", "INCOME"));
            });
        }
    };
}
