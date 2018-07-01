package com.example.jshop;

/**
 * Created by 이정배 on 2018-01-02.
 */

public class ItemPair {
    Item first, second;

    public ItemPair(Item item) {
        this.first = item;
    }
    public Item getFirst() {
        return this.first;
    }
    public Item getSecond() {
        return this.second;
    }

    public ItemPair(String name, String contents, String category, String image, String size, String color, int price, int price_before, int stock, int type) {
        first = new Item(name, contents, category, image, size, color, price, price_before, stock, type);
    }

    public void setSecond(Item second) {
        this.second = second;
    }

    public void setFirst(Item first) {
        this.first = first;
    }
}
