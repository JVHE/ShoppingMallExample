package com.example.jshop;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Roulette extends AppCompatActivity {

    Button start;
    ImageView roulette;
    boolean is_started, is_stopped;
    //TextView time_limit;
    int mileage;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette);

        start = (Button) findViewById(R.id.start);
        roulette = (ImageView) findViewById(R.id.roulette);
        //   time_limit = (TextView) findViewById(R.id.time_limit);

        is_started = false;
        is_stopped = false;

        final Handler handler = new Handler();
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1000; i >= 0 && !is_stopped; i--) {
                    int time;
                    time = i;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final int finalI = i;
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            time_limit.setText("남은 시간: " + finalI / 100 + "." + finalI % 100 + "초");
//                        }
//                    });
                }
                if (is_started && !is_stopped) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            start.performClick();
                        }
                    });
                }
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        time_limit.setText("남은 시간: 0.00초");
//                    }
//                });
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!is_started) {
                    is_started = true;
                    RotateAnimation anim = new RotateAnimation(0f, 30000f, roulette.getWidth() / 2, roulette.getHeight() / 2);
                    anim.setInterpolator(new LinearInterpolator());
//                    anim.setRepeatCount(Animation.INFINITE);
                    anim.setInterpolator(getApplicationContext(), android.R.anim.linear_interpolator);
//                    anim.setFillAfter(true);
                    anim.setDuration(20000);
//
//                    Toast.makeText(getApplicationContext(), "회전!", Toast.LENGTH_SHORT).show();
                    roulette.startAnimation(anim);

                    //start 텍스트를 stop로 바꿔준다.
                    start.setText("stop");
                    thread.start();

                } else if (is_started && !is_stopped) {

                    thread.interrupt();

                    is_stopped = true;
                    roulette.setAnimation(null);
                    int rand = (int) (Math.random() * 359);
                    rand = rand / 60 * 60;
                    RotateAnimation anim = new RotateAnimation(0, 1830 + rand, roulette.getWidth() / 2, roulette.getHeight() / 2);
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setFillAfter(true);
                    anim.setInterpolator(AnimationUtils.loadInterpolator(getApplicationContext(), android.R.anim.decelerate_interpolator));
                    anim.setDuration(1900 + (long) rand);

                    roulette.startAnimation(anim);

                    //stop텍스트를 종료로 바꿔주고 비활성화 시킨다.
                    start.setClickable(false);
                    start.setText("참여 완료!");

                    //각도 설정
                    int type = (int) ((rand) / 60);
                    switch (type) {
                        case 0:
                            mileage = 500;
                            break;
                        case 1:
                            mileage = 300;
                            break;
                        case 2:
                            mileage = 5000;
                            break;
                        case 3:
                            mileage = 3000;
                            break;
                        case 4:
                            mileage = 2000;
                            break;
                        case 5:
                            mileage = 1000;
                            break;
                    }

                    sp = getSharedPreferences("current_user", MODE_PRIVATE);
                    editor = sp.edit();
                    editor.putInt("mileage", mileage + sp.getInt("mileage", 0));
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "" + mileage + " 마일리지 당첨!! 총 " + sp.getInt("mileage", 0) + "점 보유!!", Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

}
