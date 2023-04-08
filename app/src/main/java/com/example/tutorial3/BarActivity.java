package com.example.tutorial3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class BarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);

        BarChart chart = (BarChart) findViewById(R.id.idBarChart);

        Button BackButton = (Button) findViewById(R.id.button_back);
        Button openCSV = (Button) findViewById(R.id.btn_openCsv);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickBack();
            }
        });

        openCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLoadCSV();
            }
        });

        ArrayList<String[]> csvData = new ArrayList<>();
        csvData= CsvRead("/sdcard/csv_dir/data.csv");
        ArrayList<Integer> result = DataValues(csvData);
        double mean1 = mean(result);
        double sd1 = sd(result);

        ArrayList<String[]> csvData2 = new ArrayList<>();
        csvData2= CsvRead("/sdcard/csv_dir2/data.csv");
        ArrayList<Integer> result2 = DataValues(csvData2);
        double mean2 = mean(result2);
        double sd2 = sd(result2);

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f,(float)mean1));
        entries.add(new BarEntry(1f,(float)sd1));
        entries.add(new BarEntry(2f,(float)mean2));
        entries.add(new BarEntry(3f,(float)sd2));

        BarDataSet set = new BarDataSet(entries,"BarDataSet");
        set.setColors(new int[] {Color.CYAN, Color.CYAN, Color.MAGENTA, Color.MAGENTA, Color.BLUE});
        BarData data = new BarData(set);
        data.setBarWidth(0.5f);

        chart.setData(data);
        chart.setFitBars(true);
        chart.invalidate();










    }



    private void OpenLoadCSV(){
        Intent intent = new Intent(this,LoadCSV.class);
        startActivity(intent);
    }

    private void ClickBack(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private ArrayList<String[]> CsvRead(String path){
        ArrayList<String[]> CsvData = new ArrayList<>();
        try {
            File file = new File(path);
            CSVReader reader = new CSVReader(new FileReader(file));
            String[]nextline;
            while((nextline = reader.readNext())!= null){
                if(nextline != null){
                    CsvData.add(nextline);

                }
            }

        }catch (Exception e){}
        return CsvData;
    }

    private ArrayList<Integer> DataValues(ArrayList<String[]> csvData){
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < csvData.size(); i++){

            res.add((int)Double.parseDouble(csvData.get(i)[1]));


        }

        return res;
    }

    public static double mean (ArrayList<Integer> table)
    {
        int total = 0;

        for ( int i= 0;i < table.size(); i++)
        {
            int currentNum = table.get(i);
            total+= currentNum;
        }
        return (double) total / (double) table.size();
    }

    public static double sd (ArrayList<Integer> table)
    {
        double mean= mean(table);
        double temp =0;
        for ( int i= 0; i <table.size(); i++)
        {
            temp= Math.pow(i-mean, 2);
        }

        return Math.sqrt(mean( table));
    }

}