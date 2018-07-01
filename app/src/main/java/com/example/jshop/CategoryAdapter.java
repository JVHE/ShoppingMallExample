package com.example.jshop;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 이정배 on 2018-01-20.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    ArrayList<String> groupList = null;
    ArrayList<ArrayList<String>> childList = null;
    LayoutInflater inflater = null;
    ViewHolder viewHolder = null;
    ItemHolder itemHolder = null;

    Animation fade_in;

    public CategoryAdapter(Context c, ArrayList<String> groupList,
                           ArrayList<ArrayList<String>> childList) {
        super();
        this.inflater = LayoutInflater.from(c);
        this.groupList = groupList;
        this.childList = childList;
    }

    class ViewHolder {
        public TextView category;
        public ImageView navigate;
    }

    class ItemHolder {
        public TextView item;
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
    public String getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
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
            v = inflater.inflate(R.layout.category_row, viewGroup, false);
            viewHolder.category = (TextView) v.findViewById(R.id.category);
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

        viewHolder.category.setText(getGroup(groupPosition));


        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        View v = view;

        if (v == null) {
            itemHolder = new ItemHolder();
            v = inflater.inflate(R.layout.item_row, null);
            itemHolder.item = (TextView) v.findViewById(R.id.item);
            v.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) v.getTag();
        }

        fade_in = AnimationUtils.loadAnimation(v.getContext(), R.anim.category_fade_in);
        v.startAnimation(fade_in);

        itemHolder.item.setText(getChild(groupPosition, childPosition));
        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
