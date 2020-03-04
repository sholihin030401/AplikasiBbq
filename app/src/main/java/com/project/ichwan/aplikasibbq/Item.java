package com.project.ichwan.aplikasibbq;

public class Item {
    private String itemName;
    private String imageurl;

    public Item(String itemName, String imageurl) {
        this.itemName = itemName;
        this.imageurl = imageurl;
    }

    public Item(){

    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
