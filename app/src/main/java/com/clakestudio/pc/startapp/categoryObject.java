package com.clakestudio.pc.startapp;

/**
 * Created by pc on 2017-08-27.
 */

public class categoryObject {

    public String getCategory() {
        return Category;
    }

    private String Category;

    categoryObject(String category) {
        this.Category = category;
    }

    public void setCategory(String category) {
        Category = category;
    }

}
