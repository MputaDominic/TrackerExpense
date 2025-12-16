package com.example.trackerexpense.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportsFragment extends Fragment {

    private ExpenseViewModel mExpenseViewModel;
    private PieChart pieChart;
    private ReportsAdapter adapter;
    private TabLayout tabLayout;
    private Spinner dateSpinner;
    private TextView chartTitle;

    private String currentType = "EXPENSE";
    private long startDate = 0;
    private long endDate = Long.MAX_VALUE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        pieChart = view.findViewById(R.id.pieChart);
        tabLayout = view.findViewById(R.id.tab_layout);
        dateSpinner = view.findViewById(R.id.spinner_date);
        chartTitle = view.findViewById(R.id.text_chart_title);

        setupPieChart();
        setupTabs();
        setupDateSpinner();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_reports);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReportsAdapter();
        recyclerView.setAdapter(adapter);

        mExpenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // Initial Load
        loadData();

        return view;
    }

    private void setupTabs() {
        TabLayout.Tab expenseTab = tabLayout.getTabAt(1);
        if (expenseTab != null) {
            expenseTab.select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    currentType = "INCOME";
                    chartTitle.setText("Income by categories");
                } else {
                    currentType = "EXPENSE";
                    chartTitle.setText("Expenses by categories");
                }
                loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupDateSpinner() {
        List<String> dates = new ArrayList<>();
        dates.add("Dec 2025");
        dates.add("All Time");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Dec 2025
                    Calendar cal = Calendar.getInstance();
                    cal.set(2025, Calendar.DECEMBER, 1, 0, 0, 0);
                    startDate = cal.getTimeInMillis();
                    cal.set(2025, Calendar.DECEMBER, 31, 23, 59, 59);
                    endDate = cal.getTimeInMillis();
                } else {
                    // All Time
                    startDate = 0;
                    endDate = Long.MAX_VALUE;
                }
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.parseColor("#2C2C2C"));
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.setCenterTextSize(16f);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.getLegend().setEnabled(false);
    }

    private void loadData() {
        if (mExpenseViewModel == null) return;

        mExpenseViewModel.getCategoryTotals(startDate, endDate, currentType).observe(getViewLifecycleOwner(), totals -> {
            updateChart(totals);
            adapter.setCategoryTotals(totals);
        });
    }

    private void updateChart(List<CategoryTotal> totals) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        double totalAmount = 0;

        for (CategoryTotal total : totals) {
            if (total.totalAmount > 0) {
                entries.add(new PieEntry((float) total.totalAmount, ""));
                totalAmount += total.totalAmount;
            }
        }

        pieChart.setCenterText(String.format("%.1fKSh", totalAmount));

        if (entries.isEmpty()) {
            pieChart.clear();
            pieChart.setCenterText("No Data");
            return;
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (CategoryTotal total : totals) {
             if (total.totalAmount > 0) {
                 try {
                     colors.add(Color.parseColor(total.color));
                 } catch (Exception e) {
                     colors.add(Color.GRAY);
                 }
             }
        }

        if (colors.isEmpty()) {
             colors.add(Color.GRAY);
        }
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(0f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.invalidate();
    }
}
