package com.example.jshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button login, sign_up, show;
    TextInputEditText id, pw;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        sign_up = (Button) findViewById(R.id.sign_up);
        id = (TextInputEditText) findViewById(R.id.id);
        pw = (TextInputEditText) findViewById(R.id.pw);
        show = (Button) findViewById(R.id.show);


        if (savedInstanceState != null) {
            id.setText(savedInstanceState.getString("id", ""));
            pw.setText(savedInstanceState.getString("pw", ""));
        }

        //로그인 버튼
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아이디 공백 상황
                if (id.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                //비밀번호 공백 상황
                else if (pw.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                //아이디 비밀번호 둘다 있을 경우
                else {
                    //유저 리스트의 명단확인
                    sp = getSharedPreferences("user_list", MODE_PRIVATE);
                    //아이디와 비밀번호가 맞을 경우
                    if (sp.getString(id.getText().toString(), "").equals(pw.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();

                        //현재 로그인 유저 확인.
                        sp = getSharedPreferences("current_user", MODE_PRIVATE);
                        editor = sp.edit();
                        editor.putString("current_user", id.getText().toString());
                        editor.commit();
                        finish();
                    }
                    //아이디 또는 비밀번호가 틀릴 경우
                    else {
                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //show 버튼을 누르면 비밀번호가 보여진다.
        show.setOnClickListener(new View.OnClickListener() {
            Boolean is_clicked = false;
            @Override
            public void onClick(View view) {
                if(is_clicked) {
                    pw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    show.setText("show");
                    is_clicked = false;
                } else {
                    pw.setInputType(InputType.TYPE_CLASS_TEXT);
                    show.setText("hide");
                    is_clicked = true;
                }
            }
        });

        //회원가입 버튼
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), SignUp.class), 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            if(resultCode == RESULT_OK) {
                id.setText(data.getStringExtra("id"));
                pw.setText(data.getStringExtra("pw"));
                login.callOnClick();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("id", id.getText().toString());
        outState.putString("pw", pw.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
