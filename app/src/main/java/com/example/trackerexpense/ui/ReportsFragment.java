package com.example.trackerexpense.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackerexpense.R;
import com.example.trackerexpense.data.CategoryTotal;
import com.example.trackerexpense.viewmodel.ExpenseViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    private ExpenseViewModel mExpenseViewModel;
    private PieChart pieChart;
    private ReportsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        pieChart = view.findViewById(R.id.pieChart);
        setupPieChart();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_reports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportsAdapter();
        recyclerView.setAdapter(adapter);

        mExpenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // For demo, we just get totals for all time.
        // In real app, we would handle date ranges.
        // We use a very wide range for "All Time"
        long start = 0;
        long end = Long.MAX_VALUE;

        mExpenseViewModel.getCategoryTotals(start, end, "EXPENSE").observe(getViewLifecycleOwner(), totals -> {
            updateChart(totals);
            adapter.setCategoryTotals(totals);
        });

        return view;
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setEntryLabelColor(Color.BLACK); // Make labels visible
    }

    private void updateChart(List<CategoryTotal> totals) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (CategoryTotal total : totals) {
            if (total.totalAmount > 0) {
                entries.add(new PieEntry((float) total.totalAmount, total.categoryName));
            }
        }

        if (entries.isEmpty()) {
            pieChart.clear();
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expenses");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }
}
