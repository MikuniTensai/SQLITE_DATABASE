package com.example.sqlite_database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sqlite_database.adapter.CustomListAdapter;
import com.example.sqlite_database.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private CustomListAdapter adapter_off;
    private MyDatabase db;
    private List<Komputer> listKomputer = new ArrayList<Komputer>();

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] img = {R.drawable.ic_settings,R.drawable.ic_add, R.drawable.ic_person, R.drawable.ic_launcher_background};
    private ArrayList<Integer>  ImgArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDatabase(this);
        adapter_off = new CustomListAdapter(this, listKomputer);
        mListView = (ListView) findViewById(R.id.list_komputer);
        mListView.setAdapter(adapter_off);
        mListView.setOnItemClickListener(this);
        mListView.setClickable(true);
        ListKomputer();
        init();
        //hello
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
        Object o = mListView.getItemAtPosition(i);
        Komputer obj_itemDetails = (Komputer)o;
        String Sid = obj_itemDetails.get_id();
        String Snama = obj_itemDetails.get_nama();
        String Sharga = obj_itemDetails.get_harga();
        Intent goUpdel = new Intent(MainActivity.this, MainUpdel.class);
        goUpdel.putExtra("Iid", Sid);
        goUpdel.putExtra("Inama", Snama);
        goUpdel.putExtra("Iharga", Sharga);
        startActivity(goUpdel);
    }
    @Override
    protected void onResume() {
        super.onResume();
        ListKomputer();
    }

    public void ListKomputer(){
        listKomputer.clear();
        mListView.setAdapter(adapter_off);
        List<Komputer> contacts = db.ReadKomputer();
        for (Komputer cn : contacts) {
            Komputer judulModel = new Komputer();
            judulModel.set_id(cn.get_id());
            judulModel.set_nama(cn.get_nama());
            judulModel.set_harga(cn.get_harga());
            listKomputer.add(judulModel);
            if ((listKomputer.isEmpty()))
                Toast.makeText(MainActivity.this, "Tidak ada data",
                        Toast.LENGTH_SHORT).show();
            else {
            }
        }
    }

    public void btn_create(View view) {
        Intent a = new Intent(MainActivity.this, MainCreate.class);
        startActivity(a);
    }

    private void init (){
        for (int i=0; i<img.length; i++)
            ImgArray.add(img[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ViewPagerAdapter(MainActivity.this, ImgArray));
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == img.length){
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        //Auto start
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 1100);
    }
}
