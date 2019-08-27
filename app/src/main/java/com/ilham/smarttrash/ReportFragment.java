package com.ilham.smarttrash;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Handler;

import com.ilham.smarttrash.modelSensortes.ResultItem;
import com.ilham.smarttrash.modelSensortes.Value;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ReportFragment extends Fragment {

    private String URL = "http://tasmarttrash.site/";
//    private static final String URL = "http://192.168.43.32/";
//    private static final String URL = "http://192.168.42.212/esloin/";
//    private static final String URL = "http://192.168.1.18/esloin/";

    SharedPreferences mData;

    TextView txtStatus;
    TextView txtJarak;
    TextView txtKelembaban;
    TextView txtSuhu;

    GraphView graph1;
//    GraphView graph2;
    LineGraphSeries<DataPoint> series1;
    LineGraphSeries<DataPoint> series2;



    private List<ResultItem> results;

    private Handler handler = new Handler();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        mData = getActivity().getSharedPreferences("SAVE_DATA", Context.MODE_PRIVATE);

        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        txtJarak = (TextView) view.findViewById(R.id.txtJarak);
        txtSuhu = (TextView) view.findViewById(R.id.txtSuhu);
        txtKelembaban = (TextView) view.findViewById(R.id.txtKelembaban);

        graph1 = (GraphView) view.findViewById(R.id.graph1);

        // set manual X bounds
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(12);

        // set manual Y bounds
        graph1.getViewport().setYAxisBoundsManual(true);
        graph1.getViewport().setMinY(0);
        graph1.getViewport().setMaxY(100);

        handler.post(runnable1);
        handler.post(runnable2);

        return view;
    }

    private void loadData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Value> call = api.view();
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, retrofit2.Response<Value> response) {
                String value = response.body().getValue();
                if (value.equals("1")) {
                    results = response.body().getResult();

//                    Log.d("cek", String.valueOf(results.get(0).getUltrasonik()));

                    if (results.size() > 0) {
                        Integer check = results.get(results.size() - 1).getUltrasonik();

                        txtJarak.setText("Data Ultrasonik : " + results.get(results.size() - 1).getUltrasonik() + " cm");
                        txtKelembaban.setText(results.get(results.size() - 1).getKelembaban() + " %");
                        txtSuhu.setText(results.get(results.size() - 1).getSuhu() + " °C");

                        SharedPreferences.Editor editor = mData.edit();
                        editor.putInt("statusSampah", check);
                        editor.apply();
                    }else{
                        txtJarak.setText("Data Ultrasonik : 0 cm");
                        txtKelembaban.setText("0 %");
                        txtSuhu.setText("0 °C");

                        SharedPreferences.Editor editor = mData.edit();
                        editor.putInt("statusSampah", 99);
                        editor.apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                txtJarak.setText("Data Ultrasonik : 0 cm");
                txtKelembaban.setText("0 %");
                txtSuhu.setText("0 °C");

                SharedPreferences.Editor editor = mData.edit();
                editor.putInt("statusSampah", 99);
                editor.apply();
            }
        });
    }

    private void loadData1(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Value> call = api.view();

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, retrofit2.Response<Value> response) {
//                graph1.removeSeries(series1);
                graph1.removeAllSeries();
                String value = response.body().getValue();
                if (value.equals("1")) {
                    results = response.body().getResult();

                    Log.d("size", String.valueOf(results.size()));

                    if(results.size() == 0){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0)
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0)
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 1){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 2){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 3){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 4){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 5){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 5).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 5).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 6){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 5).getKelembaban()),
                                new DataPoint(6, results.get(results.size() - 6).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 5).getSuhu()),
                                new DataPoint(6, results.get(results.size() - 6).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 7){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 5).getKelembaban()),
                                new DataPoint(6, results.get(results.size() - 6).getKelembaban()),
                                new DataPoint(7, results.get(results.size() - 7).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 5).getSuhu()),
                                new DataPoint(6, results.get(results.size() - 6).getSuhu()),
                                new DataPoint(7, results.get(results.size() - 7).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 8){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 5).getKelembaban()),
                                new DataPoint(6, results.get(results.size() - 6).getKelembaban()),
                                new DataPoint(7, results.get(results.size() - 7).getKelembaban()),
                                new DataPoint(8, results.get(results.size() - 8).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 5).getSuhu()),
                                new DataPoint(6, results.get(results.size() - 6).getSuhu()),
                                new DataPoint(7, results.get(results.size() - 7).getSuhu()),
                                new DataPoint(8, results.get(results.size() - 8).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 9){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 5).getKelembaban()),
                                new DataPoint(6, results.get(results.size() - 6).getKelembaban()),
                                new DataPoint(7, results.get(results.size() - 7).getKelembaban()),
                                new DataPoint(8, results.get(results.size() - 8).getKelembaban()),
                                new DataPoint(9, results.get(results.size() - 9).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 5).getSuhu()),
                                new DataPoint(6, results.get(results.size() - 6).getSuhu()),
                                new DataPoint(7, results.get(results.size() - 7).getSuhu()),
                                new DataPoint(8, results.get(results.size() - 8).getSuhu()),
                                new DataPoint(9, results.get(results.size() - 9).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 10){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 5).getKelembaban()),
                                new DataPoint(6, results.get(results.size() - 6).getKelembaban()),
                                new DataPoint(7, results.get(results.size() - 7).getKelembaban()),
                                new DataPoint(8, results.get(results.size() - 8).getKelembaban()),
                                new DataPoint(9, results.get(results.size() - 9).getKelembaban()),
                                new DataPoint(10, results.get(results.size() - 10).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 5).getSuhu()),
                                new DataPoint(6, results.get(results.size() - 6).getSuhu()),
                                new DataPoint(7, results.get(results.size() - 7).getSuhu()),
                                new DataPoint(8, results.get(results.size() - 8).getSuhu()),
                                new DataPoint(9, results.get(results.size() - 9).getSuhu()),
                                new DataPoint(10, results.get(results.size() - 10).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() == 11){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 5).getKelembaban()),
                                new DataPoint(6, results.get(results.size() - 6).getKelembaban()),
                                new DataPoint(7, results.get(results.size() - 7).getKelembaban()),
                                new DataPoint(8, results.get(results.size() - 8).getKelembaban()),
                                new DataPoint(9, results.get(results.size() - 9).getKelembaban()),
                                new DataPoint(10, results.get(results.size() - 10).getKelembaban()),
                                new DataPoint(11, results.get(results.size() - 11).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 5).getSuhu()),
                                new DataPoint(6, results.get(results.size() - 6).getSuhu()),
                                new DataPoint(7, results.get(results.size() - 7).getSuhu()),
                                new DataPoint(8, results.get(results.size() - 8).getSuhu()),
                                new DataPoint(9, results.get(results.size() - 9).getSuhu()),
                                new DataPoint(10, results.get(results.size() - 10).getSuhu()),
                                new DataPoint(11, results.get(results.size() - 11).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    } else if(results.size() == 12){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 5).getKelembaban()),
                                new DataPoint(6, results.get(results.size() - 6).getKelembaban()),
                                new DataPoint(7, results.get(results.size() - 7).getKelembaban()),
                                new DataPoint(8, results.get(results.size() - 8).getKelembaban()),
                                new DataPoint(9, results.get(results.size() - 9).getKelembaban()),
                                new DataPoint(10, results.get(results.size() - 10).getKelembaban()),
                                new DataPoint(11, results.get(results.size() - 11).getKelembaban()),
                                new DataPoint(12, results.get(results.size() - 12).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 0),
                                new DataPoint(1, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 5).getSuhu()),
                                new DataPoint(6, results.get(results.size() - 6).getSuhu()),
                                new DataPoint(7, results.get(results.size() - 7).getSuhu()),
                                new DataPoint(8, results.get(results.size() - 8).getSuhu()),
                                new DataPoint(9, results.get(results.size() - 9).getSuhu()),
                                new DataPoint(10, results.get(results.size() - 10).getSuhu()),
                                new DataPoint(11, results.get(results.size() - 11).getSuhu()),
                                new DataPoint(12, results.get(results.size() - 12).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }else if(results.size() > 12){
                        series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, results.get(results.size() - 1).getKelembaban()),
                                new DataPoint(1, results.get(results.size() - 2).getKelembaban()),
                                new DataPoint(2, results.get(results.size() - 3).getKelembaban()),
                                new DataPoint(3, results.get(results.size() - 4).getKelembaban()),
                                new DataPoint(4, results.get(results.size() - 5).getKelembaban()),
                                new DataPoint(5, results.get(results.size() - 6).getKelembaban()),
                                new DataPoint(6, results.get(results.size() - 7).getKelembaban()),
                                new DataPoint(7, results.get(results.size() - 8).getKelembaban()),
                                new DataPoint(8, results.get(results.size() - 9).getKelembaban()),
                                new DataPoint(9, results.get(results.size() - 10).getKelembaban()),
                                new DataPoint(10, results.get(results.size() - 11).getKelembaban()),
                                new DataPoint(11, results.get(results.size() - 12).getKelembaban()),
                                new DataPoint(12, results.get(results.size() - 13).getKelembaban())
                        });
                        graph1.addSeries(series1);
                        series1.setColor(Color.GREEN);

                        series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, results.get(results.size() - 1).getSuhu()),
                                new DataPoint(1, results.get(results.size() - 2).getSuhu()),
                                new DataPoint(2, results.get(results.size() - 3).getSuhu()),
                                new DataPoint(3, results.get(results.size() - 4).getSuhu()),
                                new DataPoint(4, results.get(results.size() - 5).getSuhu()),
                                new DataPoint(5, results.get(results.size() - 6).getSuhu()),
                                new DataPoint(6, results.get(results.size() - 7).getSuhu()),
                                new DataPoint(7, results.get(results.size() - 8).getSuhu()),
                                new DataPoint(8, results.get(results.size() - 9).getSuhu()),
                                new DataPoint(9, results.get(results.size() - 10).getSuhu()),
                                new DataPoint(10, results.get(results.size() - 11).getSuhu()),
                                new DataPoint(11, results.get(results.size() - 12).getSuhu()),
                                new DataPoint(12, results.get(results.size() - 13).getSuhu())
                        });
                        graph1.addSeries(series2);
                        series2.setColor(Color.RED);

                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 0)
                });
                graph1.addSeries(series1);
                series1.setColor(Color.GREEN);

                series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 0)
                });
                graph1.addSeries(series2);
                series2.setColor(Color.RED);
            }
        });
    }

//    private void loadData2(){
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RegisterAPI api = retrofit.create(RegisterAPI.class);
//        Call<Value> call = api.view();
//
//        call.enqueue(new Callback<Value>() {
//            @Override
//            public void onResponse(Call<Value> call, retrofit2.Response<Value> response) {
//                graph1.removeSeries(series2);
//                String value2 = response.body().getValue();
//                if (value2.equals("1")) {
//                    results = response.body().getResult();
//                    float suhu1;
//                    float suhu2;
//                    float suhu3;
//                    float suhu4;
//                    float suhu5;
//                    float suhu6;
//                    float suhu7;
//                    float suhu8;
//                    float suhu9;
//                    float suhu10;
//                    float suhu11;
//                    float suhu12;
//
//                    //dht = results.get(0).getDht();
////                    suhu1 = results.get(11).getSuhu();
////                    suhu2 = results.get(10).getSuhu();
////                    suhu3 = results.get(9).getSuhu();
////                    suhu4 = results.get(8).getSuhu();
////                    suhu5 = results.get(7).getSuhu();
////                    suhu6 = results.get(6).getSuhu();
////                    suhu7 = results.get(5).getSuhu();
////                    suhu8 = results.get(4).getSuhu();
////                    suhu9 = results.get(3).getSuhu();
////                    suhu10 = results.get(2).getSuhu();
////                    suhu11 = results.get(1).getSuhu();
////                    suhu12 = results.get(0).getSuhu();
////
////                    series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
////                            new DataPoint(0, 0),
////                            new DataPoint(1, suhu1),
////                            new DataPoint(2, suhu2),
////                            new DataPoint(3, suhu3),
////                            new DataPoint(4, suhu4),
////                            new DataPoint(5, suhu5),
////                            new DataPoint(6, suhu6),
////                            new DataPoint(7, suhu7),
////                            new DataPoint(8, suhu8),
////                            new DataPoint(9, suhu9),
////                            new DataPoint(10, suhu10),
////                            new DataPoint(11, suhu11),
////                            new DataPoint(12, suhu12)
////                    });
////
////                    graph1.addSeries(series2);
////                    series2.setColor(Color.RED);
//
//                }
//                else {
////                    series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
////                            new DataPoint(0, 0),
////                            new DataPoint(1, 0),
////                            new DataPoint(2, 0),
////                            new DataPoint(3, 0),
////                            new DataPoint(4, 0)
////                    });
////
////                    graph2.addSeries(series2);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Value> call, Throwable t) {
//
//            }
//        });
//    }

    private void status(){

//        SharedPreferences mData = getActivity().getSharedPreferences("SAVE_DATA", Context.MODE_PRIVATE);
        int status = mData.getInt("statusSampah", -1);

        if(status <= 15) {
            txtStatus.setText("Status Sampah : Penuh");
        }
        else {
            txtStatus.setText("Status Sampah : Belum Penuh");
        }
    }

    // Define the code block to be executed
    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            // Insert custom code here

            loadData();
            loadData1();
//            loadData2();


            // Repeat every 2 seconds
            handler.postDelayed(runnable1, 3000);
        }
    };

    // Define the code block to be executed
    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            // Insert custom code here
            status();


            // Repeat every 2 seconds
            handler.postDelayed(runnable2, 1000);
        }
    };

}
