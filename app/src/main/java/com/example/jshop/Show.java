package com.example.jshop;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Show extends AppCompatActivity {

    private static final int LIST_ADD = 0;
    private static final int LIST_ITEM_VIEW = 1;
    private static final int MANAGER = 0;
    private static final int USER = 1;
    EditText word;
    Intent intent;
    TextView search, shopping_bag_count;
    ImageButton item1;
    Button find, add, shopping_bag;
    ListView item_list;
    ArrayList<ItemPair> list, searched_list;
    MyAdapter adapter, searched_adapter;
    String keyword = "";

    SharedPreferences sp;
    SharedPreferences.Editor editor;

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        add = (Button) findViewById(R.id.add);
        word = (EditText) findViewById(R.id.word);
        shopping_bag = (Button) findViewById(R.id.shopping_bag);
        shopping_bag_count = (TextView) findViewById(R.id.shopping_bag_count);
        intent = getIntent();

        search = (TextView) findViewById(R.id.search);

        //장바구니 액티비티로 이동
        shopping_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopping_list = new Intent(getApplicationContext(), ShoppingBag.class);
                startActivity(shopping_list);
            }
        });

        //검색 기능
        find = (Button) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (word.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해 주세요. 여기확인필요", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "입력 후 이동!", Toast.LENGTH_SHORT).show();
                    Intent search = new Intent(getApplicationContext(), Show.class);
                    search.putExtra("keyword", word.getText().toString());
                    startActivity(search);
                    finish();
                }
            }
        });

        item_list = (ListView) findViewById(R.id.item_list);
        list = new ArrayList<ItemPair>();

        //리스트뷰 추수삭 되기 전에 테스트로 집어넣은 것들
//        list.add(new ItemPair("test", "체크", "", "", "", "Red", 100000, 150000, 5, 1));
//        list.add(new ItemPair("test1", "체크", "", "", "", "Red", 100000, 150000, 5, 1));
//        list.add(new ItemPair("test2", "체크", "", "", "", "Blue", 100000, 150000, 5, 1));
//        list.add(new ItemPair("test3", "체크", "", "", "", "", 100000, 150000, 5, 1));
//        list.add(new ItemPair("test4", "체크", "", "", "", "Red", 100000, 150000, 5, 1));
//        list.get(1).second = new Item("test66", "체크", "", "", "", "Red", 55000, 100000, 5, 1);


        //listview에 아이템 추가하기.
        int cnt = 1;
        sp = getSharedPreferences("item_0", MODE_PRIVATE);
        editor = sp.edit();
        while (!sp.getString("name", "").equals("")) {
            list.add(new ItemPair(sp.getString("name", ""), sp.getString("contents", ""), sp.getString("category", ""), sp.getString("image", ""), sp.getString("size", ""),
                    sp.getString("color", ""), sp.getInt("price", 0), sp.getInt("price_before", 0), sp.getInt("stock", 0), sp.getInt("type", 0)));
            if (!sp.getString("name2", "").equals("")) {
                list.get(list.size() - 1).second = new Item(sp.getString("name2", ""), sp.getString("contents2", ""), sp.getString("category2", ""), sp.getString("image2", ""), sp.getString("size2", ""),
                        sp.getString("color2", ""), sp.getInt("price2", 0), sp.getInt("price_before2", 0), sp.getInt("stock2", 0), sp.getInt("type2", 0));
            }

            sp = getSharedPreferences("item_" + Integer.toString(cnt), MODE_PRIVATE);
            editor = sp.edit();
            cnt++;
        }

//
//
//        //listview에 아이템 추가하기. 이렇게 추가하니까 빈 element 하나가 추가되는 문제가 있다.
//        int cnt = 0;
//        do {
//            sp = getSharedPreferences("item_" + Integer.toString(cnt), MODE_PRIVATE);
//            editor = sp.edit();
//            list.add(new ItemPair(sp.getString("name", ""), sp.getString("contents", ""), sp.getString("category", ""), sp.getString("image", ""), sp.getString("size", ""),
//                    sp.getString("color", ""), sp.getInt("price", 0), sp.getInt("price_before", 0), sp.getInt("stock", 0), sp.getInt("type", 0)));
//            if (!sp.getString("name2", "").equals("")) {
//                list.get(list.size() - 1).second = new Item(sp.getString("name2", ""), sp.getString("contents2", ""), sp.getString("category2", ""), sp.getString("image2", ""), sp.getString("size2", ""),
//                        sp.getString("color2", ""), sp.getInt("price2", 0), sp.getInt("price_before2", 0), sp.getInt("stock2", 0), sp.getInt("type2", 0));
//            }
//            cnt++;
//        } while (!sp.getString("name", "").equals(""));

        adapter = new MyAdapter(this, list);
        item_list.setAdapter(adapter);

        //type 키워드 받아오기
        if (intent.getStringExtra("type") != null) {
            keyword = intent.getStringExtra("type");
            search.setText(keyword);
        }

        //검색이 되었다면 검색 키워드 리스트뷰만 보여주기
        else if (intent.getStringExtra("keyword") != null) {
            //검색 단어 위에 보여주기
            keyword = intent.getStringExtra("keyword");
            search.setText(keyword);

            searched_list = new ArrayList<ItemPair>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getFirst().getName().contains(keyword)) {
                    searched_list.add(new ItemPair(list.get(i).getFirst()));
                }
                if (list.get(i).getSecond() != null) {
                    if (list.get(i).getSecond().getName().contains(keyword)) {
                        searched_list.add(new ItemPair(list.get(i).getSecond()));
                    }
                }
            }
            searched_adapter = new MyAdapter(this, searched_list);
            item_list.setAdapter(searched_adapter);

        }

        else if (intent.getStringExtra("category_item") != null) {
            //아이템 위에 보여주기
            keyword = intent.getStringExtra("item");
            search.setText(keyword);


            String search_word = intent.getStringExtra("category_item");

            searched_list = new ArrayList<ItemPair>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getFirst().getCategory().equals(search_word)) {
                    searched_list.add(new ItemPair(list.get(i).getFirst()));
                }
                if (list.get(i).getSecond() != null) {
                    if (list.get(i).getSecond().getCategory().equals(search_word)) {
                        searched_list.add(new ItemPair(list.get(i).getSecond()));
                    }
                }
            }
            searched_adapter = new MyAdapter(this, searched_list);
            item_list.setAdapter(searched_adapter);

        }

        //유저 권한에 따라 아이템 보여주고 말고 결정.
        sp = getSharedPreferences("current_user", MODE_PRIVATE);
        String user_id = sp.getString("current_user", "비회원");

        sp = getSharedPreferences("user_info_" + user_id, MODE_PRIVATE);
        int authority = sp.getInt("authority", USER);
        if (authority == MANAGER) {

            //리스트뷰 아이템 추가
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(getApplicationContext(), AddProduct.class);
                    intent.putExtra("title", "제품 추가");
                    startActivityForResult(intent, LIST_ADD);
                }
            });
        } else {
            LinearLayout layout_for_manager = (LinearLayout) findViewById(R.id.layout_for_manager);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_for_manager.getLayoutParams();
            params.width = 0;
            params.height = 0;
            layout_for_manager.setLayoutParams(params);
        }


    }


    @Override
    protected void onStop() {

//        SharedPreferences sp;
//        SharedPreferences.Editor editor;


        //sp에 저장하기 전에 초기화 시켜버리기
        int cnt = 0;
        sp = getSharedPreferences("item_" + Integer.toString(cnt), MODE_PRIVATE);
        do {
            editor = sp.edit();
            editor.clear();
            editor.commit();
            cnt++;
            sp = getSharedPreferences("item_" + Integer.toString(cnt), MODE_PRIVATE);
        } while (!sp.getString("name", "").equals(""));

        for (int i = 0; i < list.size(); i++) {
            sp = getSharedPreferences("item_" + Integer.toString(i), MODE_PRIVATE);
            editor = sp.edit();

            if (list.get(i).getFirst() != null) {
                editor.putString("name", list.get(i).getFirst().getName());
                editor.putString("contents", list.get(i).getFirst().getContents());
                editor.putString("category", list.get(i).getFirst().getCategory());
                editor.putString("image", list.get(i).getFirst().getImage());
                editor.putString("size", list.get(i).getFirst().getSize());
                editor.putString("color", list.get(i).getFirst().getColor());
                editor.putInt("price", list.get(i).getFirst().getPrice());
                editor.putInt("price_before", list.get(i).getFirst().getPrice_before());
                editor.putInt("stock", list.get(i).getFirst().getStock());
                editor.putInt("type", list.get(i).getFirst().getType());
            }
            if (list.get(i).getSecond() != null) {

                editor.putString("name2", list.get(i).getSecond().getName());
                editor.putString("contents2", list.get(i).getSecond().getContents());
                editor.putString("category2", list.get(i).getSecond().getCategory());
                editor.putString("image2", list.get(i).getSecond().getImage());
                editor.putString("size2", list.get(i).getSecond().getSize());
                editor.putString("color2", list.get(i).getSecond().getColor());
                editor.putInt("price2", list.get(i).getSecond().getPrice());
                editor.putInt("price_before2", list.get(i).getSecond().getPrice_before());
                editor.putInt("stock2", list.get(i).getSecond().getStock());
                editor.putInt("type2", list.get(i).getSecond().getType());
            }
            editor.commit();
        }

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //리스트뷰 제품 추가
        if (requestCode == LIST_ADD) {
            //추가일 경우
            if (resultCode == Activity.RESULT_OK) {
                Uri test = Uri.parse(data.getStringExtra("경로"));
                ImageView test1 = (ImageView)findViewById(R.id.test);
                test1.setImageURI(test);
                ItemPair item = new ItemPair((Item) data.getSerializableExtra("item"));
                list.add(item);
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
            }

        }
        //수정 및 삭제
        else if (requestCode == LIST_ITEM_VIEW) {
            if (resultCode == Activity.RESULT_OK) {
                //삭제 명령
                if (data != null) {
                    if (data.getStringExtra("action").equals("delete")) {
                        if (data.getIntExtra("position", -1) != -1) {
                            //첫번째인지 두번째인지 확인
                            if (data.getStringExtra("order").equals("second")) {
                                list.get(data.getIntExtra("position", -1)).setSecond(null);
                                adapter.notifyDataSetChanged();
                            }
                            //두번째 아이템이 있는데 첫번째를 지울 경우
                            else if (data.getStringExtra("order").equals("first")) {
                                if (list.get(data.getIntExtra("position", -1)).getSecond() != null) {
                                    list.get(data.getIntExtra("position", -1)).setFirst(list.get(data.getIntExtra("position", -1)).getSecond());
                                    list.get(data.getIntExtra("position", -1)).setSecond(null);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    list.remove(data.getIntExtra("position", -1));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }

            if (resultCode == Activity.RESULT_CANCELED) {
                //만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
                if (data != null) {
                    //수정 명령
                    if (data.getStringExtra("action").equals("edit")) {
                        if (data.getIntExtra("position", -1) != -1) {
                            //     ItemPair item2 = new ItemPair((Item) data.getSerializableExtra("item"));
//                            list.set(data.getIntExtra("position", -1), item);
                            if (data.getStringExtra("order").equals("first")) {
                                list.get(data.getIntExtra("position", -1)).first = (Item) data.getSerializableExtra("item");
                                adapter.notifyDataSetChanged();
                            } else if (data.getStringExtra("order").equals("second")) {

                                list.get(data.getIntExtra("position", -1)).second = (Item) data.getSerializableExtra("item");
                                adapter.notifyDataSetChanged();
                            }
                        }
                        //두번째 줄 추가 명령
                    } else if (data.getStringExtra("action").equals("add_more")) {
                        if (data.getIntExtra("position", -1) != -1) {
                            list.get(data.getIntExtra("position", -1)).second = (Item) data.getSerializableExtra("item");
                            adapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        }

    }

}
