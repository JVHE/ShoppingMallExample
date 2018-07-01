package com.example.jshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Product extends AppCompatActivity implements View.OnClickListener {

    TextView price_before, price, name, contents, shopping_bag_count;
    int position;
    Intent intent, last_intent;
    Item item;
    Button edit, delete, add_to_shopping_bag, shopping_bag, add_more;
    ImageView image;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    String user_id;
    String order = "";

    private static final int LIST_EDIT = 0;
    private static final int ADD_MORE = 1;
    private static final int MANAGER = 0;
    private static final int USER = 1;

    @Override
    protected void onStart() {

        super.onStart();
        //sp에 저장되어 있는 장바구니 아이템 갯수 가져와보기.
        //장바구니 버튼의 제품 갯수 보여주는 기능. 항상 갱신이 필요하기 때문에 onStart() 내부에 집어넣는다.

        //유저 id확인
        sp = getSharedPreferences("current_user", MODE_PRIVATE);
        String user_id = sp.getString("current_user", "비회원");

        //장바구니에 들어있는 물건 갯수 세기.
        int cnt = 0;
        int total_count = 0;
        sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
        while (!sp.getString("name", "").equals("")) {
            cnt++;
            total_count += sp.getInt("stock", 0);
            sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
        }

        //user_info_아이디에서 장바구니에 들어있는 아이템 갯수를 집어넣는다.
        sp = getSharedPreferences("user_info_" + user_id, MODE_PRIVATE);
        sp.edit().putInt("shopping_bag_count", total_count).commit();
        shopping_bag_count.setText(Integer.toString(sp.getInt("shopping_bag_count", 0)));
        //shopping_bag_count.setText(Integer.toString(cnt));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        name = (TextView) findViewById(R.id.product_name);
        price = (TextView) findViewById(R.id.price);
        edit = (Button) findViewById(R.id.edit);
        delete = (Button) findViewById(R.id.delete);
        image = (ImageView) findViewById(R.id.image);
        contents = (TextView) findViewById(R.id.contents);
        add_to_shopping_bag = (Button) findViewById(R.id.add_to_shopping_bag);
        shopping_bag = (Button) findViewById(R.id.shopping_bag);
        shopping_bag_count = (TextView) findViewById(R.id.shopping_bag_count);

        add_more = (Button) findViewById(R.id.add_more);

        edit.setOnClickListener(this);
        delete.setOnClickListener(this);

//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        //장바구니 액티비티로 이동
        shopping_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopping_list = new Intent(getApplicationContext(), ShoppingBag.class);
                startActivity(shopping_list);
            }
        });

        //액티비티 활성화시킨 인텐트 받아오기
        last_intent = getIntent();
        item = (Item) last_intent.getSerializableExtra("product");
        position = last_intent.getIntExtra("position", -1);
        order = last_intent.getStringExtra("order");

        //할인 가격 빨갛게 처리
        price_before = (TextView) findViewById(R.id.before_discount);
        price_before.setPaintFlags(price_before.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        //이름 적기
        name.setText(item.getName());
        //숫자에 천단위마다 콤마 찍어주기
        DecimalFormat formatter = new DecimalFormat("#,### 원");
        String formatted = formatter.format(item.getPrice());
        price.setText(formatted);
        //이전 가격에 콤마 찍어주기
        formatted = formatter.format(item.getPrice_before());
        price_before.setText(formatted);
        price_before.setPaintFlags(price_before.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        //이미지 처리
        if (!item.getImage().equals("")) {
            byte[] decodedByteArray = Base64.decode(item.getImage(), Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            image.setImageBitmap(decodedBitmap);
        }

        //내용 적기
        contents.setText(item.getContents());


        //유저 아이디 불러오기
        sp = getSharedPreferences("current_user", MODE_PRIVATE);
        user_id = sp.getString("current_user", "비회원");




        //장바구니에 아이템 넣기
        add_to_shopping_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //sp에 저장하기 위해 위치 확인
                int cnt = 0;
                sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                while (!sp.getString("name", "").equals("")) {

                    //같은 아이템이 이미 존재할 경우
                    if (sp.getString("name", "").equals(item.getName())) {
                        editor = sp.edit();
                        editor.putInt("stock", sp.getInt("stock", 0) + 1);
                        editor.commit();

                        //유저 id확인
                        sp = getSharedPreferences("current_user", MODE_PRIVATE);
                        String user_id = sp.getString("current_user", "비회원");


                        //장바구니에 들어있는 물건 갯수 세기.
                        cnt = 0;
                        int total_count = 0;
                        sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                        while (!sp.getString("name", "").equals("")) {
                            cnt++;
                            total_count += sp.getInt("stock", 0);
                            sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                        }

                        //user_info_아이디에서 장바구니에 들어있는 아이템 갯수를 집어넣는다.
                        sp = getSharedPreferences("user_info_" + user_id, MODE_PRIVATE);
                        sp.edit().putInt("shopping_bag_count", total_count).commit();
                        shopping_bag_count.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.twinkle));
                        shopping_bag_count.setText(Integer.toString(sp.getInt("shopping_bag_count", 0)));
                        //shopping_bag_count.setText(Integer.toString(cnt));


                        Toast.makeText(getApplicationContext(), "장바구니에 같은 아이템 추가 완료!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    cnt++;
                    sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                }

                sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                editor = sp.edit();

                editor.putString("name", item.getName());
                editor.putString("contents", item.getContents());
                editor.putString("category", item.getCategory());
                editor.putString("image", item.getImage());
                editor.putString("size", item.getSize());
                editor.putString("color", item.getColor());
                editor.putInt("price", item.getPrice());
                editor.putInt("price_before", item.getPrice_before());
                //재고는 한개씩 추가
                editor.putInt("stock", 1);
                editor.putInt("type", item.getType());

                editor.commit();

                //sp에 저장되어 있는 장바구니 아이템 갯수 가져와보기.
                //장바구니 버튼의 제품 갯수 보여주는 기능. 항상 갱신이 필요하기 때문에 onStart() 내부에 집어넣는다.

                //유저 id확인
                sp = getSharedPreferences("current_user", MODE_PRIVATE);
                String user_id = sp.getString("current_user", "비회원");



                //장바구니에 들어있는 물건 갯수 세기.
                cnt = 0;
                int total_count = 0;
                sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                while (!sp.getString("name", "").equals("")) {
                    cnt++;
                    total_count += sp.getInt("stock", 0);
                    sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                }

                //user_info_아이디에서 장바구니에 들어있는 아이템 갯수를 집어넣는다.
                sp = getSharedPreferences("user_info_" + user_id, MODE_PRIVATE);
                sp.edit().putInt("shopping_bag_count", total_count).commit();
                shopping_bag_count.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.twinkle));
                shopping_bag_count.setText(Integer.toString(sp.getInt("shopping_bag_count", 0)));
                //shopping_bag_count.setText(Integer.toString(cnt));


                Toast.makeText(getApplicationContext(), "장바구니에 추가 완료!", Toast.LENGTH_SHORT).show();

            }
        });


        //유저 권한에 따라 아이템 보여주고 말고 결정.
        sp = getSharedPreferences("current_user", MODE_PRIVATE);
        String user_id = sp.getString("current_user", "비회원");

        sp = getSharedPreferences("user_info_" + user_id, MODE_PRIVATE);
        int authority = sp.getInt("authority", USER);
        if (authority != MANAGER) {
            LinearLayout layout_for_manager = (LinearLayout) findViewById(R.id.layout_for_manager);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_for_manager.getLayoutParams();
            params.width = 0;
            params.height = 0;
            layout_for_manager.setLayoutParams(params);
        }

        //한 줄에 아이템이 하나밖에 없다면 같은줄에 아이템추가 버튼 만들기
        intent = getIntent();
        if (intent.getBooleanExtra("is_single", false)) {
            add_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getApplicationContext(), AddProduct.class);
                    intent.putExtra("title", "제품 추가");
                    startActivityForResult(intent, ADD_MORE);
                }
            });
        } else {
            LinearLayout layout_for_manager = (LinearLayout) findViewById(R.id.is_single_layout);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_for_manager.getLayoutParams();
            params.width = 0;
            params.height = 0;
            layout_for_manager.setLayoutParams(params);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplicationContext(), "수정 명령 취소", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == LIST_EDIT) {
            Toast.makeText(getApplicationContext(), "수정 명령 성공", Toast.LENGTH_SHORT).show();
            Item item = (Item) data.getSerializableExtra("item");
            intent = new Intent();
            intent.putExtra("action", "edit");
            intent.putExtra("item", item);
            intent.putExtra("position", position);
            intent.putExtra("order", order);
            setResult(RESULT_CANCELED, intent);
            finish();
        } else if (requestCode == ADD_MORE) {
            Toast.makeText(getApplicationContext(), "새로운 아이템 같은줄에 추가", Toast.LENGTH_SHORT).show();
            Item item = (Item) data.getSerializableExtra("item");
            intent = new Intent();
            intent.putExtra("action", "add_more");
            intent.putExtra("item", item);
            intent.putExtra("position", position);
            setResult(RESULT_CANCELED, intent);
            finish();

        }
    }

    @Override
    public void onClick(View view) {


        //삭제 명령


        if (view.getId() == R.id.delete) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(view.getContext());
            alert_confirm.setMessage("이 제품을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("네",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 'yes'
                            intent = new Intent();
                            intent.putExtra("action", "delete");
                            intent.putExtra("position", position);
                            intent.putExtra("order", order);
                            setResult(RESULT_OK, intent);

                            finish();
                        }
                    }).setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 'no'
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }
        //편집 명령
        else if (view.getId() == R.id.edit) {
            intent = new Intent(getApplicationContext(), AddProduct.class);
            intent.putExtra("title", "제품 수정");
            intent.putExtra("edit", item);
            startActivityForResult(intent, LIST_EDIT);
        }


    }
}
