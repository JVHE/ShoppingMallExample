package com.example.jshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 1500);// 1.5 초


    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.loadfadein, R.anim.loadfadeout);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp;
        //------------------------------------------------------------------------------------------------------------------------------------------------------
        //어플리케이션을 꺼도 SP로 로그인이 유지되기 때문에 어플리케이션을 다시 킬 때 로그인이 되어있다는 점을 알려주기 위하여 토스트 메시지를 띄워보고 싶다.
        sp = getSharedPreferences("current_user", MODE_PRIVATE);
        //로그인한 상태일 경우
        if (!sp.getString("current_user", "비회원").equals("비회원")) {
            Toast.makeText(getApplicationContext(), "환영합니다! " + sp.getString("current_user", "비회원") + "님!", Toast.LENGTH_SHORT).show();
        }
        //------------------------------------------------------------------------------------------------------------------------------------------------------

    }
}
