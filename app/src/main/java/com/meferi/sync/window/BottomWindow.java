package com.meferi.sync.window;

import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.meferi.sync.QrcodeActivity;
import com.meferi.sync.R;
import com.meferi.sync.SettingConfigBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/* renamed from: com.ubx.ustage.ui.window.BottomWindow */
/* loaded from: classes2.dex */
public class BottomWindow extends BottomPopupView {
    @BindView(R.id.config_scan_value)
    SwitchMaterial configScanValue;
    @BindView(R.id.config_system_value)
    SwitchMaterial configSystemValue;
    @BindView(R.id.config_tip)
    TextView configTip;
    @BindView(R.id.config_tip_value)
    EditText configTipValue;
    @BindView(R.id.layout_pop)
    ConstraintLayout layoutPop;
    private Activity mContext;
    @BindView(R.id.pop_window_title)
    TextView popWindowTitle;
    private String title = "";
    private int flagOnClick = 0;
    int USDKStatus = 1;

    @Override // com.lxj.xpopup.core.BottomPopupView, com.lxj.xpopup.core.BasePopupView
    protected int getImplLayoutId() {
        return R.layout.activity_pop;
    }

    public BottomWindow(Activity activity) {
        super(activity);
        this.mContext = activity;
//
//        try {
//            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo("com.ubx.usdk.profile", 0);
//            PrintStream printStream = System.out;
//            printStream.println("---------   " + new Gson().toJson(packageInfo));
//            if (packageInfo == null) {
//                this.USDKStatus = 0;
//            } else if (packageInfo.versionCode >= 118) {
//                this.USDKStatus = 1;
//            } else {
//                this.USDKStatus = -1;
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            this.USDKStatus = 0;
//        }
//        SwitchMaterial switchMaterial = (SwitchMaterial) findViewById(R.id.config_system_value);
//        int i = this.USDKStatus;
//        if (i == 0 || i == -1) {
//            switchMaterial.setChecked(false);
//            switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                }
//            });
//        } else {
//            switchMaterial.setChecked(true);
//        }
//        this.popWindowTitle.setText(this.title);
//        if (this.title.equals(this.mContext.getString(R.string.qrcode_genera))) {
//            this.configTip.setVisibility(4);
//            this.configTipValue.setVisibility(4);
//            return;
//        }
//        this.configTip.setVisibility(0);
//        this.configTip.setText(this.mContext.getString(R.string.config_tip));
//        this.configTipValue.setVisibility(0);
    }

    public void setTitle(String str) {
        this.title = str;
    }

    @Override
    protected void beforeDismiss() {
        super.beforeDismiss();
    }



    public /* synthetic */ void lambda$beforeShow$0$BottomWindow(SwitchMaterial switchMaterial, CompoundButton compoundButton, boolean z) {
        int i = this.USDKStatus;
        if (i == 0) {
            switchMaterial.setChecked(false);
            Toast.makeText(this.mContext, (int) R.string.usdk_uplaod, 0).show();
        } else if (i == -1) {
            switchMaterial.setChecked(false);
            Toast.makeText(this.mContext, (int) R.string.usdk_version, 0).show();
        }
    }

    @Override // com.lxj.xpopup.core.BasePopupView
    protected void onCreate() {
        super.onCreate();
        ButterKnife.bind(this);
        this.configTipValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
    }

    @OnClick({R.id.pop_cancel, R.id.pop_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_cancel:
                this.flagOnClick = 0;
                dismiss();
                return;
            case R.id.pop_confirm:
                if (this.configScanValue.isChecked() || this.configSystemValue.isChecked()) {
                    this.flagOnClick = 1;
                } else {
                    this.flagOnClick = 0;
                    Activity activity = this.mContext;
                    Toast.makeText(activity, activity.getString(R.string.must_be_one), 0).show();
                }
                dismiss();
                return;
            default:
                return;
        }
    }

    @Override // com.lxj.xpopup.core.BasePopupView
    public void onDismiss() {
        Intent intent;
        super.onDismiss();
        if (this.flagOnClick == 1) {
            SettingConfigBean settingConfigBean = new SettingConfigBean();
            if (this.title.equals(this.mContext.getString(R.string.qrcode_genera))) {
                intent = new Intent(this.mContext, QrcodeActivity.class);
            }
            else {
                Intent intent2 = new Intent(this.mContext, QrcodeActivity.class);
//                Intent intent2 = new Intent(this.mContext, UploadOrExpertActivity.class);
                settingConfigBean.setTip(this.configTipValue.getText().toString());
                if (this.title.equals(this.mContext.getString(R.string.upload_to_ums))) {
                    settingConfigBean.setAction(0);
                } else {
                    settingConfigBean.setAction(1);
                }
                intent = intent2;
            }
            settingConfigBean.setScan(this.configScanValue.isChecked());
            settingConfigBean.setSystem(this.configSystemValue.isChecked());

            intent.putExtra("data", settingConfigBean);
            this.mContext.startActivity(intent);
            this.mContext.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            this.flagOnClick = 0;
        }
    }

    @Override // com.lxj.xpopup.core.BasePopupView
    protected int getMaxHeight() {
        float screenHeight;
        float f;
        if (getResources().getConfiguration().orientation == 1) {
            screenHeight = (float) XPopupUtils.getWindowHeight(getContext());
            f = 0.4f;
        } else {
            screenHeight = (float) XPopupUtils.getWindowHeight(getContext());
            f = 0.6f;
        }
        return (int) (screenHeight * f);
    }

    @Override // com.lxj.xpopup.core.BottomPopupView, com.lxj.xpopup.core.BasePopupView
    public void doAfterDismiss() {
        super.doAfterDismiss();
    }
}
