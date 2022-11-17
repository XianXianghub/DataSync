package com.meferi.sync;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.Window;

import com.meferi.DeflaterUtils;

import java.util.Map;

/* loaded from: classes2.dex */
public class Utils {
    public static void setWindowStatusBarColor(Activity activity, int i) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = activity.getWindow();
                window.addFlags(Integer.MIN_VALUE);
                window.setStatusBarColor(activity.getResources().getColor(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ConfigBean ScannerSettingExpert(String str) throws Exception {
        Map<String, String> map;
//        Setting_Expert(MainApplication.getInstance());
//        Thread.sleep(1000);
//        if (isExistPackage("com.ubx.datawedge")) {
//            map = queryXML("/storage/emulated/0/Default_scanner_property.xml");
//        } else {
//            map = queryXML("/storage/emulated/0/scanner_property.xml");
//        }
//        if (map.size() < 220) {
//            Thread.sleep(1000);
//            if (isExistPackage("com.ubx.datawedge")) {
//                map = queryXML("/storage/emulated/0/Default_scanner_property.xml");
//            } else {
//                map = queryXML("/storage/emulated/0/scanner_property.xml");
//            }
//        }
//        StringBuilder sb = new StringBuilder("scan\u001c");
//        LinkedHashMap<String, String> initEmptyProperty = PropertyMap.initEmptyProperty("scanner_setting.xml");
//        for (String str2 : map.keySet()) {
//            initEmptyProperty.put(str2, map.get(str2));
//        }
//        for (String str3 : initEmptyProperty.keySet()) {
//            sb.append(initEmptyProperty.get(str3));
//            sb.append("\u001c");
//        }
//        String zipBase64 = zipBase64(sb.toString());
//        return new ConfigBean(str, MainApplication.getInstance().getString(C1839R.string.config_scan), zipBase64, QRCodeUtil.createQRCode(zipBase64, TypedValues.TransitionType.TYPE_DURATION));
//
        return null;

    }

    public static ConfigBean SystemSettingsExpert(String str) throws Exception {
        StringBuilder sb = new StringBuilder("sys\u001c");
        for (Map.Entry<String, String> entry : PropertyMap.initProperty("system_setting.xml", "setting").entrySet()) {
            String systemCongfig = MainApplication.deviceManagerPlus.getSystemCongfig(entry.getValue());
            Log.d("bds", entry.getValue() + " " + systemCongfig);
            sb.append(systemCongfig);
            sb.append("\u001c");
        }
        String zipBase64 = zipBase64(sb.toString());
        return new ConfigBean(str, MainApplication.getInstance().getString(R.string.config_system), zipBase64, QRCodeUtil.createQRCode(zipBase64, 700));
    }

    public static String unzipBase64(String str) throws Exception {
        return DeflaterUtils.unzipString(str);
    }

    public static String zipBase64(String str) {
        return DeflaterUtils.zipString(str);
    }
}
