package com.ilham.smarttrash;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ilham.smarttrash.modelSensortes.Value;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ControlFragment extends Fragment {

//    private String URL = "https://smarttrash.000webhostapp.com/";
    public static final String URL = "http://192.168.43.32/";
//    private static final String URL = "http://192.168.42.212/esloin/";

    SharedPreferences mData;

    private Button btnPenetes;
    private Button btnPengolah;

    private TextView txtKoneksi;

    private TextView txtOnOff;
    private TextView txtOnOff2;
    private boolean isOn;
    private boolean isOn2;

    private ProgressBar progressBar;
    private TextView persentase;
    private int Value;

    private ProgressBar progressBar2;
    private TextView persentase2;
    private int Value2;

    private Handler handler1 = new Handler();

    private final View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnPengolah:
                    tmblPengolah();
                    break;
                case R.id.btnPenetes:
                    tmblPenetes();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);

        mData = getActivity().getSharedPreferences("SAVE_DATA", Context.MODE_PRIVATE);

        txtKoneksi = (TextView) view.findViewById(R.id.txtKoneksi);

        btnPengolah = (Button) view.findViewById(R.id.btnPengolah);
        txtOnOff = (TextView) view.findViewById(R.id.txtOnOff);

        btnPenetes = (Button) view.findViewById(R.id.btnPenetes);
        txtOnOff2 = (TextView) view.findViewById(R.id.txtOnOff2);

        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        persentase = (TextView) view.findViewById(R.id.persentase);
        progressBar.setProgress(0); //Set Progress Dimulai Dari O

        progressBar2 = (ProgressBar) view.findViewById(R.id.progress2);
        persentase2 = (TextView) view.findViewById(R.id.persentase2);
        progressBar2.setProgress(0); //Set Progress Dimulai Dari O



        view.findViewById(R.id.btnPengolah).setOnClickListener(mListener);
        view.findViewById(R.id.btnPenetes).setOnClickListener(mListener);

        handler1.post(runnable1);

//        btnPenetes.setSelected(false);
        btnPenetes.setEnabled(false);
//        btnPengolah.setSelected(false);
        btnPengolah.setEnabled(false);

        return view;
    }

    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            checkSavedPreference();

            handler1.postDelayed(runnable1, 1000);
        }
    };

    private void checkSavedPreference() {

        String isOn = mData.getString("isOn", "");

        if(!isOn.isEmpty()){
            if(isOn.equals("true") ){
                btnPenetes.setEnabled(true);
                btnPengolah.setEnabled(true);
            }
            else if(isOn.equals("false")){
                btnPenetes.setEnabled(false);
                btnPengolah.setEnabled(false);
            }
        }

    }

    private void tmblPengolah() {
        onLoad1();
    }

    private void tmblPenetes() {
        onLoad3();
    }

    private void onTrue1(){
        Value = 0;
        isOn = true;
        txtOnOff.setText("On");
        btnPengolah.setSelected(true);
        btnPengolah.setEnabled(false);

        if (isOn == true)
        {
            // Handler untuk Updating data pada latar belakang
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // Menampung semua data yang ingin diproses oleh thread
                    persentase.setText(String.valueOf(Value)+"%");
                    if(Value == progressBar.getMax()){
//                        Toast.makeText(getActivity(), "Progress Completed", Toast.LENGTH_SHORT).show();
                        isOn = false;
                        txtOnOff.setText("Off");
                        btnPengolah.setEnabled(true);
                        btnPengolah.setSelected(false);
                        Value = 0;
                        onLoad2();
                    }
                    Value++;
                }
            };

            // Thread untuk updating progress pada ProgressBar di Latar Belakang
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        for(int w=0; w<=progressBar.getMax(); w++){
                            progressBar.setProgress(w); // Memasukan Value pada ProgressBar
                            // Mengirim pesan dari handler, untuk diproses didalam thread
                            handler.sendMessage(handler.obtainMessage());
                            Thread.sleep(100); // Waktu Pending 100ms/0.1 detik
                        }
                    }catch(InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    private void onTrue2(){
        Value2 = 0;
        isOn2 = true;
        txtOnOff2.setText("On");
        btnPenetes.setSelected(true);
        btnPenetes.setEnabled(false);

        if (isOn2 == true) {
            // Handler untuk Updating data pada latar belakang
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    // Menampung semua data yang ingin diproses oleh thread
                    persentase2.setText(String.valueOf(Value2) + "%");
                    if (Value2 == progressBar2.getMax()) {
//                        Toast.makeText(getActivity(), "Progress Completed", Toast.LENGTH_SHORT).show();
                        isOn2 = false;
                        txtOnOff2.setText("Off");
                        btnPenetes.setEnabled(true);
                        btnPenetes.setSelected(false);
                        onLoad4();

                        //shared

                    }
                    Value2++;

                }
            };

            // Thread untuk updating progress pada ProgressBar di Latar Belakang
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int w = 0; w <= progressBar2.getMax(); w++) {
                            progressBar2.setProgress(w); // Memasukan Value pada ProgressBar
                            // Mengirim pesan dari handler, untuk diproses didalam thread
                            handler.sendMessage(handler.obtainMessage());
                            Thread.sleep(50); // Waktu Pending 100ms/0.1 detik
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();



        }
    }

    private void onLoad1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);

        String id = "2";
        String nilai = "1";
        String waktu_berakhir = "-";
        Call<com.ilham.smarttrash.modelSensortes.Value> call = api.ubah(id, nilai, waktu_berakhir);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if (value.equals("1")) {
                    txtKoneksi.setText("Status : Berhasil Terkoneksi");
                    onTrue1();
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                t.printStackTrace();
                txtKoneksi.setText("Status : Jaringan Error");
//                Toast.makeText(getActivity(), "Jaringan Error!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void onLoad2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);

        String id = "2";
        String nilai = "0";
        String waktu_berakhir = "-";
        Call<Value> call = api.ubah(id, nilai, waktu_berakhir);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if (value.equals("1")) {

//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                t.printStackTrace();
//                Toast.makeText(getActivity(), "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onLoad3() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);

        String id = "1";
        String nilai = "1";
        String waktu_berakhir = "-";
        Call<Value> call = api.ubah(id, nilai, waktu_berakhir);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if (value.equals("1")) {
                    onTrue2();
                    txtKoneksi.setText("Status : Berhasil Terkoneksi");
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                t.printStackTrace();
                txtKoneksi.setText("Status : Jaringan Error");
//                Toast.makeText(getActivity(), "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onLoad4() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RegisterAPI api = retrofit.create(RegisterAPI.class);

        String id = "1";
        String nilai = "0";
        String waktu_berakhir = "-";
        Call<Value> call = api.ubah(id, nilai, waktu_berakhir);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                if (value.equals("1")) {
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                t.printStackTrace();
//                Toast.makeText(getActivity(), "Jaringan Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
