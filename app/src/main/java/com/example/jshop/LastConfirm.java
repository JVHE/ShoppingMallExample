package com.example.jshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class LastConfirm extends AppCompatActivity {

    TextView name, address, phone, price, total_price;
    Button payment;
    AlertDialog.Builder alert_load;
    AlertDialog alert;

    ArrayList<Item> mGroupList = null;
    ArrayList<ArrayList<Item>> mChildList = null;
    ArrayList<Item> mChildListContent = null;
    ExpandableListView mListView = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_confirm);

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        phone = (TextView) findViewById(R.id.phone);
        price = (TextView) findViewById(R.id.price);
        total_price = (TextView) findViewById(R.id.total_price);
        payment = (Button) findViewById(R.id.payment);

        mListView = (ExpandableListView) findViewById(R.id.item_list);

        mGroupList = new ArrayList<Item>();
        mChildList = new ArrayList<ArrayList<Item>>();
        mChildListContent = new ArrayList<Item>();

        mGroupList.add(new Item());

        mChildListContent = new ArrayList<Item>();

        //유저 아이디 불러오기
        sp = getSharedPreferences("current_user", MODE_PRIVATE);
        user_id = sp.getString("current_user", "비회원");


        //listview에 sp에 저장된 아이템 불러오기.
        int cnt = 1;
        sp = getSharedPreferences(user_id + "_bag_0", MODE_PRIVATE);
        while (!sp.getString("name", "").equals("")) {
            mChildListContent.add(new Item(sp.getString("name", ""), sp.getString("contents", ""), sp.getString("category", ""), sp.getString("image", ""), sp.getString("size", ""),
                    sp.getString("color", ""), sp.getInt("price", 0), sp.getInt("price_before", 0), sp.getInt("stock", 0), sp.getInt("type", 0)));

            sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
            cnt++;
        }

        mChildList.add(mChildListContent);

        mListView.setAdapter(new ConfirmAdapter(this, mGroupList, mChildList));

        if (!user_id.equals("비회원")) {
            sp = getSharedPreferences("user_info_" + user_id, MODE_PRIVATE);
            name.setText(sp.getString("name", ""));
            address.setText(sp.getString("addr", ""));
            phone.setText("" + sp.getInt("phone", 0));
        }

        // 그룹 클릭 했을 경우 이벤트
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            //     int lastClickedPosition = -1;

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                setExpandableListViewHeight(mListView, groupPosition);
                return false;
            }
        });

        int total = 0;
        int total_count = 0;
        for (int i = 0; i < mChildListContent.size(); i++) {
            total += mChildListContent.get(i).getStock() * mChildListContent.get(i).getPrice();
            total_count += mChildListContent.get(i).getStock();
        }
        //숫자에 천단위마다 콤마 찍어주기
        DecimalFormat formatter = new DecimalFormat("#,### 원");
        String formatted = formatter.format(total);
        price.setText(formatted);
        total_price.setText(formatted);


        alert_load = new AlertDialog.Builder(LastConfirm.this);
        alert_load.setTitle("최종확인");
        alert_load.setMessage("총 "+total_count+"상품\n가격: " +total+"원\n배송지 :"+sp.getString("addr", "")+"\n결제방식: 계좌이체\n입금계좌: 우리은행 1002-030-715510 예금주:이정배\n*계좌이체는 입금 확인 후 배송됩니다."+"\n결제 사항을 확인하셨습니까?");
        alert_load.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int cnt = 0;
                sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                do {
                    editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    cnt++;
                    sp = getSharedPreferences(user_id + "_bag_" + Integer.toString(cnt), MODE_PRIVATE);
                } while (!sp.getString("name", "").equals(""));

                Toast.makeText(LastConfirm.this, "결제 완료!!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });
        //아니오일 경우 작성중이던 내용 모두 지움
        alert_load.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        alert = alert_load.create();


        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.show();
            }
        });

    }

    private void setExpandableListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        if (listAdapter == null) {
            return;
        }

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            view = listAdapter.getGroupView(i, false, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
            if (((listView.isGroupExpanded(i)) && (i != group)) || ((!listView.isGroupExpanded(i)) && (i == group))) {
                View listItem = null;
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    listItem = listAdapter.getChildView(i, j, false, listItem, listView);
                    listItem.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, View.MeasureSpec.UNSPECIFIED));
                    listItem.measure(
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
