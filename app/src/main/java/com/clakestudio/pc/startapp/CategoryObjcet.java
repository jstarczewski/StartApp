package com.clakestudio.pc.startapp;

/**
 * Created by pc on 2017-08-27.
 */

public class CategoryObjcet {

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    private String Category;

    CategoryObjcet(String category) {
        this.Category = category;
    }

}
