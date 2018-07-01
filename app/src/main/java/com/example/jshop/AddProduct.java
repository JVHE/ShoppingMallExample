package com.example.jshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class AddProduct extends AppCompatActivity  {
    Uri mImageCaptureUri=null;
    //사진 선택하기 버튼을 눌렀을 경우 카메라에서 가져온다면 PICK_FROM_CAMERA, 앨범에서 가져온다면 PICK_FROM_ALBUM
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    //추가인지 수정인지 확인
    TextView title;

    //name: 제품 이름, content: 상세 정보, image: 이미지, size: 사이즈, color: 색상
    TextInputEditText name, contents, category, size, color;
    //price_before: 세일 전 가격, price: 현재 가격, stock: 재고;
    TextInputEditText price, price_before, stock;

    //이미지
    ImageView image;

    //이미지 스트링 변환 후 전달용
    String image_string = "";

    //판매 형식 0: 일반 1: 세일 2: 콜라보레이션
    TextInputEditText type;

    //이미지 업로드 버튼
    Button btn_image_upload, btn_add;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        //변수별 아이디 매칭
        name = (TextInputEditText) findViewById(R.id.name);
        contents = (TextInputEditText) findViewById(R.id.contents);
        category = (TextInputEditText) findViewById(R.id.category);
        image = (ImageView) findViewById(R.id.image);
        size = (TextInputEditText) findViewById(R.id.size);
        color = (TextInputEditText) findViewById(R.id.color);
        price = (TextInputEditText) findViewById(R.id.price);
        price_before = (TextInputEditText) findViewById(R.id.price_before);
        stock = (TextInputEditText) findViewById(R.id.stock);
        // type = (TextInputEditText) findViewById(R.id.type);


        btn_image_upload = (Button) findViewById(R.id.btn_image_upload);
        btn_add = (Button) findViewById(R.id.btn_add);
        title = (TextView) findViewById(R.id.title);

        //인텐트를 이용해서 추가인지 수정인지 확인하기
        intent = getIntent();
        title.setText(intent.getStringExtra("title"));

        //수정일 경우 값 복사
        if (intent.getStringExtra("title").equals("제품 수정")) {
            Toast.makeText(getApplicationContext(), "수정 칸입니다.", Toast.LENGTH_SHORT);

            Item item = (Item) intent.getSerializableExtra("edit");
            btn_add.setText("수정하기");
            name.setText(item.getName());
            contents.setText(item.getContents());
            category.setText(item.getCategory());
            //이미지 처리
            if (!item.getImage().equals("")) {
                byte[] decodedByteArray = Base64.decode(item.getImage(), Base64.NO_WRAP);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
                image.setImageBitmap(decodedBitmap);
            }
            image_string = item.getImage();
            size.setText(item.getSize());
            color.setText(item.getColor());
            price.setText(Integer.toString(item.getPrice()));
            price_before.setText(Integer.toString(item.getPrice_before()));
            stock.setText(Integer.toString(item.getStock()));
            // type.setText(Integer.toString(item.getType()));
        }

        //이미지 선택 버튼
        btn_image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_load = new AlertDialog.Builder(AddProduct.this);
                alert_load.setTitle("업로드할 이미지 선택").
                        setPositiveButton("사진 촬영", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICK_FROM_CAMERA);
                                return;
                            }
                        }).setNeutralButton("앨범 선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_FROM_ALBUM);
                        return;
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
                        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        startActivityForResult(intent, 3);


                        return;
                    }
                });
                AlertDialog alert = alert_load.create();
                alert.show();
            }
        });

        //저장된 내용을 추가해서 보내기
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int int_price, int_price_before, int_stock, int_type;
                int_price = (price.getText().toString().equals("")) ? 0 : Integer.parseInt(price.getText().toString());
                int_price_before = (price_before.getText().toString().equals("")) ? 0 : Integer.parseInt(price_before.getText().toString());
                int_stock = (stock.getText().toString().equals("")) ? 0 : Integer.parseInt(stock.getText().toString());
                int_type = 0;//(type.getText().toString().equals("")) ? 0 : Integer.parseInt(type.getText().toString());
                //Item item = new Item("test", "체크", "", "", "", "Red", 100000, 150000, 5, 1);
                Item item = new Item(name.getText().toString(), contents.getText().toString(), category.getText().toString(), image_string, size.getText().toString(), color.getText().toString(), int_price, int_price_before, int_stock, int_type);
                Intent intent = new Intent();
                intent.putExtra("경로", ""+mImageCaptureUri);
                intent.putExtra("item", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


    //비트맵을 스트링으로 바꿔주는 메소드
    public String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA: {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(photo);
                image_string = getBase64String(photo);
            }
            break;
            case PICK_FROM_ALBUM: {
                Bitmap photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(photo);
                image_string = getBase64String(photo);
            }
            break;
            case 3: {
                image.setImageURI(mImageCaptureUri);
                Toast.makeText(getApplicationContext(), "사진가져옴 " + mImageCaptureUri, Toast.LENGTH_SHORT).show();

            }
            break;
        }
    }
}
