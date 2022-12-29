package com.meferi.datasync;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.meferi.datasync.qrcode.QrcodeViewModel;
import com.meferi.datasync.window.LoadingWindow;
import com.zhpan.bannerview.BannerViewPager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


public class QrcodeActivity extends BaseActivity {
    private String TAG = "QrcodeActivity";
    @BindView(R.id.banner_qrcode)
    public BannerViewPager bannerViewPager;
    private final List<ConfigBean> configBeans = new ArrayList();
    @BindView(R.id.pages)
    TextView pages;
    QrcodeViewModel qrcodeViewModel;
    @BindView(R.id.return_view)
    MaterialButton returnView;

    @BindView(R.id.save_local)
    Button bt_save;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    loadingPopupView.setStatus("Export file to " + msg.obj, true);
                    break;
                case 1001:
                    loadingPopupView.setStatus("Export file failed", false);
                    break;
            }
        }
    };
    private LoadingWindow loadingPopupView;

    public void showLoading() {
        LoadingWindow loadingWindow = this.loadingPopupView;
        if (loadingWindow == null) {
            LoadingWindow loadingWindow2 = new LoadingWindow(this, R.layout.window_loading);
            this.loadingPopupView = loadingWindow2;
            loadingWindow2.setTitle("Saving...");
            new XPopup.Builder(this).dismissOnBackPressed(false).dismissOnTouchOutside(false).asCustom(this.loadingPopupView).show();
            return;
        }
        loadingWindow.setTitle("Saving...").show();
    }

    @Override
    // com.ubx.ustage.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        super.onCreate(bundle);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        this.qrcodeViewModel = (QrcodeViewModel) new ViewModelProvider(this).get(QrcodeViewModel.class);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        init();
    }

    public void expertToSdcard() {
        String str;
        String str2;
        String str3;
        try {
            List<ConfigBean> value = this.qrcodeViewModel.getConfigs().getValue();
            String str4 = "";

            if (value.size() > 1) {
                str3 = "scanner_system_" + MainApplication.deviceManagerPlus.getDeviceId();
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
//            jSONObject.put("type", this.setTypeMap.get(str2));
            jSONObject.put("config", str);

            File imageFilePath = new File(Environment.getExternalStorageDirectory() + File.separator + "DataSync");

            if (!imageFilePath.exists()) {
                imageFilePath.mkdirs();
            }
            String path = Environment.getExternalStorageDirectory() + File.separator + "DataSync/" + str4 + ".pdf";

            Utils.saveBitmapForPdf(MainApplication.getInstance(), value, Environment.getExternalStorageDirectory() + File.separator + "DataSync", str4 + ".pdf");
            Message msg = new Message();
            msg.obj = path;
            msg.what = 1000;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            Message msg = new Message();
            msg.what = 1001;
            mHandler.sendMessage(msg);
            Log.d(TAG, "本地导出设置失败:" + e.toString());
        }
    }

    public void init() {
        this.pages.setText("");
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        expertToSdcard();
                    }
                }).start();

            }
        });
        this.bannerViewPager.setLifecycleRegistry(getLifecycle())
                .setAdapter(new SimpleAdapter(this))
                .setPageStyle(8)
                .setAutoPlay(false)
                .setCanLoop(false)
//                .setIndicatorView(getVectorDrawableIndicator())
                .setIndicatorVisibility(0)
                .create();
        this.bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // from class: com.ubx.ustage.ui.qrcode.QrcodeActivity.1
            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageSelected(int i) {
                super.onPageSelected(i);
                TextView textView = QrcodeActivity.this.pages;
                textView.setText((i + 1) + "/" +
                        QrcodeActivity.this.bannerViewPager.getAdapter().getItemCount() +
                        QrcodeActivity.this.getString(R.string.page_after));
            }
        });
        this.qrcodeViewModel.getConfigs();
        this.qrcodeViewModel.ConfigExpert(getIntent());
        this.qrcodeViewModel.isSuccess().observe(this, new Observer() { // from class: com.ubx.ustage.ui.qrcode.-$$Lambda$QrcodeActivity$gNQ52lKmYQDICRVX-FuTEAuZnxQ
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                QrcodeActivity.this.showPageView((Boolean) obj);
            }
        });
    }

    public void showPageView(Boolean bool) {
        if (bool.booleanValue()) {
            this.bannerViewPager.refreshData(this.qrcodeViewModel.getConfigs().getValue());
            this.bannerViewPager.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_in));
            pages.setText("1/" + this.qrcodeViewModel.getConfigs().getValue().size() + getString(R.string.page_after));
            return;
        }
        new XPopup.Builder(this).isDestroyOnDismiss(true).dismissOnBackPressed(false).asConfirm(getString(R.string.abnormal), getString(R.string.expert_fail), getString(R.string.cancel), getString(R.string.retry), new OnConfirmListener() { // from class: com.ubx.ustage.ui.qrcode.-$$Lambda$QrcodeActivity$p7ZiC27dV98OwWFbeUdJwpBDPJU
            @Override // com.lxj.xpopup.interfaces.OnConfirmListener
            public final void onConfirm() {
                QrcodeActivity.this.ConfigExpert();
            }
        }, new OnCancelListener() {
            @Override // com.lxj.xpopup.interfaces.OnCancelListener
            public final void onCancel() {
                finish();
            }
        }, false).show();
    }

    public void ConfigExpert() {
        this.qrcodeViewModel.ConfigExpert(getIntent());
    }


    @OnTouch({R.id.return_view})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.returnView.setIconTintResource(R.color.line_gray_half);
            return false;
        } else if (motionEvent.getAction() != 1) {
            return false;
        } else {
            this.returnView.setIconTintResource(R.color.white_light);
            return false;
        }
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @OnClick({R.id.return_view})
    public void onClick(View view) {
        finish();
    }


}
