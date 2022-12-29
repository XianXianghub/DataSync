package com.meferi.datasync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.huawei.hms.hmsscankit.ScanKitActivity;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.huawei.hms.ml.scan.HmsScanBase;
import com.lxj.xpopup.XPopup;
import com.meferi.scanner.ScannerManager;
import com.meferi.scanner.tool.Constants;
import com.meferi.datasync.window.LoadingWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class MainActivity extends BaseActivity implements  View.OnClickListener{
    ScannerManager mScannerManager;
    int mLastScanOutputMode ;
    int MaxLenth = 48 ;
    private String ACTION_SEND_RESULT = "android.intent.action.RECEIVE_SCANDATA_BROADCAST";
    private String EXTRA_SCAN_BARCODE = "android.intent.extra.SCAN_BROADCAST_DATA";
    private String EXTRA_SCAN_BARCODE_TYPE = "BROADCAST_CODE_TYPE";
    private String EXTRA_SCAN_BARCODE_TYPE_NAME = "BROADCAST_CODE_TYPE_NAME";
    private LoadingWindow loadingPopupView;
    private MainViewModel mainViewModel;
    ActivityResultLauncher<Intent> launcher;
    @BindView(R.id.camera_scan)
    TextView cameraScan;


    HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).create();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mScannerManager = ScannerManager.getInstance();
        try {
            if(mScannerManager.isScannerServiceRunning()) {
                Log.d("bds", "ACTION_SEND_RESULT="+ mScannerManager.getBroadcastParam(Constants.BROADCAST_CODE_ACTION));
                ACTION_SEND_RESULT = mScannerManager.getBroadcastParam(Constants.BROADCAST_CODE_ACTION);
                EXTRA_SCAN_BARCODE = mScannerManager.getBroadcastParam(Constants.BROADCAST_CODE_DATA1);
                EXTRA_SCAN_BARCODE_TYPE = mScannerManager.getBroadcastParam(Constants.BROADCAST_CODE_TYPE);
                EXTRA_SCAN_BARCODE_TYPE_NAME = mScannerManager.getBroadcastParam(Constants.BROADCAST_CODE_TYPE_NAME);
            }
        } catch (android.os.RemoteException e) {
            e.printStackTrace();
        }

        this.mainViewModel  = (MainViewModel) new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getIsLoading().observe(this, new Observer<Integer>() { // from class: com.ubx.ustage.ui.home.MainActivity.1
            public void onChanged(Integer num) {
                int intValue = num.intValue();
                if (intValue != 1) {
                    if (intValue == 2 && MainActivity.this.loadingPopupView != null) {
                        MainActivity.this.loadingPopupView.setStatus(MainActivity.this.getString(R.string.sync_fail), false);
                    }
                } else if (MainActivity.this.loadingPopupView != null) {
                    MainActivity.this.loadingPopupView.setStatus(MainActivity.this.getString(R.string.sync_success), true);
                }
            }
        });
        this.launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() { // from class: com.ubx.ustage.ui.home.MainActivity.2
            public void onActivityResult(ActivityResult activityResult) {
                if (activityResult.getData() != null) {
                    HmsScan hmsScan = (HmsScan) activityResult.getData().getParcelableExtra(ScanUtil.RESULT);
                    MainActivity.this.showLoading();
                    System.out.println(hmsScan.originalValue);
                    MainActivity.this.mainViewModel.postBarcode(hmsScan.originalValue);
                }
            }
        });
        registerReceiver();

    }
    public void showLoading() {
        LoadingWindow loadingWindow = this.loadingPopupView;
        if (loadingWindow == null) {
            LoadingWindow loadingWindow2 = new LoadingWindow(this, R.layout.window_loading);
            this.loadingPopupView = loadingWindow2;
            loadingWindow2.setTitle(getString(R.string.setting_syncing));
            new XPopup.Builder(this).dismissOnBackPressed(false).dismissOnTouchOutside(false).asCustom(this.loadingPopupView).show();
            return;
        }
        loadingWindow.setTitle(getString(R.string.setting_syncing)).show();
    }

    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (ACTION_SEND_RESULT.equals(action))
            {
                String stringExtra = intent.getStringExtra(EXTRA_SCAN_BARCODE);
                MainActivity.this.showLoading();
                MainActivity.this.mainViewModel.postBarcode(stringExtra);
            }
        }
    };
    private void registerReceiver()
    {
        IntentFilter intFilter = new IntentFilter();
        intFilter.addAction(ACTION_SEND_RESULT);
        registerReceiver(mResultReceiver, intFilter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }
    private void unRegisterReceiver()
    {
        try {
            unregisterReceiver(mResultReceiver);
        } catch (Exception e) {

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SystemProperties.set("persist.sys.barcode.limit", "0");
        try {
            mLastScanOutputMode = mScannerManager.getScanResultOutputMode();
            mScannerManager.setScanResultOutputMode(Constants.OUTPUT_MODE_BROADCAST);
            MaxLenth = Integer.valueOf(mScannerManager.getCodeParam("QR", "Maxlen"));
            mScannerManager.setCodeParam("QR", "Maxlen", "500");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        SystemProperties.set("persist.sys.barcode.limit", "1");
    }
    @Override
    protected void onPause() {
        super.onPause();
        SystemProperties.set("persist.sys.barcode.limit", "0");
        mScannerManager.setCodeParam("QR", "Maxlen", String.valueOf(MaxLenth));
        SystemProperties.set("persist.sys.barcode.limit", "1");
        mScannerManager.setScanResultOutputMode(mLastScanOutputMode);
    }

    @OnTouch({R.id.btn_scan, R.id.camera_scan, R.id.setting})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        if (id != R.id.btn_scan) {
            Log.d("bds", "333");
//            if (id != R.id.camera_scan) {
//                if (id == R.id.setting) {
//                    if (motionEvent.getAction() == 0) {
//                        this.setting.setIconTintResource(C1510R.C1512color.line_gray_half);
//                    } else if (motionEvent.getAction() == 1) {
//                        this.setting.setIconTintResource(C1510R.C1512color.black);
//                    }
//                }
//            } else if (motionEvent.getAction() == 0) {
//                this.cameraScan.setTextColor(getColor(C1510R.C1512color.blue_light));
//            } else if (motionEvent.getAction() == 1) {
//                this.cameraScan.setTextColor(getColor(C1510R.C1512color.blue));
//            }
        } else if (motionEvent.getAction() == 0) {
            this.cameraScan.setTextColor(getColor(R.color.blue_light));
            mScannerManager.keyScan(true);
        } else if (motionEvent.getAction() == 1) {
            this.cameraScan.setTextColor(getColor(R.color.blue));
            mScannerManager.keyScan(false);
        }
        return false;
    }
    private int REQUEST_CODE_SCAN = 10001;

    @OnClick({R.id.camera_scan, R.id.view_expert, R.id.setting})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_expert:
                startActivity(new Intent(this, ExpertActivity.class));
                break;
            case R.id.btn_scan:
                break;
            case R.id.camera_scan:
                Intent intent = new Intent(this, ScanKitActivity.class);
                intent.putExtra(HmsScanBase.SCAN_FORMAT_FLAG, this.options.mode);
                this.launcher.launch(intent);
                overridePendingTransition(17432576, 17432577);
                break;
            case R.id.setting:
                break;
        }


    }

}