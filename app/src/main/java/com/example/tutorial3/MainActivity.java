package com.example.tutorial3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.CSVWriter;

import java.util.ArrayList;
import java.util.Random;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class MainActivity extends AppCompatActivity {
    LineChart mpLineChart;
    int counter = 1;
    int val = 40;
    int val2 = 0;
    private Handler mHandlar = new Handler();  //Handlar is used for delay definition in the loop



    public MainActivity() throws FileNotFoundException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }





        mpLineChart = (LineChart) findViewById(R.id.line_chart);
        LineDataSet lineDataSet1 =  new LineDataSet(dataValues1(), "Data Set 1");
        LineDataSet lineDataSet2 =  new LineDataSet(dataValues1(), "Data Set 2");
        lineDataSet2.setColor(Color.MAGENTA);
        lineDataSet2.setCircleColor(Color.MAGENTA);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        LineData data = new LineData(dataSets);
        mpLineChart.setData(data);
        mpLineChart.invalidate();

        Button buttonClear = (Button) findViewById(R.id.button1);
        Button buttonCsvShow = (Button) findViewById(R.id.button2);
        Button buttonBarShow = (Button) findViewById(R.id.button7);

        buttonCsvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLoadCSV();

            }
        });

        buttonBarShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenBarActivity();
            }
        });







        LineDataSet finalLineDataSet = lineDataSet1;
        LineDataSet finalLineDataSet2 = lineDataSet2;

        Runnable DataUpdate = new Runnable(){
            @Override
            public void run() {

                data.addEntry(new Entry(counter,val),0);
                finalLineDataSet.notifyDataSetChanged();
                mpLineChart.notifyDataSetChanged(); // let the chart know it's data changed
                mpLineChart.invalidate(); // refresh
                val = (int) (Math.random() * 80);

                saveToCsv("/sdcard/csv_dir/",String.valueOf(counter),String.valueOf(val));

                data.addEntry(new Entry(counter,val2),1);
                finalLineDataSet2.notifyDataSetChanged();
                mpLineChart.notifyDataSetChanged(); // let the chart know it's data changed
                mpLineChart.invalidate(); // refresh
                Random ran = new Random();
                val2 = (int) ran.nextGaussian() * 80;

                saveToCsv("/sdcard/csv_dir2/",String.valueOf(counter),String.valueOf(val2));

                counter += 1;
                mHandlar.postDelayed(this,500);


            }
        };

        buttonClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Clear",Toast.LENGTH_SHORT).show();
                LineData data = mpLineChart.getData();
                ILineDataSet set = data.getDataSetByIndex(0);
                ILineDataSet set2 = data.getDataSetByIndex(1);
                data.getDataSetByIndex(0);
                while(set.removeLast()){}
                data.getDataSetByIndex(1);
                while(set2.removeLast()){}
                val=40;
                val2 =  0;
                counter = 1;

            }
        });

        
        mHandlar.postDelayed(DataUpdate,500);
    }



    private ArrayList<Entry> dataValues1()
    {
        ArrayList<Entry> dataVals = new ArrayList<Entry>();
        dataVals.add(new Entry(0,0));
        return dataVals;
    }

    private void saveToCsv(String path,String str1, String str2){
        try{
            File file = new File(path);
            file.mkdirs();
            String csv = path + "data.csv";
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csv,true));
            String row[]= new String[]{str1,str2};
            csvWriter.writeNext(row);
            csvWriter.close();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
    }
   private void OpenLoadCSV(){
     Intent intent = new Intent(this,LoadCSV.class);
     startActivity(intent);
   }

    private void OpenBarActivity(){
        Intent intent = new Intent(this,BarActivity.class);
        startActivity(intent);
    }


}
