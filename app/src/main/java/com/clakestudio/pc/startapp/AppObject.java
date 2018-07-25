package com.clakestudio.pc.startapp;

/**
 * Created by pc on 2017-08-26.
 */

public class AppObject {

   private String Titel;
   private String ShortDesc;
    private String LongDesc;
    private String PackageName;
   private String UserId;
    private String Category;
    private int Color;
    private int AppRating;
    private int AppTime;

    private  int TextColor;
    private String RootTitel;
    AppObject() {

    }



    AppObject(String titel, String shortDesc, String longDesc, String packageName,
              String userId, int appRating, int color, String category,
              int appTime, String rootTitel, int textColor) {
        this.Titel=titel;
        this.ShortDesc=shortDesc;
        this.LongDesc=longDesc;
        this.PackageName=packageName;
        this.UserId=userId;
        this.AppRating=appRating;
        this.Color=color;
        this.Category =category;
        this.AppTime=appTime;
        this.RootTitel=rootTitel;
        this.TextColor=textColor;

    }

    public String getTitel() {
        return Titel;
    }

    public void setTitel(String titel) {
        Titel = titel;
    }

    public String getShortDesc() {
        return ShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        ShortDesc = shortDesc;
    }

    public String getLongDesc() {
        return LongDesc;
    }

    public void setLongDesc(String longDesc) {
        LongDesc = longDesc;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
    public int getAppRating() {
        return AppRating;
    }

    public void setAppRating(int appRating) {
        AppRating = appRating;
    }
    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
    public int getAppTime() {
        return AppTime;
    }

    public void setAppTime(int appTime) {
        AppTime = appTime;
    }

    public String getRootTitel() {
        return RootTitel;
    }

    public void setRootTitel(String rootTitel) {
        RootTitel = rootTitel;
    }

    public int getTextColor() {
        return TextColor;
    }

    public void setTextColor(int textColor) {
        TextColor = textColor;
    }




}
