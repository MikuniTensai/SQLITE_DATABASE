package com.example.sqlite_database;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.sqlite_database.adapter.CustomCursorAdapter;
import com.example.sqlite_database.adapter.ViewPagerAdapter;
import com.example.sqlite_database.helper.DBHelper_Account;
import com.example.sqlite_database.helper.DBHelper_Item;
import com.example.sqlite_database.helper.InfoActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    DBHelper_Account helper;
    DBHelper_Item MyDB;

    View dialogView;
    TextView Tv_Harga, Tv_Nama, Tv_Selengkapnya;
    LayoutInflater inflater;
    ImageView im_Foto;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] img = {R.drawable.view1, R.drawable.view2, R.drawable.view3, R.drawable.view4, R.drawable.view5, R.drawable.view6, R.drawable.view7, R.drawable.view8};
    private ArrayList<Integer> ImgArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_contact_person);
        toolbar.setOverflowIcon(drawable);

        helper = new DBHelper_Account(this);
        listView = findViewById(R.id.list_data);
        listView.setOnItemClickListener(this);

        init();
        MyDB = new DBHelper_Item(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;
        if (id == R.id.action_settings) {
            intent = new Intent(this, LoginActivity.class);
        } else if (id == R.id.action_info) {
            intent = new Intent(this, InfoActivity.class);
        } else {
            intent = new Intent(this, AdminActivity.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long x) {
        TextView getId = view.findViewById(R.id.listID);
        final long id = Long.parseLong(getId.getText().toString());
        final Cursor cur = helper.oneData(id);
        cur.moveToFirst();

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(cur.getString(cur.getColumnIndex(DBHelper_Account.row_nama)));

        String[] options = {"Lihat Data", "Contact"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        final AlertDialog.Builder viewData = new AlertDialog.Builder(MainActivity.this);
                        inflater = getLayoutInflater();
                        dialogView = inflater.inflate(R.layout.view_data, null);
                        viewData.setView(dialogView);
                        viewData.setTitle(cur.getString(cur.getColumnIndex(DBHelper_Account.row_nama)));

                        Tv_Harga = (TextView)dialogView.findViewById(R.id.tv_Harga);
                        Tv_Nama = (TextView)dialogView.findViewById(R.id.tv_Nama);
                        Tv_Selengkapnya = (TextView)dialogView.findViewById(R.id.tv_Selengkapnya);
                        im_Foto = (ImageView) dialogView.findViewById(R.id.iv_Foto);

                        Tv_Harga.setText("Harga: " + cur.getString(cur.getColumnIndex(DBHelper_Account.row_harga)));
                        Tv_Nama.setText("Nama: " + cur.getString(cur.getColumnIndex(DBHelper_Account.row_nama)));
                        Tv_Selengkapnya.setText("Spesifikasi: " + cur.getString(cur.getColumnIndex(DBHelper_Account.row_selengkapnya)));
                        Glide.with(im_Foto.getContext())
                                .load(cur.getString(cur.getColumnIndex(DBHelper_Account.row_foto)))
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }
                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                })
                                .apply(new RequestOptions().transform(new RoundedCorners(50)).error(R.drawable.ic_add).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                                .into(im_Foto);

                        viewData.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        viewData.show();
                }
                switch (which){
                    case 1:
                        Intent infodata = new Intent(MainActivity.this, InfoActivity.class);
                        startActivity(infodata);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setListView() {
        Cursor cursor = helper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        listView.setAdapter(customCursorAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }

    private void init() {
        for (int i = 0; i < img.length; i++)
            ImgArray.add(img[i]);

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new ViewPagerAdapter(MainActivity.this, ImgArray));
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == img.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
    }
}
