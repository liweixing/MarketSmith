package com.marketsmith;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.app.Application;

public class CustomerAppl extends Application {
    public List<Activity> activities = new ArrayList<Activity>();

    public void exitall() {
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.setStatisticsEnable(false);
        JPushInterface.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
