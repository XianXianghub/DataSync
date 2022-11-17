package com.meferi.sync;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/* loaded from: classes2.dex */
public class CustomImageViewerPopup extends CenterPopupView {
    private Bitmap bitmap;
    private Context mContext;

    @Override // com.lxj.xpopup.core.CenterPopupView, com.lxj.xpopup.core.BasePopupView
    protected int getImplLayoutId() {
        return R.layout.window_image;
    }

    public CustomImageViewerPopup(Context context, Bitmap bitmap) {
        super(context);
        this.mContext = context;
        this.bitmap = bitmap;
    }

    @Override // com.lxj.xpopup.core.CenterPopupView, com.lxj.xpopup.core.BasePopupView
    public int getMaxWidth() {
        if (this.popupInfo.maxWidth == 0) {
            return (int) (((float) XPopupUtils.getWindowWidth(getContext())) * 1.0f);
        }
        return this.popupInfo.maxWidth;
    }

    @Override // com.lxj.xpopup.core.BasePopupView
    protected int getPopupHeight() {
        return (int) (((float) XPopupUtils.getWindowHeight(getContext())) * 1.0f);
    }

    @Override // com.lxj.xpopup.core.BasePopupView
    protected void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 21) {
            getPopupImplView().setElevation(10.0f);
        }
        if (this.bindLayoutId == 0) {
//            getPopupImplView().setBackground(XPopupUtils.createBitmapDrawable(Color.parseColor("#FFFFFF"), 0.0f));
        }
        ((ImageView) findViewById(R.id.image_view)).setImageBitmap(this.bitmap);
        findViewById(R.id.layout_image).setOnClickListener(new OnClickListener() { // from class: com.ubx.ustage.base.-$$Lambda$CustomImageViewerPopup$qjUqho0xpJ2xRWa_hxQHLdZAkZU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CustomImageViewerPopup.this.lambda$onCreate$0$CustomImageViewerPopup(view);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0$CustomImageViewerPopup(View view) {
        dismiss();
    }

    @Override // com.lxj.xpopup.core.BasePopupView
    protected void onShow() {
        super.onShow();
        Log.e("tag", "CustomImageViewerPopup onShow");
    }

    @Override // com.lxj.xpopup.core.BasePopupView
    public void onDismiss() {
        super.onDismiss();
        Log.e("tag", "CustomImageViewerPopup onDismiss");
    }
}
