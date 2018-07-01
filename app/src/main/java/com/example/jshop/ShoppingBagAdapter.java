package com.example.jshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by 이정배 on 2018-01-05.
 */

public class ShoppingBagAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Button to_product;
    ImageButton remove;
    TextView name, price, color, size, stock, stock_count;
    Context context;
    ArrayList<Item> data;
    ImageView image;
    ImageButton plus, minus;

    public ShoppingBagAdapter(Context context, ArrayList<Item> data) {
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
            view = LayoutInflater.from(context).inflate(R.layout.shopping_bag_item, null);
        }
        //포지션 값 받아옴.
        final int position = i;


        name = (TextView) view.findViewById(R.id.name);
        price = (TextView) view.findViewById(R.id.price);
        color = (TextView) view.findViewById(R.id.color);
        size = (TextView) view.findViewById(R.id.size);
        image = (ImageView) view.findViewById(R.id.image);
        stock = (TextView) view.findViewById(R.id.stock);
        plus = (ImageButton) view.findViewById(R.id.plus);
        minus = (ImageButton) view.findViewById(R.id.minus);
        stock_count = (TextView) view.findViewById(R.id.stock_count);

        //이미지 처리
        if (!data.get(i).getImage().equals("")) {
            byte[] decodedByteArray = Base64.decode(data.get(i).getImage(), Base64.NO_WRAP);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            image.setImageBitmap(decodedBitmap);
        } else {
            image.setImageResource(R.drawable.women);
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.get(position).setStock(data.get(position).getStock() + 1);
                stock_count.setText(""+data.get(position).getStock());
                notifyDataSetChanged();
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(position).getStock() > 1) {
                    data.get(position).setStock(data.get(position).getStock() - 1);
                    stock_count.setText(""+data.get(position).getStock());
                    notifyDataSetChanged();
                } else {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(view.getContext());
                    alert_confirm.setMessage("장바구니에서 지우겠습니까?").setCancelable(false).setPositiveButton("네",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 'yes'
                                    data.remove(position);
                                    notifyDataSetChanged();
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

            }
        });

        stock_count.setText(""+data.get(i).getStock());
        //이름 적기
        name.setText(data.get(i).getName());
        //숫자에 천단위마다 콤마 찍어주기
        DecimalFormat formatter = new DecimalFormat("#,### 원");
        String formatted = formatter.format(data.get(i).getStock() * data.get(i).getPrice());
        //전체 가격 입력
        price.setText(formatted);

        //개당 가격 * 상품 가격 표시
        formatted = formatter.format(data.get(i).getPrice());
        stock.setText("" + data.get(i).getStock() + " * " + formatted);

        //색상 입력
        color.setText(data.get(i).getColor());
        //사이즈 입력
        size.setText(data.get(i).getSize());
        //재고 입력에 원가 곱해서 보여준다.
        //stock.setText(data.get(i).getStock());

        remove = (ImageButton) view.findViewById(R.id.remove);
        remove.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(v.getContext());
                alert_confirm.setMessage("장바구니에서 지우겠습니까?").setCancelable(false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 'yes'
                                data.remove(position);
                                notifyDataSetChanged();
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
        });

        return view;
    }
}
