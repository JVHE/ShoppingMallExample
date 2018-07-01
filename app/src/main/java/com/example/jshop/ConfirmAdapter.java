package com.example.jshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by 이정배 on 2018-01-22.
 */

public class ConfirmAdapter extends BaseExpandableListAdapter {

    ArrayList<Item> groupList = null;
    ArrayList<ArrayList<Item>> childList = null;
    LayoutInflater inflater = null;
    ViewHolder viewHolder = null;
    ItemHolder itemHolder = null;
    Animation fade_in;

    public ConfirmAdapter(Context c, ArrayList<Item> groupList,
                          ArrayList<ArrayList<Item>> childList) {
        super();
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
    }

    class ViewHolder {
        public TextView stock_count;
        public ImageView navigate;
    }

    class ItemHolder {
        public TextView name, stock_count, total_price;
        public ImageView image;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Item getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Item getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        //뷰 받아오기
        View v = view;

        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.confirm_row, viewGroup, false);
            viewHolder.stock_count = (TextView) v.findViewById(R.id.stock_count);
            viewHolder.navigate = (ImageView) v.findViewById(R.id.navigate);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
        if (isExpanded) {
//            Toast.makeText((Activity) v.getContext(), "뷰 열림", Toast.LENGTH_SHORT).show();
            viewHolder.navigate.setImageResource(R.drawable.navigate_up);
        } else {
//            Toast.makeText((Activity) v.getContext(), "뷰 닫힘", Toast.LENGTH_SHORT).show();
            viewHolder.navigate.setImageResource(R.drawable.navigate_down);
        }

        int total_count = 0;
        for (int i = 0; i < getChildrenCount(groupPosition); i++) {
            total_count += getChild(groupPosition, i).getStock();
        }
        viewHolder.stock_count.setText("" + total_count + " 제품");


        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null) {
            itemHolder = new ItemHolder();
            v = inflater.inflate(R.layout.confirm_child_row, null);
            itemHolder.name = (TextView) v.findViewById(R.id.name);
            itemHolder.stock_count = (TextView) v.findViewById(R.id.stock_count);
            itemHolder.image = (ImageView) v.findViewById(R.id.image);
            itemHolder.total_price = (TextView)v.findViewById(R.id.total_price);
            v.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) v.getTag();
        }

        fade_in = AnimationUtils.loadAnimation(v.getContext(), R.anim.category_fade_in);
        v.startAnimation(fade_in);


        itemHolder.name.setText(getChild(groupPosition,childPosition).getName());
        itemHolder.stock_count.setText("x"+getChild(groupPosition,childPosition).getStock());

        //이미지 처리
        if (!getChild(groupPosition, childPosition).getImage().equals("")) {
            byte[] decodedByteArray = Base64.decode(getChild(groupPosition, childPosition).getImage(), Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            itemHolder.image.setImageBitmap(decodedBitmap);
        } else {
            itemHolder.image.setImageResource(R.drawable.women);
        }

        DecimalFormat formatter = new DecimalFormat("#,### 원");
        String formatted = formatter.format(getChild(groupPosition, childPosition).getStock() * getChild(groupPosition, childPosition).getPrice());
        //전체 가격 입력
        itemHolder.total_price.setText(formatted);



        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
