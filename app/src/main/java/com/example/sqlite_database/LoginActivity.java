package com.example.sqlite_database;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlite_database.helper.DBHelper_Item;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etUsername, etPassword;
    TextView Byspass;
    String Username = "root", Password = "admin";
    DBHelper_Item MyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyDB = new DBHelper_Item(this);
        Cursor res = MyDB.LihatData();
        if(res.moveToNext()){
            finish();
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        }
        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        Byspass = findViewById(R.id.byspass);
        Byspass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("root");
                etPassword.setText("admin");
            }
        });

    }
    @SuppressLint("SetTextI18n")
    private void Login(){
        String User = etUsername.getText().toString();
        String Pass = etPassword.getText().toString();
        if(User.equals("") || Pass.equals("")){
            Toast.makeText(this, "Form Masih Kosong!", Toast.LENGTH_SHORT).show();
        }else {
            if(!User.equals(Username) || !Pass.equals(Password)){
                Toast.makeText(this, "Username atau Password Salah!", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Berhasil Login!", Toast.LENGTH_SHORT).show();
                MyDB.SimpanData(User, Pass);
                finish();
                Intent intent = new Intent(this, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        }
    }
}