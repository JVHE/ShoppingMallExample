package com.example.jshop;

import java.io.Serializable;

/**
 * Created by 이정배 on 2017-12-30.
 */

public class Item implements Serializable {
    //name: 제품 이름, content: 상세 정보, image: 이미지, size: 사이즈, color: 색상
    String name, contents, category, image, size, color;
    //price_before: 세일 전 가격, price: 현재 가격, stock: 재고;
    int price, price_before, stock;

    //판매 형식 0: 일반 1: 세일 2: 콜라보레이션
    int type = 0;

    //getXxx: Xxx 값 반환 메소드
    public String getName() {
        return name;
    }

    public String getContents() {
        return contents;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public int getPrice() {
        return price;
    }

    public int getPrice_before() {
        return price_before;
    }

    public int getStock() {
        return stock;
    }

    public int getType() {
        return type;
    }

    //setXxx: Xxx 값 변경 메소드
    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPrice_before(int price_before) {
        this.price_before = price_before;
    }

    public void setType(int type) {
        this.type = type;
    }

    //addStock_size: 재고 추가 메소드
    public void addStock(int addStock) {
        this.stock += addStock;
    }


    //초기 생성자
    public Item(String name, String contents, String category, String image, String size, String color, int price, int price_before, int stock, int type) {
        this.name = name;
        this.contents = contents;
        this.category = category;
        this.image = image;
        this.size = size;
        this.color = color;
        this.price = price;
        this.price_before = price_before;
        this.stock = stock;
        this.type = type;
    }
    public Item() {
        this.name = "";
        this.contents = "";
        this.category = "";
        this.image = "";
        this.size = "";
        this.color = "";
        this.price = 0;
        this.price_before = 0;
        this.stock = 0;
        this.type = 0;
    }
}
