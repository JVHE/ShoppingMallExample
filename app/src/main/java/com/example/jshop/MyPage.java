package com.example.jshop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyPage extends AppCompatActivity {

    TextView email;
    Button my_shipment, address, privacy, account_info, logout;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        email = (TextView)findViewById(R.id.email);
        my_shipment = (Button)findViewById(R.id.my_shipment);
        address = (Button)findViewById(R.id.address);
        privacy = (Button)findViewById(R.id.privacy);
        account_info = (Button)findViewById(R.id.account_info);
        logout = (Button)findViewById(R.id.logout);

        //회원 이메일 상단에 표시
        sp = getSharedPreferences("current_user",MODE_PRIVATE);
        email.setText(sp.getString("current_user",""));

        //로그아웃 버튼
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp = getSharedPreferences("current_user", MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("current_user", "비회원");
                editor.putInt("mileage", 0);
                editor.commit();
                Toast.makeText(getApplicationContext(),"로그아웃",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
