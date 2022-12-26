package com.meferi.sync;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import com.meferi.devicemanager.MefDeviceManager;
import com.meferi.sdk.IDeviceManager;

/* loaded from: classes.dex */
public class DeviceManagerPlus {
    private static IDeviceManager deviceManagerPlus;
    private static DeviceManagerPlus dmp;
    private static Context mContext;
    private static IBinder mIBinder;
    private final String TAG = DeviceManagerPlus.class.getSimpleName();
    private final String SERVICENAME = "sdkservice";

    private DeviceManagerPlus() {
    }
    public static String getDeviceId() {
        String sn = MefDeviceManager.getInstance().getProperty("ro.serialno");
        return sn;
    }

    public void setSystemConfig(String key, String val) {
        Log.d("bds", "key="+key+" val="+val);
        if (getProxy()  == null) {
            return ;
        }
        try {
             getProxy().setSystemConfig(key,  val);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public static DeviceManagerPlus get(Context context) {
        mContext = context;
        if (dmp == null) {
            dmp = new DeviceManagerPlus();
        }
        return dmp;
    }
    private IDeviceManager getProxy(){
        return IDeviceManager.Stub.asInterface(ServiceManager.getService(SERVICENAME));
    }

    public String getSystemCongfig(String str) {
        if (getProxy()  == null) {
            return "";
        }
        try {
            return getProxy().getSystemCongfig(str);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }

}
