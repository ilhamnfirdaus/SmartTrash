package com.ilham.smarttrash;

import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String URL = "http://192.168.43.32/";
//    private static final String URL = "http://192.168.42.212/esloin/";

    SharedPreferences mData;

    TextView txtStatus;
    TextView txtJarak;
    TextView txtKelembaban;
    TextView txtSuhu;

    GraphView graph1;
    GraphView graph2;
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
        graph2 = (GraphView) view.findViewById(R.id.graph2);

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

                    float kelembaban1;
                    float kelembaban2;
                    float kelembaban3;
                    float kelembaban4;
                    float kelembaban5;
                    //dht = results.get(0).getDht();
                    kelembaban1 = results.get(4).getKelembaban();
                    kelembaban2 = results.get(3).getKelembaban();
                    kelembaban3 = results.get(2).getKelembaban();
                    kelembaban4 = results.get(1).getKelembaban();
                    kelembaban5 = results.get(0).getKelembaban();

                    series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                            new DataPoint(0, kelembaban1),
                            new DataPoint(1, kelembaban2),
                            new DataPoint(2, kelembaban3),
                            new DataPoint(3, kelembaban4),
                            new DataPoint(4, kelembaban5)
                    });

                    graph1.addSeries(series1);

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
                graph2.removeSeries(series2);
                String value2 = response.body().getValue();
                if (value2.equals("1")) {
                    results = response.body().getResult();
                    float suhu1;
                    float suhu2;
                    float suhu3;
                    float suhu4;
                    float suhu5;
                    //dht = results.get(0).getDht();
                    suhu1 = results.get(4).getSuhu();
                    suhu2 = results.get(3).getSuhu();
                    suhu3 = results.get(2).getSuhu();
                    suhu4 = results.get(1).getSuhu();
                    suhu5 = results.get(0).getSuhu();

                    series2 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                            new DataPoint(0, suhu1),
                            new DataPoint(1, suhu2),
                            new DataPoint(2, suhu3),
                            new DataPoint(3, suhu4),
                            new DataPoint(4, suhu5)
                    });

                    graph2.addSeries(series2);

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
