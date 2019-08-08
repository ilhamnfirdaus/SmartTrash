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

//    private String URL = "https://smarttrash.000webhostapp.com/";
//    private static final String URL = "http://192.168.43.32/";
//    private static final String URL = "http://192.168.42.212/esloin/";
    private static final String URL = "http://192.168.1.7/esloin/";

    SharedPreferences mData;

    public float kelembaban1;
    public float kelembaban2;
    public float kelembaban3;
    public float kelembaban4;
    public float kelembaban5;
    public float kelembaban6;
    public float kelembaban7;
    public float kelembaban8;
    public float kelembaban9;
    public float kelembaban10;
    public float kelembaban11;
    public float kelembaban12;

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
//        graph2 = (GraphView) view.findViewById(R.id.graph2);

        // set manual X bounds
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(12);

        // set manual Y bounds
        graph1.getViewport().setYAxisBoundsManual(true);
        graph1.getViewport().setMinY(0);
        graph1.getViewport().setMaxY(100);

        handler.post(runnable);

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

                    Integer check = results.get(0).getUltrasonik();


                    txtJarak.setText("Ketinggian sampah : " + results.get(0).getUltrasonik() + " cm");
                    txtKelembaban.setText(results.get(0).getKelembaban() + " rh");
                    txtSuhu.setText(results.get(0).getSuhu() + " °C");

                    SharedPreferences.Editor editor = mData.edit();
                    editor.putInt("statusSampah", check);
                    editor.apply();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

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
                graph1.removeSeries(series1);
                String value = response.body().getValue();
                if (value.equals("1")) {
                    results = response.body().getResult();

//                    float kelembaban1;
//                    float kelembaban2;
//                    float kelembaban3;
//                    float kelembaban4;
//                    float kelembaban5;
//                    float kelembaban6;
//                    float kelembaban7;
//                    float kelembaban8;
//                    float kelembaban9;
//                    float kelembaban10;
//                    float kelembaban11;
//                    float kelembaban12;
                    //dht = results.get(0).getDht();

                    if(results.indexOf(11) >= results.size()){
                        // ada isinya
                        kelembaban1 = results.get(11).getKelembaban();
                    }

                    kelembaban1 = results.get(11).getKelembaban();
                    kelembaban2 = results.get(10).getKelembaban();
                    kelembaban3 = results.get(9).getKelembaban();
                    kelembaban4 = results.get(8).getKelembaban();
                    kelembaban5 = results.get(7).getKelembaban();
                    kelembaban6 = results.get(6).getKelembaban();
                    kelembaban7 = results.get(5).getKelembaban();
                    kelembaban8 = results.get(4).getKelembaban();
                    kelembaban9 = results.get(3).getKelembaban();
                    kelembaban10 = results.get(2).getKelembaban();
                    kelembaban11 = results.get(1).getKelembaban();
                    kelembaban12 = results.get(0).getKelembaban();

                    series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                            new DataPoint(0, 0),
                            new DataPoint(1, kelembaban1),
                            new DataPoint(2, kelembaban2),
                            new DataPoint(3, kelembaban3),
                            new DataPoint(4, kelembaban4),
                            new DataPoint(5, kelembaban5),
                            new DataPoint(6, kelembaban6),
                            new DataPoint(7, kelembaban7),
                            new DataPoint(8, kelembaban8),
                            new DataPoint(9, kelembaban9),
                            new DataPoint(10, kelembaban10),
                            new DataPoint(11, kelembaban11),
                            new DataPoint(12, kelembaban12)
                    });

                    graph1.addSeries(series1);
                    series1.setColor(Color.GREEN);

                }
                else {
//                    series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                            new DataPoint(0, 0),
//                            new DataPoint(1, 0),
//                            new DataPoint(2, 0),
//                            new DataPoint(3, 0),
//                            new DataPoint(4, 0)
//                    });
//
//                    graph1.addSeries(series1);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

            }
        });
    }

    private void loadData2(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Value> call = api.view();

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, retrofit2.Response<Value> response) {
                graph1.removeSeries(series2);
                String value2 = response.body().getValue();
                if (value2.equals("1")) {
                    results = response.body().getResult();
                    float suhu1;
                    float suhu2;
                    float suhu3;
                    float suhu4;
                    float suhu5;
                    float suhu6;
                    float suhu7;
                    float suhu8;
                    float suhu9;
                    float suhu10;
                    float suhu11;
                    float suhu12;

                    //dht = results.get(0).getDht();
                    suhu1 = results.get(11).getSuhu();
                    suhu2 = results.get(10).getSuhu();
                    suhu3 = results.get(9).getSuhu();
                    suhu4 = results.get(8).getSuhu();
                    suhu5 = results.get(7).getSuhu();
                    suhu6 = results.get(6).getSuhu();
                    suhu7 = results.get(5).getSuhu();
                    suhu8 = results.get(4).getSuhu();
                    suhu9 = results.get(3).getSuhu();
                    suhu10 = results.get(2).getSuhu();
                    suhu11 = results.get(1).getSuhu();
                    suhu12 = results.get(0).getSuhu();

                    series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                            new DataPoint(0, 0),
                            new DataPoint(1, suhu1),
                            new DataPoint(2, suhu2),
                            new DataPoint(3, suhu3),
                            new DataPoint(4, suhu4),
                            new DataPoint(5, suhu5),
                            new DataPoint(6, suhu6),
                            new DataPoint(7, suhu7),
                            new DataPoint(8, suhu8),
                            new DataPoint(9, suhu9),
                            new DataPoint(10, suhu10),
                            new DataPoint(11, suhu11),
                            new DataPoint(12, suhu12)
                    });

                    graph1.addSeries(series2);
                    series2.setColor(Color.RED);

                }
                else {
//                    series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                            new DataPoint(0, 0),
//                            new DataPoint(1, 0),
//                            new DataPoint(2, 0),
//                            new DataPoint(3, 0),
//                            new DataPoint(4, 0)
//                    });
//
//                    graph2.addSeries(series2);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

            }
        });
    }

    private void status(){

//        SharedPreferences mData = getActivity().getSharedPreferences("SAVE_DATA", Context.MODE_PRIVATE);
        int status = mData.getInt("statusSampah", -1);

        if(status <= 10) {
            txtStatus.setText("Status Sampah : Penuh");
        }
        else {
            txtStatus.setText("Status Sampah : Belum Penuh");
        }
    }

    // Define the code block to be executed
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Insert custom code here

            loadData();
            loadData1();
            loadData2();
            status();


            // Repeat every 2 seconds
            handler.postDelayed(runnable, 1000);
        }
    };

}
