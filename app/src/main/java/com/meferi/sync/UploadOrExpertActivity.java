package com.meferi.sync;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/* renamed from: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertActivity */
/* loaded from: classes2.dex */
public class UploadOrExpertActivity extends BaseActivity {
    @BindView(R.id.file_view)
    MaterialButton fileView;
    @BindView(R.id.result_image)
    ImageView resultImage;
    @BindView(R.id.return_view)
    MaterialButton returnView;
    @BindView(R.id.tip_load)
    TextView tipLoad;
    @BindView(R.id.tip_result)
    TextView tipResult;
    private UploadOrExpertViewModel upOrExViewModel;
    @BindView(R.id.upload_again)
    Button uploadAgain;
    @BindView(R.id.upload_title)
    TextView uploadTitle;
    @BindView(R.id.view_pop)
    ConstraintLayout viewPop;
    private String tipSuccessText = "";
    private String tipFailText = "";
    private String tipLoadText = "";
    private String titleText = "";
    private String btnAgainText = "";

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ubx.ustage.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_upload_or_expert);
        ButterKnife.bind(this);
        this.upOrExViewModel = (UploadOrExpertViewModel) new ViewModelProvider(this).get(UploadOrExpertViewModel.class);

        init();
        initView();

    }

    public void init() {
        this.viewPop.bringToFront();
        this.viewPop.setVisibility(0);
        this.upOrExViewModel.getConfigs();
        this.upOrExViewModel.ConfigExpert(getIntent());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initView() {
        if (this.upOrExViewModel.getSettingConfig(getIntent()).getValue().getAction() == 0) {
            this.btnAgainText = getString(R.string.upload_again);
            this.tipSuccessText = getString(R.string.config_upload_to_ums_success);
            this.tipFailText = getString(R.string.config_upload_to_ums_fail);
            this.titleText = getString(R.string.upload_to_ums);
            this.tipLoadText = getString(R.string.config_uploading);
        } else {
            this.uploadAgain.setVisibility(4);
            this.btnAgainText = getString(R.string.expert_again);
            this.tipSuccessText = getString(R.string.config_expert_to_sdcard_success);
            this.tipFailText = getString(R.string.config_expert_to_sdcard_fail);
            this.titleText = getString(R.string.local_expert);
            this.tipLoadText = getString(R.string.local_experting);
        }
        this.uploadTitle.setText(this.titleText);
        this.uploadAgain.setText(this.btnAgainText);
        this.tipLoad.setText(this.tipLoadText);
        this.upOrExViewModel.isSuccess().observe(this, new Observer<Integer>() { // from class: com.ubx.ustage.ui.uploadorexpert.UploadOrExpertActivity.2
            public void onChanged(Integer num) {
                if (num.intValue() == 2) {
                    UploadOrExpertActivity.this.viewPop.setVisibility(8);
                    UploadOrExpertActivity.this.resultImage.setBackground(UploadOrExpertActivity.this.getDrawable(R.drawable.ic_success));
                    TextView textView = UploadOrExpertActivity.this.tipResult;
                    textView.setText(UploadOrExpertActivity.this.tipSuccessText + "\n" + UploadOrExpertActivity.this.getString(R.string.setting_name_is) + " " + UploadOrExpertActivity.this.upOrExViewModel.getConfigName().getValue());
                } else if (num.intValue() == 0) {
                    UploadOrExpertActivity.this.viewPop.setVisibility(8);
                    UploadOrExpertActivity.this.resultImage.setBackground(UploadOrExpertActivity.this.getDrawable(R.drawable.ic_fail));
                    TextView textView2 = UploadOrExpertActivity.this.tipResult;
                    textView2.setText(UploadOrExpertActivity.this.tipFailText + "\n" + UploadOrExpertActivity.this.upOrExViewModel.getConfigName().getValue());
                } else if (num.intValue() == 1) {
                    UploadOrExpertActivity.this.upOrExViewModel.UploadOrExpert(UploadOrExpertActivity.this);
                }
            }
        });
    }

    @OnClick({R.id.cancel_button, R.id.return_view, R.id.upload_again, R.id.file_view})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.cancel_button || id == R.id.return_view) {
            finish();
        } else if (id == R.id.upload_again) {
            if (this.uploadAgain.getText().toString().equals(getString(R.string.upload_again))) {
                init();
                return;
            }
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.cyanogenmod.filemanager", "com.cyanogenmod.filemanager.activities.NavigationActivity"));
            intent.addFlags(268435456);
            startActivity(intent);
        }
    }

    @OnTouch({R.id.return_view})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.returnView.setIconTintResource(R.color.line_gray_half);
            return false;
        } else if (motionEvent.getAction() != 1) {
            return false;
        } else {
            this.returnView.setIconTintResource(R.color.black);
            return false;
        }
    }
}
