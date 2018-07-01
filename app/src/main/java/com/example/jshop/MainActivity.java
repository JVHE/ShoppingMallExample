package com.example.jshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.jshop.R.drawable.abc_ic_voice_search_api_material;
import static com.example.jshop.R.drawable.men;
import static com.example.jshop.R.drawable.sale_unisex;
import static com.example.jshop.R.drawable.shopping_bag;

public class MainActivity extends AppCompatActivity {

    EditText word;
    Button shopping_bag;
    ImageButton sale, clothes, collabo;
    //하단 네비바
    ImageButton goods, my_page, delivery_tracking, location;
    Button event;
    SharedPreferences sp;

    TextView shopping_bag_count;    //장바구니 아이템 갯수

    Thread image_change;

    ArrayList<String> mGroupList = null;
    ArrayList<ArrayList<String>> mChildList = null;
    ArrayList<String> mChildListContent = null;
    ExpandableListView mListView = null;
    String category_item = "";

    Boolean is_clicked = false;

    ScrollView scrollView;

    @Override
    protected void onStart() {

        super.onStart();
        //sp에 저장되어 있는 장바구니 아이템 갯수 가져와보기.
        //장바구니 버튼의 제품 갯수 보여주는 기능. 항상 갱신이 필요하기 때문에 onStart() 내부에 집어넣는다.

        //유저 id확인
        sp = getSharedPreferences("current_user", MODE_PRIVATE);
        String user_id = sp.getString("current_user", "비회원");

        //장바구니에 들어있는 물건 갯수 세기.
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
        setContentView(R.layout.activity_main);


        word = (EditText) findViewById(R.id.word);

        shopping_bag = (Button) findViewById(R.id.shopping_bag);

        sale = (ImageButton) findViewById(R.id.sale);
        clothes = (ImageButton) findViewById(R.id.clothes);
        collabo = (ImageButton) findViewById(R.id.collabo);

        my_page = (ImageButton) findViewById(R.id.my_page);

        scrollView = (ScrollView) findViewById(R.id.scroll1);

        mListView = (ExpandableListView) findViewById(R.id.sale_category);

        mGroupList = new ArrayList<String>();
        mChildList = new ArrayList<ArrayList<String>>();
        mChildListContent = new ArrayList<String>();

        mGroupList.add("여성");
        mGroupList.add("남성");
        mGroupList.add("공용");

        mChildListContent.add("상의");
        mChildListContent.add("하의");
        mChildListContent.add("신발");
        mChildListContent.add("악세사리");

        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);
        mChildList.add(mChildListContent);

        mListView.setAdapter(new CategoryAdapter(this, mGroupList, mChildList));
//        setExpandableListViewHeight(mListView, -1);





        // 그룹 클릭 했을 경우 이벤트
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            //     int lastClickedPosition = -1;

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {

                //확장 리스트뷰의 크기에 따른 스크롤 뷰 길이 변경
                //    setExpandableListViewHeight(mListView, groupPosition);


                //     scrollToView(v, scrollView);

//
//                for (int i = 0; i < mGroupList.size(); i++) {
//                    if (i != groupPosition) {
//                        if (mListView.isGroupExpanded(i)) {
//                            Log.e("나머지 접을 때", "값: "+i);
//                            mListView.collapseGroup(i);
//                        }
//                    }
//                }
//                if (lastClickedPosition != -1 && lastClickedPosition != groupPosition && mListView.isGroupExpanded(lastClickedPosition)) {
//                 if(lastClickedPosition > groupPosition)
//                    mListView.collapseGroup(lastClickedPosition);
//                 else {
//                     new Thread(new Runnable() {
//                         Handler handler = new Handler();
//                         @Override
//                         public void run() {
//                             final int k = lastClickedPosition;
//                             try {
//                                 Thread.sleep(1000);
//                             } catch (InterruptedException e) {
//                                 e.printStackTrace();
//                             }
//                             handler.post(new Runnable() {
//                                 @Override
//                                 public void run() {
//
//                                     mListView.collapseGroup(k);
//                                 }
//                             });
//                         }
//                     }).start();
//                 }
//                }
                //    lastClickedPosition = groupPosition;

                //확장 리스트뷰의 크기에 따른 스크롤 뷰 길이 변경
                setExpandableListViewHeight(mListView, groupPosition);

//                //다른 그룹 눌렀을 경우 열린 그룹 닫아주기
//                Boolean isExpand = (!mListView.isGroupExpanded(groupPosition));
//                if (lastClickedPosition != -1)
//                    mListView.collapseGroup(lastClickedPosition);
//                if (isExpand) {
//                    mListView.expandGroup(groupPosition);
//                }
//                lastClickedPosition = groupPosition;


//                Toast.makeText(getApplicationContext(), "g click = " + groupPosition,
//                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 차일드 클릭 했을 경우 이벤트
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                category_item = mGroupList.get(groupPosition) + "_" + mChildListContent.get(childPosition);
                Toast.makeText(getApplicationContext(), "카테고리, 아이템 = " + category_item,
                        Toast.LENGTH_SHORT).show();
                Intent search = new Intent(getApplicationContext(), Show.class);
                search.putExtra("category_item", category_item);
                search.putExtra("item", mChildListContent.get(childPosition));
                startActivity(search);
                return false;
            }
        });
        // 그룹이 닫힐 경우 이벤트
        mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(), "g Collapse = " + groupPosition,
//                        Toast.LENGTH_SHORT).show();
            }
        });

        // 그룹이 열릴 경우 이벤트
        mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastClickedPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastClickedPosition != groupPosition)
                    mListView.collapseGroup(lastClickedPosition);
                lastClickedPosition = groupPosition;
            }
        });


//location = (ImageButton) findViewById(R.id.location) ;
//
//location.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        startActivityForResult(new Intent(getApplicationContext(), Roulette.class), 5);
//    }
//});
        event = (Button) findViewById(R.id.event);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), Roulette.class), 5);

            }
        });


        shopping_bag_count = (TextView) findViewById(R.id.shopping_bag_count);

        //스플래시 액티비티 잠깐 생성
        startActivity(new Intent(getApplicationContext(), Splash.class));

        //장바구니 액티비티로 이동
        shopping_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shopping_list = new Intent(getApplicationContext(), ShoppingBag.class);
                startActivity(shopping_list);
            }
        });

        //제품 검색하기
        Button find = (Button) findViewById(R.id.find);
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
                }
            }
        });

        //세일 이미지 버튼 눌렀을 때
        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (is_clicked) {


                    for (int i = 0; i < mGroupList.size(); i++) {
                        if (mListView.isGroupExpanded(i)) {
                            mListView.collapseGroup(i);
                        }
                    }

                    Thread thread = new Thread(new Runnable() {
                        Handler handler = new Handler();

                        @Override
                        public void run() {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mListView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.category_fade_out));
                                    scrollToView(sale, scrollView);
                                }
                            });
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    LinearLayout cover = (LinearLayout) findViewById(R.id.cover);
                                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cover.getLayoutParams();
                                    params.height = 0;
                                    cover.setLayoutParams(params);
                                }
                            });
                        }
                    });
                    thread.start();
                    is_clicked = false;
                } else {
                    setExpandableListViewHeight(mListView, -1);

                    LinearLayout cover = (LinearLayout) findViewById(R.id.cover);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cover.getLayoutParams();
                    params.height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    cover.setLayoutParams(params);
                    mListView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.category_fade_in));
                    scrollToViewHalf(sale, scrollView);
//                    scrollView.smoothScrollTo(0,sale.getHeight()/2);

                    is_clicked = true;

                }


//
//                Intent search = new Intent(getApplicationContext(), Show.class);
//                search.putExtra("type", "SALE");
//                startActivity(search);
            }
        });


        //clothes 이미지 버튼 눌렀을 때
        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(getApplicationContext(), Show.class);
                search.putExtra("type", "CLOTHES");
                startActivity(search);
            }
        });


        //collabo 이미지 버튼 눌렀을 때
        collabo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(getApplicationContext(), Show.class);
                search.putExtra("type", "John Varvatos X Jshop");
                startActivity(search);
            }
        });


        //마이 페이지 이동
        my_page.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sp = getSharedPreferences("current_user", MODE_PRIVATE);
                //비회원일 경우
                if (sp.getString("current_user", "비회원").equals("비회원")) {
                    startActivityForResult(new Intent(getApplicationContext(), Login.class), 1);
                }
                //회원일 경우
                else {
                    startActivityForResult(new Intent(getApplicationContext(), MyPage.class), 2);
                }
                //아래에서 위로 올라오는 효과 주고싶다.
            }
        });


        word.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v.getText().toString().length() == 0) {
                        Toast.makeText(getApplicationContext(), "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "입력 후 이동!", Toast.LENGTH_SHORT).show();
                        Intent search = new Intent(getApplicationContext(), Show.class);
                        search.putExtra("keyword", v.getText().toString());
                        startActivity(search);
                    }
                    return true;
                }
                return false;
            }
        });


        image_change = new Thread(new Runnable() {
            Handler handler = new Handler();

            @Override
            public void run() {
                final AnimationDrawable anim;
                int i = 0;
                int count = 20;
                while (count >= 0) {
                    count--;
                    i++;
                    i %= 3;
                    if (i == 0) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                sale.setImageResource(R.drawable.men);
                            }
                        });
                        Log.e("쓰레드 핸들러 테스트", "" + i);
                    } else if (i == 1) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                sale.setImageResource(R.drawable.sale_unisex);
                            }
                        });
                        Log.e("쓰레드 핸들러 테스트", "" + i);
                    } else if (i == 2) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                sale.setImageResource(R.drawable.sale_women);
                            }
                        });

                        Log.e("쓰레드 핸들러 테스트", "" + i);

                    }

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
//
//        //------------------------------------------------------------------------------------------------------------------------------------------------------
//        //어플리케이션을 꺼도 SP로 로그인이 유지되기 때문에 어플리케이션을 다시 킬 때 로그인이 되어있다는 점을 알려주기 위하여 토스트 메시지를 띄워보고 싶다.
//        sp = getSharedPreferences("current_user", MODE_PRIVATE);
//        //로그인한 상태일 경우
//        if (!sp.getString("current_user", "비회원").equals("비회원")) {
//            Toast.makeText(getApplicationContext(), "환영합니다! " + sp.getString("current_user", "비회원") + "님!", Toast.LENGTH_SHORT).show();
//        }
//        //------------------------------------------------------------------------------------------------------------------------------------------------------


        image_change.start();
    }

    @Override
    protected void onResume() {
        if (image_change.isInterrupted())
            image_change.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        image_change.interrupt();
        super.onPause();
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

    public static void scrollToView(View view, final ScrollView scrollView) {
        scrollToView(view, scrollView, 0);
    }

    public static void scrollToView(View view, final ScrollView scrollView, int count) {
        if (view != null && view != scrollView) {
            count += view.getTop();
            scrollToView((View) view.getParent(), scrollView, count);
        } else if (scrollView != null) {
            final int finalCount = count;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount);
                }
            }, 100);
        }
    }

    public static void scrollToViewHalf(View original_view, final ScrollView scrollView) {
        scrollToViewHalf(original_view, original_view, scrollView, 0);
    }

    public static void scrollToViewHalf(final View original_view, View view, final ScrollView scrollView, int count) {
        if (view != null && view != scrollView) {
            count += view.getTop();
            scrollToViewHalf(original_view, (View) view.getParent(), scrollView, count);
        } else if (scrollView != null) {
            final int finalCount = count;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount + original_view.getHeight() / 2);
                    //  scrollView.smoothScrollBy(finalCount + original_view.getHeight() / 2, 500);
                }
            }, 100);
        }
    }

}
