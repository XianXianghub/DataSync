package com.meferi.sync;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.lxj.xpopup.XPopup;
import com.meferi.sync.window.BottomWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/* renamed from: com.ubx.ustage.ui.expert.ExpertActivity */
/* loaded from: classes2.dex */
public class ExpertActivity extends Activity implements View.OnClickListener{

    BottomWindow popupView;
    @BindView(R.id.return_view)
    MaterialButton returnView;
    @BindView(R.id.upload_to_ums)
    MaterialButton uploadToUms;

    @Override // com.ubx.ustage.base.BaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_expert);
        this.popupView = new BottomWindow(this);
        ButterKnife.bind(this);
//        requestPermission();
//        if (XXPermissions.isGranted(this, Permission.MANAGE_EXTERNAL_STORAGE)) {
//            Utils.createDir("UStage");
//        }
//        ButterKnife.bind(this);
//        int i = 0;
//        try {
//            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.urovo.uhome", 0);
//            PrintStream printStream = System.out;
//            printStream.println("---------   " + new Gson().toJson(packageInfo));
//            if (packageInfo == null) {
//                this.umsStatus = 0;
//            } else if (packageInfo.versionCode >= 272) {
//                this.umsStatus = 1;
//            } else {
//                this.umsStatus = -1;
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            this.umsStatus = i;
//        }
//        i = this.umsStatus;
//        if (i == 0 || i == -1) {
//            this.uploadToUms.setBackgroundColor(ContextCompat.getColor(this, C1839R.C1841color.line_gray_half));
//        }
    }

    @OnClick({R.id.qrcode_generation, R.id.expert_to_local, R.id.upload_to_ums, R.id.return_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expert_to_local:
                PopShow(getString(R.string.local_expert));
                return;
            case R.id.qrcode_generation:
                PopShow(getString(R.string.qrcode_genera));
                return;
            case R.id.return_view:
                finish();
//                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                return;
            case R.id.upload_to_ums:
//                int i = this.umsStatus;
//                if (i == 0) {
//                    Toast.makeText(this, (int) R.string.no_ums, 0).show();
//                    return;
//                } else if (i == -1) {
//                    Toast.makeText(this, (int) R.string.ums_upload, 0).show();
//                    return;
//                } else {
//                    PopShow(getString(R.string.upload_to_ums));
//                    return;
//                }
            default:
                return;
        }
    }

    public void PopShow(String str) {
        Log.d("bds","PopShow");
        this.popupView.setTitle(str);
        new XPopup.Builder(this).autoOpenSoftInput(false).autoFocusEditText(false).moveUpToKeyboard(true).isDestroyOnDismiss(false).asCustom(this.popupView).show();

    }


}
