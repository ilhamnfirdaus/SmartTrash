package com.ilham.smarttrash;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ilham.smarttrash.modelKontrol.ResponseKontrol;
import com.ilham.smarttrash.modelKontrol.ResultItem;
import com.ilham.smarttrash.modelSensortes.Value;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

//    private String URL = "https://smarttrash.000webhostapp.com/" ;
//    private static final String URL = "http://192.168.43.32/";
//    private static final String URL = "http://192.168.42.212/esloin/";
//    private static final String URL = "http://192.168.1.25/esloin/";
    private List<ResultItem> results;

//    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private Button btnMulai;
    private Button btnStop;
    private TextView txtMulai;
    private TextView txtHari;

    SharedPreferences mData ;

//    private boolean isOn;
    private Boolean isOn;

    private Handler handler1 = new Handler();
//    private Handler handler2 = new Handler();

    private final View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnMulai:
                    tmblMulai();
                    break;
                case R.id.btnStop:
                    String test = "test";
                    Log.d("test", test);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mData= getActivity().getSharedPreferences("SAVE_DATA", Context.MODE_PRIVATE);

        btnMulai = (Button) view.findViewById(R.id.btnMulai);
        btnStop = (Button) view.findViewById(R.id.btnStop);
        txtMulai = (TextView) view.findViewById(R.id.txtMulai);
        txtHari = (TextView) view.findViewById(R.id.txtHari);

        view.findViewById(R.id.btnMulai).setOnClickListener(mListener);
        view.findViewById(R.id.btnStop).setOnClickListener(mListener);

        handler1.post(runnable1);

//        isOn = false;
//        SharedPreferences.Editor editor = mData.edit();
//        editor.putString("isOn", String.valueOf(isOn));
//        editor.apply();

        checkSavedPreference();

//        tampilNotifikasi();

        return view;

        // Notif
    }

    private void checkSavedPreference() {
//        SharedPreferences mData = getActivity().getSharedPreferences("SAVE_DATA", Context.MODE_PRIVATE);
        String isOn = mData.getString("isOn", "");

        if(isOn.equals("true") ){
            txtMulai.setText("Pupuk Sedang di Proses");
            btnMulai.setSelected(true);
            btnMulai.setEnabled(false);
//            btnStop.setEnabled(true);
            handler1.post(runnable2);
        }
        else {
            txtHari.setText("Hari ke : ");
            txtMulai.setText("Proses Berhenti");
            btnMulai.setEnabled(true);
//            btnStop.setEnabled(false);
//            handler1.removeCallbacks(runnable2);

        }

    }

    private void tmblMulai(){
        onTrue1();
        waktuSekarang();
//        onLoad1();
    }

//
//    private void viewKontrol(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RegisterAPI api = retrofit.create(RegisterAPI.class);
//        Call<ResponseKontrol> call = api.viewKontrol();
//        call.enqueue(new Callback<ResponseKontrol>() {
//            @Override
//            public void onResponse(Call<ResponseKontrol> call, retrofit2.Response<ResponseKontrol> response) {
//                String value = String.valueOf(response.body().getValue());
//                if (value.equals("1")) {
//                    results = response.body().getResult();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseKontrol> call, Throwable t) {
//
//            }
//        });
//    }
//
//    private void onLoad1() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RegisterAPI api = retrofit.create(RegisterAPI.class);
//
//        String id = "3";
//        String nilai = "1";
//        String waktu_berakhir = "-";
//        Call<com.ilham.smarttrash.modelSensortes.Value> call = api.ubah(id, nilai, waktu_berakhir);
//        call.enqueue(new Callback<com.ilham.smarttrash.modelSensortes.Value>() {
//            @Override
//            public void onResponse(Call<Value> call, Response<Value> response) {
//                String value = response.body().getValue();
//                String message = response.body().getMessage();
//                if (value.equals("1")) {
//                    onTrue1();
////                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                } else {
////                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Value> call, Throwable t) {
//                t.printStackTrace();
////                Toast.makeText(getActivity(), "Jaringan Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void onLoad2(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RegisterAPI api = retrofit.create(RegisterAPI.class);
//
//        String id = "3";
//        String nilai = "0";
//        String waktu_berakhir = "-";
//        Call<Value> call = api.ubah(id, nilai, waktu_berakhir);
//        call.enqueue(new Callback<Value>() {
//            @Override
//            public void onResponse(Call<Value> call, Response<Value> response) {
//                String value = response.body().getValue();
//                String message = response.body().getMessage();
//                if (value.equals("1")) {
//
////                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                } else {
////                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Value> call, Throwable t) {
//                t.printStackTrace();
////                Toast.makeText(getActivity(), "Jaringan Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void onTrue1(){
        isOn = true;
        btnMulai.setSelected(true);
        btnMulai.setEnabled(false);
//        btnStop.setEnabled(true);
        txtHari.setText("Hari ke : 0");
        txtMulai.setText("Pupuk Sedang di Proses");

        SharedPreferences.Editor editor = mData.edit();
        editor.putString("isOn", String.valueOf(isOn));
        editor.apply();

        if (isOn == true) {
            handler1.post(runnable2);
        }
    }

   private void waktuSekarang(){
//        Calendar cal = Calendar.getInstance();
//        Date date=cal.getTime();
//        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//        String formattedDate = dateFormat.format(date);
//        Log.d("waktu = ", formattedDate);

       Date currentDate = new Date();

//       // convert date to calendar
//       Calendar cal = Calendar.getInstance();
//       cal.setTime(currentDate);
//
//       // manipulate date
//       cal.add(Calendar.YEAR, 0);
//       cal.add(Calendar.MONTH, 0);
//       cal.add(Calendar.DATE, 0); //same with c.add(Calendar.DAY_OF_MONTH, 1);
//       cal.add(Calendar.HOUR, 0);
//       cal.add(Calendar.MINUTE, 0);
//       cal.add(Calendar.SECOND, 5);
//
//       // convert calendar to date
//       Date currentDatePlusOne = cal.getTime();
//
//       SharedPreferences.Editor editor = mData.edit();
//       editor.putString("currentDatePlus", String.valueOf(currentDatePlusOne));
//       editor.apply();

       Calendar cal1 = Calendar.getInstance();
       Calendar cal2 = Calendar.getInstance();
       Calendar cal3 = Calendar.getInstance();
       Calendar cal4 = Calendar.getInstance();
       Calendar cal5 = Calendar.getInstance();
       Calendar cal6 = Calendar.getInstance();
       Calendar cal7 = Calendar.getInstance();
       Calendar cal8 = Calendar.getInstance();
       Calendar cal9 = Calendar.getInstance();
       Calendar cal10 = Calendar.getInstance();
       Calendar cal11 = Calendar.getInstance();
       Calendar cal12 = Calendar.getInstance();
       Calendar cal13 = Calendar.getInstance();
       Calendar cal14 = Calendar.getInstance();

       cal1.setTime(currentDate);
       cal2.setTime(currentDate);
       cal3.setTime(currentDate);
       cal4.setTime(currentDate);
       cal5.setTime(currentDate);
       cal6.setTime(currentDate);
       cal7.setTime(currentDate);
       cal8.setTime(currentDate);
       cal9.setTime(currentDate);
       cal10.setTime(currentDate);
       cal11.setTime(currentDate);
       cal12.setTime(currentDate);
       cal13.setTime(currentDate);
       cal14.setTime(currentDate);

       cal1.add(Calendar.DATE, 1);
       cal2.add(Calendar.DATE, 2);
       cal3.add(Calendar.DATE, 3);
       cal4.add(Calendar.DATE, 4);
       cal5.add(Calendar.DATE, 5);
       cal6.add(Calendar.DATE, 6);
       cal7.add(Calendar.DATE, 7);
       cal8.add(Calendar.DATE, 8);
       cal9.add(Calendar.DATE, 9);
       cal10.add(Calendar.DATE, 10);
       cal11.add(Calendar.DATE, 11);
       cal12.add(Calendar.DATE, 12);
       cal13.add(Calendar.DATE, 13);
       cal14.add(Calendar.DATE, 14);

       Date currentDatePlusOne1 = cal1.getTime();
       Date currentDatePlusOne2 = cal2.getTime();
       Date currentDatePlusOne3 = cal3.getTime();
       Date currentDatePlusOne4 = cal4.getTime();
       Date currentDatePlusOne5 = cal5.getTime();
       Date currentDatePlusOne6 = cal6.getTime();
       Date currentDatePlusOne7 = cal7.getTime();
       Date currentDatePlusOne8 = cal8.getTime();
       Date currentDatePlusOne9 = cal9.getTime();
       Date currentDatePlusOne10 = cal10.getTime();
       Date currentDatePlusOne11 = cal11.getTime();
       Date currentDatePlusOne12 = cal12.getTime();
       Date currentDatePlusOne13 = cal13.getTime();
       Date currentDatePlusOne14 = cal14.getTime();

       String formattedCurrentDatePluseOne1 = dateFormat.format(currentDatePlusOne1);
       String formattedCurrentDatePluseOne2 = dateFormat.format(currentDatePlusOne2);
       String formattedCurrentDatePluseOne3 = dateFormat.format(currentDatePlusOne3);
       String formattedCurrentDatePluseOne4 = dateFormat.format(currentDatePlusOne4);
       String formattedCurrentDatePluseOne5 = dateFormat.format(currentDatePlusOne5);
       String formattedCurrentDatePluseOne6 = dateFormat.format(currentDatePlusOne6);
       String formattedCurrentDatePluseOne7 = dateFormat.format(currentDatePlusOne7);
       String formattedCurrentDatePluseOne8 = dateFormat.format(currentDatePlusOne8);
       String formattedCurrentDatePluseOne9 = dateFormat.format(currentDatePlusOne9);
       String formattedCurrentDatePluseOne10 = dateFormat.format(currentDatePlusOne10);
       String formattedCurrentDatePluseOne11 = dateFormat.format(currentDatePlusOne11);
       String formattedCurrentDatePluseOne12 = dateFormat.format(currentDatePlusOne12);
       String formattedCurrentDatePluseOne13 = dateFormat.format(currentDatePlusOne13);
       String formattedCurrentDatePluseOne14 = dateFormat.format(currentDatePlusOne14);

       SharedPreferences.Editor editor = mData.edit();
       editor.putString("currentDatePlus1", formattedCurrentDatePluseOne1);
       editor.putString("currentDatePlus2", formattedCurrentDatePluseOne2);
       editor.putString("currentDatePlus3", formattedCurrentDatePluseOne3);
       editor.putString("currentDatePlus4", formattedCurrentDatePluseOne4);
       editor.putString("currentDatePlus5", formattedCurrentDatePluseOne5);
       editor.putString("currentDatePlus6", formattedCurrentDatePluseOne6);
       editor.putString("currentDatePlus7", formattedCurrentDatePluseOne7);
       editor.putString("currentDatePlus8", formattedCurrentDatePluseOne8);
       editor.putString("currentDatePlus9", formattedCurrentDatePluseOne9);
       editor.putString("currentDatePlus10", formattedCurrentDatePluseOne10);
       editor.putString("currentDatePlus11", formattedCurrentDatePluseOne11);
       editor.putString("currentDatePlus12", formattedCurrentDatePluseOne12);
       editor.putString("currentDatePlus13", formattedCurrentDatePluseOne13);
       editor.putString("currentDatePlus14", formattedCurrentDatePluseOne14);
       editor.apply();

   }

    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            // Insert custom code here

            Date currentTime = new Date();
            String formattedCurrentDatePluseOne = dateFormat.format(currentTime);
//            SharedPreferences mData = getActivity().getSharedPreferences("SAVE_DATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mData.edit();
            editor.putString("currentDate", formattedCurrentDatePluseOne);
            editor.apply();

            Log.d("Saiki = ", formattedCurrentDatePluseOne);

//
//
//            if(!isOn.isEmpty()){
//                if(isOn.equals("true") ){
//                    handler2.post(runnable2);
//                }
//            }
                // Repeat every 2 seconds
                handler1.postDelayed(runnable1, 1000);
        }
    };

    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            // Insert custom code here
//            viewKontrol();
//            waktuSekarang();

            String currentDate = mData.getString("currentDate", "");

//            Log.d("Saiki = ", currentDate);

//            String currentDatePlus = mData.getString("currentDatePlus", "");
//
//            if(currentDate.equals(currentDatePlus)){
//                isOn = false;
//                btnMulai.setEnabled(true);
//                btnMulai.setSelected(false);
////                btnStop.setEnabled(false);
//                txtHari.setText("Hari ke : 14");
//                txtMulai.setText("Sudah Selesai");
//
//                SharedPreferences.Editor editor = mData.edit();
//                editor.putString("isOn", String.valueOf(isOn));
//                editor.apply();
//
//                handler1.removeCallbacks(runnable2);
//            }

            String currentDatePlus1 = mData.getString("currentDatePlus1", "");
            String currentDatePlus2 = mData.getString("currentDatePlus2", "");
            String currentDatePlus3 = mData.getString("currentDatePlus3", "");
            String currentDatePlus4 = mData.getString("currentDatePlus4", "");
            String currentDatePlus5 = mData.getString("currentDatePlus5", "");
            String currentDatePlus6 = mData.getString("currentDatePlus6", "");
            String currentDatePlus7 = mData.getString("currentDatePlus7", "");
            String currentDatePlus8 = mData.getString("currentDatePlus8", "");
            String currentDatePlus9 = mData.getString("currentDatePlus9", "");
            String currentDatePlus10 = mData.getString("currentDatePlus10", "");
            String currentDatePlus11 = mData.getString("currentDatePlus11", "");
            String currentDatePlus12 = mData.getString("currentDatePlus12", "");
            String currentDatePlus13 = mData.getString("currentDatePlus13", "");
            String currentDatePlus14 = mData.getString("currentDatePlus14", "");


            if(currentDate.equals(currentDatePlus1)){
                txtHari.setText("Hari ke : 1");

            }
            else if(currentDate.equals(currentDatePlus2)){
                txtHari.setText("Hari ke : 2");
            }
            else if(currentDate.equals(currentDatePlus3)){
                txtHari.setText("Hari ke : 3");
            }
            else if(currentDate.equals(currentDatePlus4)){
                txtHari.setText("Hari ke : 4");
            }
            else if(currentDate.equals(currentDatePlus5)){
                txtHari.setText("Hari ke : 5");
            }
            else if(currentDate.equals(currentDatePlus6)){
                txtHari.setText("Hari ke : 6");
            }
            else if(currentDate.equals(currentDatePlus7)){
                txtHari.setText("Hari ke : 7");
            }
            else if(currentDate.equals(currentDatePlus8)){
                txtHari.setText("Hari ke : 8");
            }
            else if(currentDate.equals(currentDatePlus9)){
                txtHari.setText("Hari ke : 9");
            }
            else if(currentDate.equals(currentDatePlus10)){
                txtHari.setText("Hari ke : 10");
            }
            else if(currentDate.equals(currentDatePlus11)){
                txtHari.setText("Hari ke : 11");
            }
            else if(currentDate.equals(currentDatePlus12)){
                txtHari.setText("Hari ke : 12");
            }
            else if(currentDate.equals(currentDatePlus13)){
                txtHari.setText("Hari ke : 13");
            }
            else if(currentDate.equals(currentDatePlus14)){
                isOn = false;
                btnMulai.setEnabled(true);
                btnMulai.setSelected(false);
                txtHari.setText("Hari ke : 14");
                txtMulai.setText("Sudah Selesai");

                SharedPreferences.Editor editor = mData.edit();
                editor.putString("isOn", String.valueOf(isOn));
                editor.apply();

                handler1.removeCallbacks(runnable2);
            }
            else {
                txtHari.setText("Hari ke : 0");
            }

            // Repeat every 2 seconds
            handler1.postDelayed(runnable2, 1000);
        }
    };

    private void prosesStop(){
//        isOn = false;
//        txtHari.setText("Hari ke : ");
//        txtMulai.setText("Pupuk Belum di Proses");
//        btnMulai.setSelected(false);
//        btnMulai.setEnabled(true);
//        btnStop.setEnabled(false);
//
//        SharedPreferences.Editor editor = mData.edit();
//        editor.putString("isOn", String.valueOf(isOn));
//        editor.apply();
//
//        Log.d("Bool : ", String.valueOf(isOn));
//
//        handler1.removeCallbacks(runnable2);
        String hai = "hai";
        Log.d("hai ", hai);
    }

    public static final int notifikasi = 1;

    private void tampilNotifikasi() {
        // membuat komponen notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        Notification notification;
        notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle("test")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getActivity().getResources()
                        , R.mipmap.ic_launcher))
                .setContentText("test")
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifikasi, notification);
    }
}
