package com.example.jshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    TextInputEditText new_id, new_pw, new_name, new_zip_code, new_addr, new_phone;
    Button already_exist, find_addr, register;
    AlertDialog.Builder alert_load;
    AlertDialog alert;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Intent intent;

    private static final int MANAGER = 0;
    private static final int USER = 1;


    //이메일 정규식 확인 함수
    private boolean checkEmail(String email) {
        String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        new_id = (TextInputEditText) findViewById(R.id.new_id);
        new_pw = (TextInputEditText) findViewById(R.id.new_pw);
        new_name = (TextInputEditText) findViewById(R.id.new_name);
        new_zip_code = (TextInputEditText) findViewById(R.id.new_zip_code);
        new_addr = (TextInputEditText) findViewById(R.id.new_addr);
        new_phone = (TextInputEditText) findViewById(R.id.new_phone);
        already_exist = (Button) findViewById(R.id.already_exist);
        find_addr = (Button) findViewById(R.id.find_addr);
        register = (Button) findViewById(R.id.register);

        //버튼에 밑줄 보이기
        find_addr.setPaintFlags(find_addr.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //등록하기
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //유저 리스트 생성
                sp = getSharedPreferences("user_list", MODE_PRIVATE);
                editor = sp.edit();
                editor.putString(new_id.getText().toString(), new_pw.getText().toString());
                editor.commit();
                //유저 정보 생성
                sp = getSharedPreferences("user_info_" + new_id.getText().toString(), MODE_PRIVATE);
                editor = sp.edit();
                editor.putString("id", new_id.getText().toString());
                editor.putString("pw", new_pw.getText().toString());
                editor.putString("name", new_name.getText().toString());
                editor.putInt("zip_code", (new_zip_code.getText().toString().equals("")) ? 0 : Integer.parseInt(new_zip_code.getText().toString()));
                editor.putString("addr", new_addr.getText().toString());
                editor.putInt("phone", (new_phone.getText().toString().equals("")) ? 0 : Integer.parseInt(new_phone.getText().toString()));
                editor.putInt("authority", MANAGER);
                editor.commit();

                intent = new Intent();
                intent.putExtra("id", new_id.getText().toString());
                intent.putExtra("pw", new_pw.getText().toString());
                setResult(RESULT_OK, intent);
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(), "가입 완료!", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
                finish();
            }
        });

        //이메일 올바른지 체크. 보완 필요
        new_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) { /*포커스에서 떠날때만 확인*/
                if (b == false && new_id.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "아이디 에러: 필수 입력창입니다.", Toast.LENGTH_SHORT).show();
                } else if (b == false && !checkEmail(new_id.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "에러: 올바르지 않은 양식입니다..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        alert_load = new AlertDialog.Builder(SignUp.this);
        alert_load.setTitle("작성중이던 정보가 있습니다. 불러오시겠습니까?");
        alert_load.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;

            }
        });
        //아니오일 경우 작성중이던 내용 모두 지움
        alert_load.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new_id.setText("");
                new_pw.setText("");
                new_name.setText("");
                new_zip_code.setText("");
                new_phone.setText("");
            }
        });
        alert = alert_load.create();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //가입 양식에 작성중이던 정보가 있을 경우 알림창을 이용하여 계속 쓸지 여부를 묻는다.
        if (!new_id.getText().toString().equals("") || !new_pw.getText().toString().equals("") || !new_name.getText().toString().equals("") || !new_zip_code.getText().toString().equals("") || !new_phone.getText().toString().equals("")) {

            alert.show();
//            Thread thread = new Thread(new Runnable() {
//                Handler handler = new Handler();
//                @Override
//                public void run() {
//                    for(int i=0;i<5;i++) {
//                        final int chk = i;
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                alert.setTitle(""+chk);
//                            }
//                        });
//                    }
//                    return;
//                }
//            });
//            thread.start();
        }


    }

}
