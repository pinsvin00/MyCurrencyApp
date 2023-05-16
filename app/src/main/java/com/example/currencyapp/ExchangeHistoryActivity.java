
package com.example.currencyapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


class MyDataObject {
    public int x;
    public double y;
    MyDataObject(int x, double y){
        this.x = x;
        this.y = y;
    }
}

public class ExchangeHistoryActivity extends AppCompatActivity {
    private String chartType = "Bar";
    private BarChart barChart;
    private LineChart lineChart;
    private String mainCurrencyCode;
    private String compareCurrencyCode;
    private Date startDate;
    private CurrencyInfoProvider provider;
    private Date endDate;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_history);

        Bundle extras = this.getIntent().getExtras();
        this.mainCurrencyCode = extras.getString("base");
        this.compareCurrencyCode = extras.getString("symbol");

        handler = new Handler();
        provider = new CurrencyInfoProvider();

        barChart = new BarChart(this);
        barChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        lineChart = new LineChart(this);
        lineChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout asd = findViewById(R.id.cippa);
        asd.addView(barChart);
        asd.addView(lineChart);


        Button btn7day = findViewById(R.id.btn7day);
        Button btn14day = findViewById(R.id.btn14day);
        Button btn3month = findViewById(R.id.btn3month);
        Button btn1month = findViewById(R.id.btn1month);
        Button btn6month = findViewById(R.id.btn6month);
        Button btn1year = findViewById(R.id.btn1year);
        Button btnChangeChartType = findViewById(R.id.btn_change_chart_type);


        btnChangeChartType.setOnClickListener(view -> switchChartType());
        btn7day.setOnClickListener(view -> changeTimeFrame("7day"));
        btn14day.setOnClickListener(view -> changeTimeFrame("14day"));
        btn1month.setOnClickListener(view -> changeTimeFrame("1month"));
        btn3month.setOnClickListener(view -> changeTimeFrame("3month"));
        btn6month.setOnClickListener(view -> changeTimeFrame("6month"));
        btn1year.setOnClickListener(view -> changeTimeFrame("1year"));


        changeTimeFrame("14day");
        switchChartType();
    }



    private void changeTimeFrame(String timeFrame){
        endDate = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        switch (timeFrame){
            case "7day":
                calendar.add(Calendar.DATE, -7);
                startDate = calendar.getTime();
                break;
            case "14day":
                calendar.add(Calendar.DATE, -14);
                startDate = calendar.getTime();
                break;
            case "1month":
                calendar.add(Calendar.MONTH, -1);
                startDate = calendar.getTime();
                break;
            case "3month":
                calendar.add(Calendar.MONTH, -3);
                startDate = calendar.getTime();
                break;
            case "6month":
                calendar.add(Calendar.MONTH, -6);
                startDate = calendar.getTime();
                break;
            case "1year":
                calendar.add(Calendar.YEAR, -1);
                startDate = calendar.getTime();
                break;
        }
        refreshChart();
    }

    private void refreshChart(){
        if(mainCurrencyCode == null || compareCurrencyCode == null) return;

        Runnable onFinish = new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        };

        provider.fetchHistory( this.mainCurrencyCode, this.compareCurrencyCode,
                dateFormat.format(startDate), dateFormat.format(endDate),
                handler, onFinish );




    }

    private void loadData() {
        ArrayList<Pair<String, Entry>> entries;
        try {
            entries = provider.getHistoryAsEntries();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }


        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if((int) value < entries.size()) {
                    return entries.get((int) value).first;
                }
                return "";
            }
        };
        ArrayList<Entry> entries1 = new ArrayList<>();
        for(Pair<String, Entry> pair : entries) {
            entries1.add(pair.second);
        }

        switch (chartType){
            case "Line":

                LineDataSet lineDataSet = new LineDataSet(entries1, "Cena wymiany"); // add entries to dataset
                lineDataSet.setColor(R.color.purple_500);
                lineDataSet.setValueTextColor(R.color.purple_500); // styling, ...

                LineData lineData = new LineData(lineDataSet);
                try {
                    lineChart.setData(lineData);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }

                XAxis lineChartXAxis = lineChart.getXAxis();
                lineChartXAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                lineChartXAxis.setLabelCount(3);
                lineChartXAxis.setValueFormatter(formatter);
                lineChart.invalidate(); // refresh
                break;
            case "Bar":
                List<BarEntry> barEntries = new ArrayList<>();
                for (Entry data : entries1) {
                    // turn your data into Entry objects
                    barEntries.add(new BarEntry(data.getX(), (float) data.getY()));
                }

                BarDataSet barDataSet = new BarDataSet(barEntries, "Cena wymiany");
                barDataSet.setColor(R.color.purple_500);
                barDataSet.setValueTextColor(R.color.purple_500);

                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                XAxis barChartXAxis = barChart.getXAxis();
                barChartXAxis.setGranularity(1f);

                barChartXAxis.setValueFormatter(formatter);
                barChart.invalidate();
                break;
        }
    }

    private void switchChartType(){
        switch (chartType){
            case "Line":
                chartType = "Bar";
                lineChart.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);
                break;
            case "Bar":
                chartType = "Line";
                lineChart.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.GONE);
                break;
        }
        refreshChart();
    }

}