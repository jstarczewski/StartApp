package com.clakestudio.pc.startapp;

/**
 * Created by pc on 2017-09-01.
 */

public class RatingObject {


    private String Id;
    private String Titel;


    private String AppRating;


    RatingObject() {
    }

    RatingObject(String id, String appRating, String titel) {
        this.Id = id;
        this.AppRating = appRating;
        this.Titel = titel;
    }

    public String getRating() {
        return AppRating;
    }

    public void setRating(String appRating) {
        AppRating = appRating;
    }

    public void setAppRating(String appRating) {
        AppRating = appRating;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitel() {
        return Titel;
    }

    public void setTitel(String titel) {
        Titel = titel;
    }


}
