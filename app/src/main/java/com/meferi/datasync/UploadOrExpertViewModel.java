package com.meferi.datasync;

import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* renamed from: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertViewModel */
/* loaded from: classes2.dex */
public class UploadOrExpertViewModel extends ViewModel {
    private MutableLiveData<String> configName;
    private MutableLiveData<List<ConfigBean>> configs;
    private MutableLiveData<Integer> isSuccess;
    private String scanToolString;
    private MutableLiveData<SettingConfigBean> settingConfig;
    private String importFile = "";
    private String expertFile = "";
    private String ActivityName = "";
    private String PackageName = "";
    ThreadPoolExecutor service = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MICROSECONDS, new LinkedBlockingDeque());
    private Map<String, String> setTypeMap = new HashMap<String, String>() { // from class: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertViewModel.1
        {
            put(MainApplication.getInstance().getString(R.string.config_system_scanner), "2");
            put(MainApplication.getInstance().getString(R.string.config_scan), "0");
            put(MainApplication.getInstance().getString(R.string.config_system), "1");
        }
    };

    public MutableLiveData<String> getConfigName() {
        if (this.configName == null) {
            this.configName = new MutableLiveData<>();
        }
        return this.configName;
    }

    public MutableLiveData<SettingConfigBean> getSettingConfig(Intent intent) {
        if (this.settingConfig == null) {
            this.settingConfig = new MutableLiveData<>();
            if (intent.getSerializableExtra("data") != null) {
                this.settingConfig.setValue((SettingConfigBean) intent.getSerializableExtra("data"));
            } else {
                try {
                    SettingConfigBean settingConfigBean = new SettingConfigBean("", 0, false, false);
                    this.importFile = intent.getStringExtra("importFile");
                    this.expertFile = intent.getStringExtra("exportFile");
                    this.PackageName = intent.getStringExtra("packageName");
                    this.ActivityName = intent.getStringExtra("activityName");
                    this.settingConfig.setValue(settingConfigBean);
                } catch (Exception e) {
                    Log.d("errr", Log.getStackTraceString(e));
                }
            }
        }
        return this.settingConfig;
    }

    public MutableLiveData<SettingConfigBean> getSettingConfig() {
        if (this.settingConfig == null) {
            this.settingConfig = new MutableLiveData<>();
        }
        return this.settingConfig;
    }

    public MutableLiveData<Integer> isSuccess() {
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
//            new ScanManager().setParameterInts(new int[]{PropertyID.WEDGE_KEYBOARD_ENABLE}, new int[]{PreferenceUtil.getInt("keyboard", 0)});
//            DLog.m48d("切换回原本的扫描模式成功");
//        } catch (Exception e) {
//            Log.d("切换回原本的扫描模式失败", Log.getStackTraceString(e));
//        }
    }

    public void ConfigExpert(Intent intent) {
        ScanTypeResume();
        getSettingConfig(intent);
        ConfigExpertT(getSettingConfig().getValue().isScan(), getSettingConfig().getValue().isSystem());

    }

    public void UploadOrExpert(LifecycleOwner lifecycleOwner) {
        if (this.settingConfig.getValue().getAction() == 0) {
            uploadToUms(lifecycleOwner);
        } else {
            this.service.execute(new Runnable() { // from class: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertViewModel.2
                @Override // java.lang.Runnable
                public void run() {
                    UploadOrExpertViewModel.this.expertToSdcard();
                }
            });
        }
    }

    private void ConfigExpertT(boolean z, boolean z2) {
        if (z) {
            this.service.execute(new Runnable() { // from class: com.ubx.ustage.ui.uploadorexpert.-$$Lambda$UploadOrExpertViewModel$Tl61-RZqQzQ8s2Cpg_NYodjtW2s
                @Override // java.lang.Runnable
                public final void run() {
                    UploadOrExpertViewModel.this.lambda$ConfigExpertT$0$UploadOrExpertViewModel();
                }
            });
        }
        if (z2) {
            this.service.execute(new Runnable() { // from class: com.ubx.ustage.ui.uploadorexpert.-$$Lambda$UploadOrExpertViewModel$G3BTyKkbQPSMIsBSyKStho2tqLw
                @Override // java.lang.Runnable
                public final void run() {
                    UploadOrExpertViewModel.this.lambda$ConfigExpertT$1$UploadOrExpertViewModel();
                }
            });
        }
        if (!z2 && !z) {
            this.service.execute(new Runnable() { // from class: com.ubx.ustage.ui.uploadorexpert.-$$Lambda$UploadOrExpertViewModel$f6g6cN9C93g32ClKPTTqMd_hw-I
                @Override // java.lang.Runnable
                public final void run() {
                    UploadOrExpertViewModel.this.lambda$ConfigExpertT$2$UploadOrExpertViewModel();
                }
            });
        }
    }

    public  void lambda$ConfigExpertT$0$UploadOrExpertViewModel() {
        try {
            getConfigs().getValue().add(Utils.ScannerSettingExpert("scanner_" + MainApplication.deviceManagerPlus.getDeviceId()));
            if (this.service.getQueue().isEmpty()) {
                isSuccess().postValue(1);
            }
        } catch (Exception e) {
            isSuccess().postValue(0);
            this.service.getQueue().clear();
            Log.d("导出扫描设置失败", Log.getStackTraceString(e));
        }
    }

    public /* synthetic */ void lambda$ConfigExpertT$1$UploadOrExpertViewModel() {
        try {
            getConfigs().getValue().add(Utils.SystemSettingsExpert("system_" +MainApplication.deviceManagerPlus.getDeviceId()));
            if (this.service.getQueue().isEmpty()) {
                isSuccess().postValue(1);
            }
        } catch (Exception e) {
            isSuccess().postValue(0);
            Log.d("error","导出系统设置失败 " + Log.getStackTraceString(e));
        }
    }

    public /* synthetic */ void lambda$ConfigExpertT$2$UploadOrExpertViewModel() {
        try {
//            this.scanToolString = Utils.getScanTool(this.importFile, this.expertFile, this.ActivityName, this.PackageName);
//            if (this.service.getQueue().isEmpty()) {
//                isSuccess().postValue(1);
//            }
        } catch (Exception e) {
            isSuccess().postValue(0);
            Log.d("导出ScanTool设置失败", Log.getStackTraceString(e));
        }
    }

    public void expertToSdcard() {
        String str;
        String str2;
        String str3;
        try {
            List<ConfigBean> value = getConfigs().getValue();
            String str4 = "";

            if (value.size() > 1) {
                if (TextUtils.isEmpty(this.settingConfig.getValue().getTip())) {
                    str3 = "scanner_system_" + MainApplication.deviceManagerPlus.getDeviceId();
                } else {
                    str3 = "scanner_system_" + MainApplication.deviceManagerPlus.getDeviceId() + "_" + this.settingConfig.getValue().getTip();
                }
                str = value.get(0).getConfig() + "\u001e" + value.get(1).getConfig();
                str4 = str3;
                str2 = value.get(0).getConfigType() + "_" + value.get(1).getConfigType();
            } else if (value.size() == 1) {
                str4 = value.get(0).getConfigName();
                str2 = value.get(0).getConfigType();
                str = value.get(0).getConfig();
            } else {
                str2 = str4;
                str = str2;
            }
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", str4);
            jSONObject.put("type", this.setTypeMap.get(str2));
            jSONObject.put("config", str);
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "UStage", str4 + ".txt");
            File imageFilePath = new File(Environment.getExternalStorageDirectory() + File.separator + "UStage");

            if(!imageFilePath.exists()){
                imageFilePath.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(jSONObject.toString().getBytes());
            fileOutputStream.close();
            Utils.saveBitmapForPdf(MainApplication.getInstance(), value, Environment.getExternalStorageDirectory() + File.separator + "UStage", str4 + ".pdf");
            getConfigName().postValue(str4);
            isSuccess().postValue(2);
        } catch (Exception e) {
            getConfigName().postValue(e.toString());
            isSuccess().postValue(0);
            this.service.getQueue().clear();
            Log.d("本地导出设置失败", Log.getStackTraceString(e));
        }
    }

    private void uploadToUms(LifecycleOwner lifecycleOwner) {
//        String str;
//        String str2;
//        final C1849content content;
//        try {
//            List<ConfigBean> value = getConfigs().getValue();
//            String str3 = "";
//            if (value != null) {
//                if (value.size() > 1) {
//                    if (TextUtils.isEmpty(this.settingConfig.getValue().getTip())) {
//                        str3 = "scanner_system_" + MainApplication.deviceManagerPlus.getDeviceId();
//                    } else {
//                        str3 = "scanner_system_" + MainApplication.deviceManagerPlus.getDeviceId() + "_" + this.settingConfig.getValue().getTip();
//                    }
//                    str = MainApplication.getInstance().getString(R.string.config_system_scanner);
//                    str2 = value.get(0).getConfig() + "\u001e" + value.get(1).getConfig();
//                } else if (value.size() == 1) {
//                    str3 = value.get(0).getConfigName();
//                    str = value.get(0).getConfigType();
//                    str2 = value.get(0).getConfig();
//                }
//                if (!getSettingConfig().getValue().isScan() || getSettingConfig().getValue().isSystem()) {
//                    content = new C1849content(str3, this.setTypeMap.get(str), str2);
//                } else {
//                    str3 = "scantool_" + MainApplication.deviceManagerPlus.getDeviceId();
//                    content = new C1849content(str3, "0", DeflaterUtils.zipString(this.scanToolString));
//                }
//                getConfigName().postValue(str3);
//                UploadRequest uploadRequest = new UploadRequest(MainApplication.deviceManagerPlus.getDeviceId(), Build.MODEL, content);
//                DLog.m48d("request: " + new Gson().toJson(uploadRequest));
//                DLog.m48d("url: " + (NetConfig.getBaseHost() + ":" + NetConfig.getBasePort()));
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName("com.urovo.uhome", "com.urovo.uhome.third.UmsFuncService"));
//                intent.setAction("com.urovo.uhome.func");
//                intent.setFlags(268435456);
//                MainApplication.getInstance().bindService(intent, new ServiceConnection() { // from class: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertViewModel.3
//                    @Override // android.content.ServiceConnection
//                    public void onServiceDisconnected(ComponentName componentName) {
//                    }
//
//                    @Override // android.content.ServiceConnection
//                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                        if (iBinder != null) {
//                            try {
//                                IUmsFunction.Stub.asInterface(iBinder).uploadConfig(content.getName(), content.getType(), content.getConfig(), new IUmsCallback.Stub() { // from class: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertViewModel.3.1
//                                    @Override // com.urovo.uhome.IUmsCallback
//                                    public void onSuccess(String str4, String str5, String str6) throws RemoteException {
//                                        if (str4 == null) {
//                                            UploadOrExpertViewModel.this.getConfigName().postValue(null);
//                                            UploadOrExpertViewModel.this.isSuccess().postValue(0);
//                                        } else if (str4.equals("0")) {
//                                            UploadOrExpertViewModel.this.isSuccess().postValue(2);
//                                        } else {
//                                            UploadOrExpertViewModel.this.getConfigName().postValue(str6);
//                                            UploadOrExpertViewModel.this.isSuccess().postValue(0);
//                                        }
//                                    }
//
//                                    @Override // com.urovo.uhome.IUmsCallback
//                                    public void onFailure(String str4, String str5, String str6) throws RemoteException {
//                                        UploadOrExpertViewModel.this.getConfigName().postValue(str6);
//                                        UploadOrExpertViewModel.this.isSuccess().postValue(0);
//                                    }
//                                });
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }, 1);
//            }
//            str2 = str3;
//            str = str2;
//            if (!getSettingConfig().getValue().isScan()) {
//            }
//            content = new C1849content(str3, this.setTypeMap.get(str), str2);
//            getConfigName().postValue(str3);
//            UploadRequest uploadRequest2 = new UploadRequest(MainApplication.deviceManagerPlus.getDeviceId(), Build.MODEL, content);
//            DLog.m48d("request: " + new Gson().toJson(uploadRequest2));
//            DLog.m48d("url: " + (NetConfig.getBaseHost() + ":" + NetConfig.getBasePort()));
//            Intent intent2 = new Intent();
//            intent2.setComponent(new ComponentName("com.urovo.uhome", "com.urovo.uhome.third.UmsFuncService"));
//            intent2.setAction("com.urovo.uhome.func");
//            intent2.setFlags(268435456);
//            MainApplication.getInstance().bindService(intent2, new ServiceConnection() { // from class: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertViewModel.3
//                @Override // android.content.ServiceConnection
//                public void onServiceDisconnected(ComponentName componentName) {
//                }
//
//                @Override // android.content.ServiceConnection
//                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                    if (iBinder != null) {
//                        try {
//                            IUmsFunction.Stub.asInterface(iBinder).uploadConfig(content.getName(), content.getType(), content.getConfig(), new IUmsCallback.Stub() { // from class: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertViewModel.3.1
//                                @Override // com.urovo.uhome.IUmsCallback
//                                public void onSuccess(String str4, String str5, String str6) throws RemoteException {
//                                    if (str4 == null) {
//                                        UploadOrExpertViewModel.this.getConfigName().postValue(null);
//                                        UploadOrExpertViewModel.this.isSuccess().postValue(0);
//                                    } else if (str4.equals("0")) {
//                                        UploadOrExpertViewModel.this.isSuccess().postValue(2);
//                                    } else {
//                                        UploadOrExpertViewModel.this.getConfigName().postValue(str6);
//                                        UploadOrExpertViewModel.this.isSuccess().postValue(0);
//                                    }
//                                }
//
//                                @Override // com.urovo.uhome.IUmsCallback
//                                public void onFailure(String str4, String str5, String str6) throws RemoteException {
//                                    UploadOrExpertViewModel.this.getConfigName().postValue(str6);
//                                    UploadOrExpertViewModel.this.isSuccess().postValue(0);
//                                }
//                            });
//                        } catch (RemoteException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }, 1);
//        } catch (Exception e) {
//            getConfigName().postValue(e.toString());
//            isSuccess().postValue(0);
//            this.service.getQueue().clear();
//            Log.d("上传至UMS失败", Log.getStackTraceString(e));
//        }
    }
}
