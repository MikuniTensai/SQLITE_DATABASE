package com.example.sqlite_database;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.sqlite_database.helper.DBHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    DBHelper helper;
    EditText TxNomor, TxNama, TxTempatLahir, TxTanggal, TxAlamat;
    Spinner SpJK;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    CircularImageView imageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxNomor = (EditText)findViewById(R.id.txNomor_Add);
        TxNama = (EditText)findViewById(R.id.txNama_Add);
        TxTempatLahir = (EditText)findViewById(R.id.txTempatLahir_Add);
        TxTanggal = (EditText)findViewById(R.id.txTglLahir_Add);
        TxAlamat = (EditText)findViewById(R.id.txAlamat_Add);
        SpJK = (Spinner)findViewById(R.id.spJK_Add);
        imageView = (CircularImageView)findViewById(R.id.image_profile);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TxTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(AddActivity.this);
            }
        });

    }

    private void showDateDialog(){
        Calendar calendar = Calendar.getInstance();

        datePickerDialog =  new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                    TxTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_add:
                String nomor = TxNomor.getText().toString().trim();
                String nama = TxNama.getText().toString().trim();
                String tempatLahir = TxTempatLahir.getText().toString().trim();
                String tanggal = TxTanggal.getText().toString().trim();
                String alamat = TxAlamat.getText().toString().trim();
                String jk = SpJK.getSelectedItem().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_nomor, nomor);
                values.put(DBHelper.row_nama, nama);
                values.put(DBHelper.row_tempatLahir, tempatLahir);
                values.put(DBHelper.row_tglLahir, tanggal);
                values.put(DBHelper.row_alamat, alamat);
                values.put(DBHelper.row_jk, jk);
                values.put(DBHelper.row_foto, String.valueOf(uri));

                if (nomor.equals("") || nama.equals("") || tempatLahir.equals("") ||tanggal.equals("") || alamat.equals("")){
                    Toast.makeText(AddActivity.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else {
                    helper.insertData(values);
                    Toast.makeText(AddActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                        , 0);
            } else {
                startCrop(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(result.getUri());
                uri = result.getUri();
            }
        }
    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
        uri = imageuri;
    }
}
