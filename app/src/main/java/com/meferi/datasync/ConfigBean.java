package com.meferi.datasync;

import android.graphics.Bitmap;

/* loaded from: classes2.dex */
public class ConfigBean {
    private String config;
    private String configName;
    private Bitmap configQrcode;
    private String configType;

    public ConfigBean(String str, String str2, String str3, Bitmap bitmap) {
        this.configName = str;
        this.configType = str2;
        this.config = str3;
        this.configQrcode = bitmap;
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(String str) {
        this.config = str;
    }

    public String getConfigName() {
        return this.configName;
    }

    public void setConfigName(String str) {
        this.configName = str;
    }

    public String getConfigType() {
        return this.configType;
    }

    public void setConfigType(String str) {
        this.configType = str;
    }

    public Bitmap getConfigQrcode() {
        return this.configQrcode;
    }

    public void setConfigQrcode(Bitmap bitmap) {
        this.configQrcode = bitmap;
    }
}
