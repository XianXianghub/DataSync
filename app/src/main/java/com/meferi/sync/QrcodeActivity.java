package com.meferi.sync;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.meferi.sync.qrcode.QrcodeViewModel;
import com.zhpan.bannerview.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;


public class QrcodeActivity extends BaseActivity {
    @BindView(R.id.banner_qrcode)
    public BannerViewPager bannerViewPager;
    private final List<ConfigBean> configBeans = new ArrayList();
    @BindView(R.id.pages)
    TextView pages;
    QrcodeViewModel qrcodeViewModel;
    @BindView(R.id.return_view)
    MaterialButton returnView;
    @BindView(R.id.view_pop)
    ConstraintLayout viewPop;

    @Override // com.ubx.ustage.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if( getActionBar() != null) {
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

    public void init() {
        this.pages.setText("");
        this.viewPop.bringToFront();
        this.viewPop.setVisibility(0);
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
                textView.setText(QrcodeActivity.this.getString(R.string.page_pre) + (i + 1) + "/" +
                        QrcodeActivity.this.bannerViewPager.getAdapter().getItemCount() +
                        QrcodeActivity.this.getString(R.string.page_after));
            }
        });
        this.qrcodeViewModel.getConfigs();
        this.qrcodeViewModel.ConfigExpert(getIntent());
        this.qrcodeViewModel.isSuccess().observe(this, new Observer() { // from class: com.ubx.ustage.ui.qrcode.-$$Lambda$QrcodeActivity$gNQ52lKmYQDICRVX-FuTEAuZnxQ
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                QrcodeActivity.this.lambda$init$2$QrcodeActivity((Boolean) obj);
            }
        });
    }

    public /* synthetic */ void lambda$init$2$QrcodeActivity(Boolean bool) {
        if (bool.booleanValue()) {
            this.bannerViewPager.refreshData(this.qrcodeViewModel.getConfigs().getValue());
            this.viewPop.setVisibility(8);
            this.bannerViewPager.startAnimation(AnimationUtils.loadAnimation(this, R.anim.bottom_in));
            TextView textView = this.pages;
            textView.setText(getString(R.string.page_pre) + "1/" + this.bannerViewPager.getAdapter().getItemCount() + getString(R.string.page_after));
            return;
        }
        new XPopup.Builder(this).isDestroyOnDismiss(true).dismissOnBackPressed(false).asConfirm(getString(R.string.abnormal), getString(R.string.expert_fail), getString(R.string.cancel), getString(R.string.retry), new OnConfirmListener() { // from class: com.ubx.ustage.ui.qrcode.-$$Lambda$QrcodeActivity$p7ZiC27dV98OwWFbeUdJwpBDPJU
            @Override // com.lxj.xpopup.interfaces.OnConfirmListener
            public final void onConfirm() {
                QrcodeActivity.this.lambda$init$0$QrcodeActivity();
            }
        }, new OnCancelListener() { // from class: com.ubx.ustage.ui.qrcode.-$$Lambda$QrcodeActivity$6ikgLIkeizYtU9Ne32O0dlOmDe8
            @Override // com.lxj.xpopup.interfaces.OnCancelListener
            public final void onCancel() {
                QrcodeActivity.this.lambda$init$1$QrcodeActivity();
            }
        }, false).show();
    }

    public /* synthetic */ void lambda$init$0$QrcodeActivity() {
        this.qrcodeViewModel.ConfigExpert(getIntent());
    }

    public /* synthetic */ void lambda$init$1$QrcodeActivity() {
        finish();
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

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @OnClick({R.id.return_view})
    public void onClick(View view) {
        finish();
    }

//    private IIndicator getVectorDrawableIndicator() {
//        if (getResources().getConfiguration().orientation == 2) {
//            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.dp6);
//            return new DrawableIndicator(this).setIndicatorGap(getResources().getDimensionPixelOffset(R.dimen.dp3)).setIndicatorDrawable(R.C1842drawable.banner_indicator_nornal, R.C1842drawable.banner_indicator_focus).setIndicatorSize(dimensionPixelOffset, dimensionPixelOffset, getResources().getDimensionPixelOffset(R.dimen.dp12), dimensionPixelOffset);
//        }
//        int dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R.dimen.dp12);
//        return new DrawableIndicator(this).setIndicatorGap(getResources().getDimensionPixelOffset(R.dimen.dp3)).setIndicatorDrawable(R.C1842drawable.banner_indicator_nornal, R.C1842drawable.banner_indicator_focus).setIndicatorSize(dimensionPixelOffset2, dimensionPixelOffset2, getResources().getDimensionPixelOffset(R.dimen.dp24), dimensionPixelOffset2);
//    }
}
