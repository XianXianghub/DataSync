package com.meferi.datasync;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;

import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

/* loaded from: classes2.dex */
public class SimpleAdapter extends BaseBannerAdapter<ConfigBean> {
    Activity mContext;

    @Override // com.zhpan.bannerview.BaseBannerAdapter
    public int getLayoutId(int i) {
        return R.layout.item_banner_qrcode;
    }

    public SimpleAdapter(Activity activity) {
        this.mContext = activity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void bindData(BaseViewHolder<ConfigBean> baseViewHolder, final ConfigBean configBean, int i, int i2) {
        Glide.with(this.mContext).load(configBean.getConfigQrcode()).into((ImageView) baseViewHolder.findViewById(R.id.banner_image));
        baseViewHolder.setText((int) R.id.config_name_value, configBean.getConfigName());
       // baseViewHolder.setText((int) R.id.config_type_value, configBean.getConfigType());
        baseViewHolder.findViewById(R.id.banner_image).setOnClickListener(new View.OnClickListener() { // from class: com.ubx.ustage.base.SimpleAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                new XPopup.Builder(SimpleAdapter.this.mContext)
                        .dismissOnBackPressed(true)
                        .dismissOnTouchOutside(true)
                        .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                        .hasShadowBg(false)
                        .asCustom(new CustomImageViewerPopup(SimpleAdapter.this.mContext, configBean.getConfigQrcode()))
                        .show();
            }
        });
    }
}
