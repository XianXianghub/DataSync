package com.meferi.datasync.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.lxj.xpopup.XPopup;
import com.meferi.datasync.ConfigBean;
import com.meferi.datasync.MainViewModel;
import com.meferi.datasync.R;
import com.meferi.datasync.Utils;
import com.meferi.datasync.window.BottomWindow;
import com.meferi.datasync.window.LoadingWindow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class NfcFragment extends BaseFragment implements View.OnClickListener, NfcAdapter.CreateBeamUrisCallback {
    private final String TAG = "BarcodeFragment";
    HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).create();
    ActivityResultLauncher<Intent> launcher;
    private LoadingWindow loadingPopupView;
    BottomWindow popupView;
    private MainViewModel mainViewModel;
    private NfcAdapter mNfcAdapter;

    public NfcFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Uri[] createBeamUris(NfcEvent nfcEvent) {
        Log.d("sss", "createBeamUris");
        ConfigBean scannerbean = null;
        ConfigBean settingbean = null;
        String data = "";
        String fileName="";
        try {
            scannerbean = Utils.ScannerSettingExpert("scanner");
            data += scannerbean.getConfig();
            fileName = "scanner";
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            settingbean = Utils.ScannerSettingExpert("scanner");
            if(TextUtils.isEmpty(data)){
                data = settingbean.getConfig();
            }else {
                data += "\u001f"+settingbean.getConfig();
            }
            if(TextUtils.isEmpty(fileName)){
                fileName = "setting";
            }else {
                fileName += "_setting";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "DataSync", fileName+".txt");
            File imageFilePath = new File(Environment.getExternalStorageDirectory() + File.separator + "UStage");

            if (!imageFilePath.exists()) {
                imageFilePath.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();


            if (file.exists()){
                Log.d(TAG, "getAbsolutePath="+file.getAbsolutePath());
                Uri[] uris = new Uri[1];
                Uri uri = Uri.parse("file://" + file.getAbsolutePath());
                uris[0] = uri;
                return uris;
            }else {
                Toast.makeText(getActivity(), getString(R.string.text_nfc_exprot_fail), Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new Uri[0];
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nfc_export:


                return;
            case R.id.nfc_import:

                return;
            default:
                return;
        }
    }

    public void showLoading() {
        LoadingWindow loadingWindow = this.loadingPopupView;
        if (loadingWindow == null) {
            LoadingWindow loadingWindow2 = new LoadingWindow(mContext, R.layout.window_loading);
            this.loadingPopupView = loadingWindow2;
            loadingWindow2.setTitle(getString(R.string.setting_syncing));
            new XPopup.Builder(mContext).dismissOnBackPressed(false).dismissOnTouchOutside(false).asCustom(this.loadingPopupView).show();
            return;
        }
        loadingWindow.setTitle(getString(R.string.setting_syncing)).show();
    }

    private void initView(View view) {

        this.mainViewModel = (MainViewModel) new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getIsLoading().observe(this, new Observer<Integer>() { // from class: com.ubx.ustage.ui.home.MainActivity.1
            public void onChanged(Integer num) {
                int intValue = num.intValue();
                if (intValue != 1) {
                    if (intValue == 2 && loadingPopupView != null) {
                        loadingPopupView.setStatus(getString(R.string.sync_fail), false);
                    }
                } else if (loadingPopupView != null) {
                    loadingPopupView.setStatus(getString(R.string.sync_success), true);
                }
            }
        });
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() { // from class: com.ubx.ustage.ui.home.MainActivity.2
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getData() != null) {
                    HmsScan hmsScan = (HmsScan) activityResult.getData().getParcelableExtra(ScanUtil.RESULT);
                    showLoading();
                    System.out.println(hmsScan.originalValue);
                    mainViewModel.postBarcode(hmsScan.originalValue);
                }
            }
        });
        view.findViewById(R.id.nfc_export).setOnClickListener(this);
        view.findViewById(R.id.nfc_import).setOnClickListener(this);
    }

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nfc_datasync, null);
        this.popupView = new BottomWindow(getActivity());
        mContext = getActivity();
        initView(view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mNfcAdapter = mNfcAdapter.getDefaultAdapter(mContext);
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {

        } else {
            IsToSet(getActivity());
            // Toast.makeText(this, "NFC 未打开，请打开NFC!!", Toast.LENGTH_SHORT).show();
        }
        mNfcAdapter.setBeamPushUrisCallback(this, getActivity());
    }

    private void IsToSet(Context activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(getString(R.string.text_nfc_no_open));
        // builder.setTitle("提示");
        builder.setPositiveButton(getString(R.string.main_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.settings.NFC_SETTINGS");
                activity.startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.main_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "Scan test hidden : " + hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
