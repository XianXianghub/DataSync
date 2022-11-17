package com.meferi.sync.qrcode;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meferi.sync.ConfigBean;
import com.meferi.sync.MainApplication;
import com.meferi.sync.SettingConfigBean;
import com.meferi.sync.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class QrcodeViewModel extends ViewModel {
    private MutableLiveData<List<ConfigBean>> configs;
    private MutableLiveData<Boolean> isSuccess;
//    private ScanManager scanManager;
    ThreadPoolExecutor service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MICROSECONDS, new LinkedBlockingDeque());

    public MutableLiveData<Boolean> isSuccess() {
        if (this.isSuccess == null) {
            this.isSuccess = new MutableLiveData<>();
        }
        return this.isSuccess;
    }

    public MutableLiveData<List<ConfigBean>> getConfigs() {
        if (this.configs == null) {
            this.configs = new MutableLiveData<>();
            this.configs.setValue(new ArrayList());
        }
        return this.configs;
    }

    public void ScanTypeResume() {
//        try {
//            ScanManager scanManager = new ScanManager();
//            this.scanManager = scanManager;
//            scanManager.setParameterInts(new int[]{PropertyID.WEDGE_KEYBOARD_ENABLE}, new int[]{PreferenceUtil.getInt("keyboard", 0)});
//            Log.d("切换回原本的扫描模式成功");
//        } catch (Error e) {
//            DLog.m46e("切换回原本的扫描模式失败", Log.getStackTraceString(e));
//        } catch (Exception e2) {
//            DLog.m46e("切换回原本的扫描模式失败", Log.getStackTraceString(e2));
//        }
    }

    public void ConfigExpert(Intent intent) {

        Log.d("bds", "ConfigExpert");
        ScanTypeResume();
        SettingConfigBean settingConfigBean = (SettingConfigBean) intent.getSerializableExtra("data");
        this.configs.setValue(new ArrayList());
      //  ConfigExpertT(settingConfigBean.isScan(), settingConfigBean.isSystem());
      ConfigExpertT(false, true);
    }

    private void ConfigExpertT(boolean z, boolean z2) {
        if (z) {
            this.service.execute(new Runnable() { // from class: com.ubx.ustage.ui.qrcode.-$$Lambda$QrcodeViewModel$YiWgxDmUSkl2lYENdYrasjFU3OI
             

                @Override // java.lang.Runnable
                public final void run() {
                    QrcodeViewModel.this.exportScanSettings(true);
                }
            });
        }
        if (z2) {
            this.service.execute(new Runnable() { // from class: com.ubx.ustage.ui.qrcode.-$$Lambda$QrcodeViewModel$d207_mcD-Y758CndgtOEe4xWfyY
                @Override // java.lang.Runnable
                public final void run() {
                    QrcodeViewModel.this.exportSystemSettings();
                }
            });
        }

    }

    public  void exportScanSettings(boolean z) {
        try {
            Log.d("bds", "lambda$ConfigExpertT$0$QrcodeViewModel");
            getConfigs().getValue().add(Utils.ScannerSettingExpert("scanner_"/* + DeviceUtil.getDeviceId())*/));
            if (!z) {
                this.isSuccess.postValue(true);
            }
        } catch (Exception e) {
            this.isSuccess.postValue(false);
            this.service.getQueue().clear();
            Log.d("导出扫描设置失败", Log.getStackTraceString(e));
        }
    }

    public void exportSystemSettings() {
        try {
            getConfigs().getValue().add(Utils.SystemSettingsExpert("system_" + MainApplication.deviceManagerPlus.getDeviceId()));
            this.isSuccess.postValue(true);
        } catch (Exception e) {
            this.isSuccess.postValue(false);
            Log.d("bds","导出系统设置失败");
        }
    }
}
