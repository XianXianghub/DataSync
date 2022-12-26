package com.meferi.sync.window;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.meferi.sync.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

/* renamed from: com.ubx.ustage.ui.window.LoadingWindow */
/* loaded from: classes2.dex */
public class LoadingWindow extends CenterPopupView {
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.loading_view)
    AVLoadingIndicatorView loadingView;
    @BindView(R.id.title)
    TextView title;
    private String titleText = "";
    private boolean isSuccess = false;
    private boolean first = true;
    private Context mcontext;

    public LoadingWindow(Context context, int i) {
        super(context);
        mcontext = context;
        this.bindLayoutId = i;
        addInnerContent();
    }

    @Override // com.lxj.xpopup.core.CenterPopupView, com.lxj.xpopup.core.BasePopupView
    protected int getImplLayoutId() {
        return this.bindLayoutId != 0 ? this.bindLayoutId : R.layout.window_loading;
    }

    @Override // com.lxj.xpopup.core.BasePopupView
    protected void onCreate() {
        super.onCreate();
        ButterKnife.bind(this);
        this.title = (TextView) findViewById(R.id.title);
        if (Build.VERSION.SDK_INT >= 21) {
            getPopupImplView().setElevation(10.0f);
        }
        if (this.bindLayoutId == 0) {
            getPopupImplView().setBackground(XPopupUtils.createBitmapDrawable(mcontext.getResources(), 15,Color.parseColor("#CF000000")));
        }
        setOnTouchListener(new OnTouchListener() { // from class: com.ubx.ustage.ui.window.-$$Lambda$LoadingWindow$Qm6owNpQaJxn7IeHxUiOY8VZmv8
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return LoadingWindow.this.lambda$onCreate$0$LoadingWindow(view, motionEvent);
            }
        });
        setup(0);
    }

    public /* synthetic */ boolean lambda$onCreate$0$LoadingWindow(View view, MotionEvent motionEvent) {
        if (this.imageView.getVisibility() != 0) {
            return false;
        }
        dismiss();
        return false;
    }

    public LoadingWindow setStatus(String str, boolean z) {
        this.titleText = str;
        this.isSuccess = z;
        setup(1);
        return this;
    }

    public LoadingWindow setTitle(String str) {
        this.titleText = str;
        setup(0);
        return this;
    }

    protected void setup(final int i) {
        if (this.title != null) {
            post(new Runnable() { // from class: com.ubx.ustage.ui.window.LoadingWindow.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!LoadingWindow.this.first) {
                        TransitionManager.beginDelayedTransition(LoadingWindow.this.centerPopupContainer, new TransitionSet().setDuration((long) LoadingWindow.this.getAnimationDuration()).addTransition(new Fade()).addTransition(new ChangeBounds()));
                    }
                    LoadingWindow.this.first = false;
                    int i2 = i;
                    if (i2 == 1) {
                        LoadingWindow.this.loadingView.setVisibility(8);
                        LoadingWindow.this.imageView.setVisibility(0);
                        if (LoadingWindow.this.isSuccess) {
                            LoadingWindow.this.imageView.setBackgroundResource(R.drawable.ic_success);
                        } else {
                            LoadingWindow.this.imageView.setBackgroundResource(R.drawable.ic_fail);
                        }
                    } else if (i2 == 0) {
                        LoadingWindow.this.loadingView.setVisibility(0);
                        LoadingWindow.this.imageView.setVisibility(8);
                    }
                    LoadingWindow.this.title.setText(LoadingWindow.this.titleText);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.lxj.xpopup.core.BasePopupView
    public void onDismiss() {
        super.onDismiss();
    }
}
