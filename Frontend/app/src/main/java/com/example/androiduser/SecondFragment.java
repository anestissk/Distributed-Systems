package com.example.androiduser;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    private BarChart barChartDistance;
    private BarChart barChartElapsedTime;
    private BarChart barChartElevation;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        barChartDistance = view.findViewById(R.id.barChartDistance);
        barChartElapsedTime = view.findViewById(R.id.barChartElapsedTime);
        barChartElevation = view.findViewById(R.id.barChartElevation);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupDistanceChart();
        setupElapsedTimeChart();
        setupElevationChart();
    }

    private void setupDistanceChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, getUserDistance()));
        entries.add(new BarEntry(1, getTotalAverageDistance()));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(getDistanceChartColors());

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChartDistance.setData(data);
        barChartDistance.getAxisLeft().setAxisMinimum(0f); 


        String[] labels = {"User", "Total Average"};
        XAxis xAxis = barChartDistance.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChartDistance.invalidate();
    }

    private void setupElapsedTimeChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, getUserElapsedTime()));
        entries.add(new BarEntry(1, getTotalAverageElapsedTime()));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(getElapsedTimeChartColors());

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChartElapsedTime.setData(data);
        barChartElapsedTime.getAxisLeft().setAxisMinimum(0f);


        String[] labels = {"User", "Total Average"};
        XAxis xAxis = barChartElapsedTime.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChartElapsedTime.invalidate();
    }

    private void setupElevationChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, getUserElevation()));
        entries.add(new BarEntry(1, getTotalAverageElevation()));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColors(getElevationChartColors());

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);

        barChartElevation.setData(data);
        barChartElevation.getAxisLeft().setAxisMinimum(0f);


        String[] labels = {"User", "Total Average"};
        XAxis xAxis = barChartElevation.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        barChartElevation.invalidate();
    }


    private float getUserDistance() {

        return sharedPreferences.getFloat("averageUserDistance", 0);
    }

    private float getTotalAverageDistance() {

        return sharedPreferences.getFloat("averageTotalDistance", 0);
    }

    private float getUserElapsedTime() {

        return sharedPreferences.getFloat("averageUserElapsedTime", 0);
    }

    private float getTotalAverageElapsedTime() {

        return sharedPreferences.getFloat("averageTotalElapsedTime", 0);
    }

    private float getUserElevation() {

        return sharedPreferences.getFloat("averageUserElevation", 0);
    }

    private float getTotalAverageElevation() {

        return sharedPreferences.getFloat("averageTotalElevation", 0);
    }


    private int[] getDistanceChartColors() {
        return new int[]{Color.RED, Color.GREEN};
    }


    private int[] getElapsedTimeChartColors() {
        return new int[]{Color.BLUE, Color.YELLOW};
    }


    private int[] getElevationChartColors() {
        return new int[]{Color.MAGENTA, Color.CYAN};
    }
}
