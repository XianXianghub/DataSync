package com.meferi.datasync;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class SettingConfigBean implements Serializable {
    private int action;
    private boolean scan;
    private boolean system;
    private String tip;

    public SettingConfigBean() {
    }

    public SettingConfigBean(String str, int i, boolean z, boolean z2) {
        this.tip = str;
        this.action = i;
        this.scan = z;
        this.system = z2;
    }

    public SettingConfigBean(int i, boolean z, boolean z2) {
        this.action = i;
        this.scan = z;
        this.system = z2;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String str) {
        this.tip = str;
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int i) {
        this.action = i;
    }

    public boolean isScan() {
        return this.scan;
    }

    public void setScan(boolean z) {
        this.scan = z;
    }

    public boolean isSystem() {
        return this.system;
    }

    public void setSystem(boolean z) {
        this.system = z;
    }
}
