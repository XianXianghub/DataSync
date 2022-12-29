package com.meferi.datasync;

import android.app.Application;

/* loaded from: classes2.dex */
public class MainApplication extends Application {
    public static DeviceManagerPlus deviceManagerPlus;
    private static MainApplication instance;
//    public static ArrayList<SystemItem> items;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        instance = this;
        deviceManagerPlus = DeviceManagerPlus.get(this);
    }
}
