package com.example.jshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingBag extends AppCompatActivity {

    ListView item_list;
    ArrayList<Item> list;
    ShoppingBagAdapter adapter;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String user_id;

    TextView total_price, stock_count;

    Button order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_bag);

        total_price = (TextView) findViewById(R.id.total_price);
        stock_count = (TextView) findViewById(R.id.stock_count);
        item_list = (ListView) findViewById(R.id.item_list);

        list = new ArrayList<Item>();

        //유저 아이디 불러오기
        sp = getSharedPreferences("current_user", MODE_PRIVATE);
        user_id = sp.getString("current_user", "비회원");


        //listview에 sp에 저장된 아이템 불러오기.
        int cnt = 1;
        sp = getSharedPreferences(user_id + "_bag_0", MODE_PRIVATE);
        while (!sp.getString("name", "").equals("")) {
            list.add(new Item(sp.getString("name", ""), sp.getString("contents", ""), sp.getString("category", ""), sp.getString("image", ""), sp.getString("size", ""),
                    sp.getString("color", ""), sp.getInt("price", 0), sp.getInt("price_before", 0), sp.getInt("stock", 0), sp.getInt("type", 0)));

            sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
            cnt++;
        }

        adapter = new ShoppingBagAdapter(this, list);
        item_list.setAdapter(adapter);
//
//        //현재 총 가격 확인.
//        int int_total_price = 0;
//        for (int i = 0; i < list.size(); i++) {
//            int_total_price += list.get(i).getPrice();
//        }
//        total_price.setText("전체 가격: " + int_total_price + "원");

        order = (Button)findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LastConfirm.class));
                finish();
            }
        });

        Thread thread = new Thread(new Runnable() {
            Handler handler = new Handler();

            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            int int_total_price = 0;
                            int total_stock = 0;
                            for (int j = 0; j < list.size(); j++) {
                                int_total_price += list.get(j).getPrice() * list.get(j).getStock();
                                total_stock += list.get(j).getStock();
                            }
                            DecimalFormat formatter = new DecimalFormat("#,### 원");
                            String formatted = formatter.format(int_total_price);
                            total_price.setText(formatted);
                            stock_count.setText("" + total_stock + " 상품");
                        }
                    });
                }
            }
        });
        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();


        //sp에 저장하기 전에 초기화 시켜버리기
        int cnt = 0;

        sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
        do {
            editor = sp.edit();
            editor.clear();
            editor.commit();
            cnt++;
            sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
        } while (!sp.getString("name", "").equals(""));


        //sp로 리스트뷰에 있는 아이템 저장하기
        for (int i = 0; i < list.size(); i++) {
            sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(i), MODE_PRIVATE);
            editor = sp.edit();

            editor.putString("name", list.get(i).getName());
            editor.putString("contents", list.get(i).getContents());
            editor.putString("category", list.get(i).getCategory());
            editor.putString("image", list.get(i).getImage());
            editor.putString("size", list.get(i).getSize());
            editor.putString("color", list.get(i).getColor());
            editor.putInt("price", list.get(i).getPrice());
            editor.putInt("price_before", list.get(i).getPrice_before());
            editor.putInt("stock", list.get(i).getStock());
            editor.putInt("type", list.get(i).getType());

            editor.commit();
        }

    }
}
