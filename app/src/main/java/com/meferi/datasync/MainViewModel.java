package com.meferi.datasync;


import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* renamed from: com.ubx.ustage.ui.home.MainViewModel */
/* loaded from: classes2.dex */
public class MainViewModel extends ViewModel {
    private MutableLiveData<String> actionBroadcast;
    private MutableLiveData<Boolean> isFeedBack;
    private MutableLiveData<Integer> isLoading;
//    private ScanManager scanManager;
    private MutableLiveData<Integer> scanStatus;
    ThreadPoolExecutor service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MICROSECONDS, new LinkedBlockingDeque());

    public MutableLiveData<Boolean> getIsFeedBack() {
        if (this.isFeedBack == null) {
            this.isFeedBack = new MutableLiveData<>();
        }
        return this.isFeedBack;
    }




    public void ScanTypeResume() {

    }


    public MutableLiveData<Integer> getIsLoading() {
        if (this.isLoading == null) {
            MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
            this.isLoading = mutableLiveData;
            mutableLiveData.setValue(-1);
        }
        return this.isLoading;
    }

    public void postBarcode(String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                XMWrite(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void XMWrite(String str) {
        this.service.execute(new Runnable() { // from class: com.ubx.ustage.ui.home.-$$Lambda$MainViewModel$Qw9KUp3fM0VctLibVQvsqNEzYC0
            @Override // java.lang.Runnable
            public final void run() {
                MainViewModel.this.imPortSetting(str);
            }
        });
    }

    public  void imPortSetting(String str) {
        try {
            Utils.SettingImport(MainApplication.getInstance(), str, "/sdcard/scanner_setting.xml");
            Log.d("bds","导入设置");
            ScanTypeChange();
            this.isLoading.postValue(1);
        } catch (Exception e) {
            Log.d("导入设置失败", Log.getStackTraceString(e));
            e.printStackTrace();
            this.service.getQueue().clear();
            this.isLoading.postValue(2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.lang.String[]] */
    /* JADX WARN: Type inference failed for: r1v5 */
    public void ScanTypeChange() {
//        String str = "在当前界面修改扫描模式为广播，并存储原本的扫描模式失败";
//        int i = 2;
//        i = 2;
//        try {
//            ScanManager scanManager = new ScanManager();
//            this.scanManager = scanManager;
//            if (scanManager.getParameterInts(new int[]{PropertyID.WEDGE_KEYBOARD_ENABLE})[0] == 1) {
//                PreferenceUtil.put("keyboard", 1);
//                this.scanManager.setParameterInts(new int[]{PropertyID.WEDGE_KEYBOARD_ENABLE}, new int[]{0});
//               Log.d("在当前界面修改扫描模式为广播，并存储原本的扫描模式");
//                return;
//            }
//            PreferenceUtil.put("keyboard", 0);
//        } catch (Error e) {
//            String[] strArr = new String[i];
//            strArr[0] = str;
//            strArr[1] = Log.getStackTraceString(e);
//            DLog.m46e(strArr);
//        } catch (Exception e2) {
//            while (true) {
//                i = new String[i];
//                i[0] = str;
//                str = Log.getStackTraceString(e2);
//                i[1] = str;
//                DLog.m46e(i);
//                return;
//            }
//        }
    }
}
