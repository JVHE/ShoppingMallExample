package com.example.jshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by 이정배 on 2017-12-30.
 */

public class MyAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<ItemPair> data;
    Button to_product;
    TextView name, price, price_before, color;
    Context context;
    ImageView image;
    Button to_product2;
    TextView name2, price2, price_before2, color2;
    ImageView image2;

    //생성자
    public MyAdapter(Context context, ArrayList<ItemPair> data) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item, null);
        }
        //포지션 값 받아옴.
        final int position = i;




        name = (TextView) view.findViewById(R.id.name);
        price = (TextView) view.findViewById(R.id.price);
        price_before = (TextView) view.findViewById(R.id.price_before);
        color = (TextView) view.findViewById(R.id.color);
        to_product = (Button) view.findViewById(R.id.product_button);
        image = (ImageView) view.findViewById(R.id.image);

        to_product.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Product.class);
                intent.putExtra("product", data.get(position).getFirst());
                intent.putExtra("position", position);
                intent.putExtra("order", "first");
                ((Activity) v.getContext()).startActivityForResult(intent, 1);
            }
        });
        byte[] decodedByteArray = Base64.decode(data.get(i).getFirst().getImage(), Base64.NO_WRAP);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
        image.setImageBitmap(decodedBitmap);
        //이미지 처리
        if (!data.get(i).getFirst().getImage().equals("")) {
//            image.setMaxHeight(300);
//            byte[] decodedByteArray = Base64.decode(data.get(i).getFirst().getImage(), Base64.NO_WRAP);
//            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
//            image.setImageBitmap(decodedBitmap);
            image.setMaxHeight(2000);
        }
        else {
            image.setMaxHeight(0);
        }

        name.setText(data.get(i).getFirst().getName());
        //숫자에 천단위마다 콤마 찍어주기
        DecimalFormat formatter = new DecimalFormat("#,### 원");
        String formatted = formatter.format(data.get(i).getFirst().getPrice());
        price.setText(formatted);
        //이전 가격에 콤마 찍어주기
        formatted = formatter.format(data.get(i).getFirst().getPrice_before());
        price_before.setText(formatted);
        price_before.setPaintFlags(price_before.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (data.get(i).getFirst().getColor().equals("")) {
            color.setMaxHeight(0);
        }


        //두번째 이미지 처리 -----------

        if (data.get(i).getSecond() != null) {
            LinearLayout item2 = (LinearLayout) view.findViewById(R.id.item2);


            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item2.getLayoutParams();
            params.weight = 1;
//            params.width =
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            item2.setLayoutParams(params);

            name2 = (TextView) view.findViewById(R.id.name2);
            price2 = (TextView) view.findViewById(R.id.price2);
            price_before2 = (TextView) view.findViewById(R.id.price_before2);
            color2 = (TextView) view.findViewById(R.id.color2);
            to_product2 = (Button) view.findViewById(R.id.product_button2);
            image2 = (ImageView) view.findViewById(R.id.image2);

            to_product2.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Product.class);
                    intent.putExtra("product", data.get(position).getSecond());
                    intent.putExtra("position", position);
                    intent.putExtra("order", "second");
                    ((Activity) v.getContext()).startActivityForResult(intent, 1);
                }
            });
            decodedByteArray = Base64.decode(data.get(i).getSecond().getImage(), Base64.NO_WRAP);
            decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            image2.setImageBitmap(decodedBitmap);
            //이미지 처리
            if (!data.get(i).getSecond().getImage().equals("")) {
//                byte[] decodedByteArray = Base64.decode(data.get(i).getSecond().getImage(), Base64.NO_WRAP);
//                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
//                image2.setImageBitmap(decodedBitmap);
            }
            else {
                image2.setMaxWidth(0);
            }

            name2.setText(data.get(i).getSecond().getName());
            //숫자에 천단위마다 콤마 찍어주기
            formatter = new DecimalFormat("#,### 원");
            formatted = formatter.format(data.get(i).getSecond().getPrice());
            price2.setText(formatted);
            //이전 가격에 콤마 찍어주기
            formatted = formatter.format(data.get(i).getSecond().getPrice_before());
            price_before2.setText(formatted);
            price_before2.setPaintFlags(price_before2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (data.get(i).getSecond().getColor().equals("")) {
                color2.setMaxHeight(0);
            }

        } else {
            LinearLayout item2 = (LinearLayout) view.findViewById(R.id.item2);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) item2.getLayoutParams();
            params.weight = 0;
            params.height = 0;
            item2.setLayoutParams(params);

            to_product.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Product.class);
                    intent.putExtra("product", data.get(position).getFirst());
                    intent.putExtra("position", position);
                    intent.putExtra("is_single", true);
                    intent.putExtra("order", "first");
                    ((Activity) v.getContext()).startActivityForResult(intent, 1);
                }
            });
        }

        return view;
    }
}
