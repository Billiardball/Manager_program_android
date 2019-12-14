package com.driver.porttrucker.Common;

import com.driver.porttrucker.Model.NewsModel;
import com.driver.porttrucker.Model.UserModel;

import java.util.ArrayList;

public class Common {
    public static UserModel userModel = null;
    public static ArrayList<NewsModel> allNews = new ArrayList<>();
    public static int currentNewsIndex;
    public static double lat;
    public static double lng;
}
