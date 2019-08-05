package com.ilham.smarttrash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

//    FragmentManager fragmentManager;
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new ReportFragment();
    final Fragment fragment3 = new ControlFragment();
    final Fragment fragment4 = new AboutFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inisialisasi BottomNavigaionView
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bn_main);
        // beri listener pada saat item/menu bottomnavigation terpilih
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fl_container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fl_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fl_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fl_container, fragment1, "1").commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_menu:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.report_menu:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
                case R.id.control_menu:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.about_menu:
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
            }
            return false;
        }
    };
}
