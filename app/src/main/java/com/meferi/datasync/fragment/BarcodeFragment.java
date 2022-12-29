package com.meferi.datasync.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.huawei.hms.hmsscankit.ScanKitActivity;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.huawei.hms.ml.scan.HmsScanBase;
import com.lxj.xpopup.XPopup;
import com.meferi.datasync.MainViewModel;
import com.meferi.datasync.R;
import com.meferi.datasync.window.BottomWindow;
import com.meferi.datasync.window.LoadingWindow;
import com.meferi.scanner.ScannerManager;
import com.meferi.scanner.tool.Constants;


public class BarcodeFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener{
    private final String TAG = "BarcodeFragment";
    HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE, HmsScan.DATAMATRIX_SCAN_TYPE).create();
    ActivityResultLauncher<Intent> launcher;
    private LoadingWindow loadingPopupView;
    BottomWindow popupView;
    private MainViewModel mainViewModel;
    ScannerManager mScannerManager;
    private MaterialButton ScannerView;
    int mLastScanOutputMode ;
    int MaxLenth = 48 ;
    private String ACTION_SEND_RESULT = "android.intent.action.RECEIVE_SCANDATA_BROADCAST";
    private String EXTRA_SCAN_BARCODE = "android.intent.extra.SCAN_BROADCAST_DATA";

    public BarcodeFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.import_scanner:
                return;
            case R.id.import_camera:
                Intent intent = new Intent(getActivity(), ScanKitActivity.class);
                intent.putExtra(HmsScanBase.SCAN_FORMAT_FLAG, this.options.mode);
                this.launcher.launch(intent);
                getActivity().overridePendingTransition(17432576, 17432577);


                return;
            case R.id.generate_qr:
                PopShow(getString(R.string.qrcode_genera));
//                PopShow(getString(R.string.local_expert));
               break;
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
        mScannerManager = ScannerManager.getInstance();
        try {
            if(mScannerManager.isScannerServiceRunning()) {

                ACTION_SEND_RESULT = mScannerManager.getBroadcastParam(Constants.BROADCAST_CODE_ACTION);
                EXTRA_SCAN_BARCODE = mScannerManager.getBroadcastParam(Constants.BROADCAST_CODE_DATA1);
            }
        } catch (android.os.RemoteException e) {
            e.printStackTrace();
        }
        this.mainViewModel  = (MainViewModel) new ViewModelProvider(this).get(MainViewModel.class);
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
        ScannerView = view.findViewById(R.id.import_scanner);
        view.findViewById(R.id.import_camera).setOnClickListener(this);
       ScannerView.setOnClickListener(this);
       ScannerView.setOnTouchListener(this);
        view.findViewById(R.id.generate_qr).setOnClickListener(this);
    }
    public void PopShow(String str) {
        Log.d("bds","PopShow");
        this.popupView.setTitle(str);
        new XPopup.Builder(getActivity()).autoOpenSoftInput(false).autoFocusEditText(false).moveUpToKeyboard(true).isDestroyOnDismiss(false).asCustom(this.popupView).show();

    }
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_datasync, null);
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
        doResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        doPause();
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
    
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        if (motionEvent.getAction() == 0) {
            ScannerView.setTextColor(getActivity().getColor(R.color.blue_light));
            mScannerManager.keyScan(true);
        } else if (motionEvent.getAction() == 1) {
            ScannerView.setTextColor(getActivity().getColor(R.color.white));
            mScannerManager.keyScan(false);
        }
        return false;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }


    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            Log.d(TAG, "action="+action);
            if (ACTION_SEND_RESULT.equals(action))
            {
                String stringExtra = intent.getStringExtra(EXTRA_SCAN_BARCODE);
                showLoading();
                mainViewModel.postBarcode(stringExtra);
            }
        }
    };
    private void registerReceiver()
    {
        IntentFilter intFilter = new IntentFilter();
        intFilter.addAction(ACTION_SEND_RESULT);
        getActivity().registerReceiver(mResultReceiver, intFilter);
    }
    private void unRegisterReceiver()
    {
        try {
            mContext.unregisterReceiver(mResultReceiver);
        } catch (Exception e) {

        }
    }


    private void doResume() {
        Log.d(TAG, "doResume");
        registerReceiver();
        SystemProperties.set("persist.sys.barcode.limit", "0");
        try {
            mLastScanOutputMode = mScannerManager.getScanResultOutputMode();
            mScannerManager.setScanResultOutputMode(Constants.OUTPUT_MODE_BROADCAST);
            MaxLenth = Integer.valueOf(mScannerManager.getCodeParam("QR", "Maxlen"));
            Log.d(TAG, "MaxLenth="+MaxLenth);
            mScannerManager.setCodeParam("QR", "Maxlen", "500");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        SystemProperties.set("persist.sys.barcode.limit", "1");
    }

    private void doPause() {

        Log.d(TAG, "doPause");
        if(mScannerManager != null) {
            SystemProperties.set("persist.sys.barcode.limit", "0");
            Log.d(TAG, "set MaxLenth="+MaxLenth);
            mScannerManager.setCodeParam("QR", "Maxlen", String.valueOf(MaxLenth));
            SystemProperties.set("persist.sys.barcode.limit", "1");
            mScannerManager.setScanResultOutputMode(mLastScanOutputMode);
        }
    }


}
