package com.meferi.datasync.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.meferi.datasync.R;
import com.meferi.datasync.fragment.BarcodeFragment;
import com.meferi.datasync.fragment.BaseFragment;
import com.meferi.datasync.fragment.NfcFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSyncActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "KeyRemapMain";
    public static String type = "";

    private List<Map<String, Object>> mKeyBoardList;
    private ListView mList;

    private String mKeyCode;

    private static final String[] CHANNELS = new String[]{"Barcode", "NFC"};

    MagicIndicator magicIndicator;
    ViewPager viewPager;
    public void onClick(View view) {
    }

    private BaseFragment BarcodeFragment;
    private BaseFragment mNfcFragment;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return false;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (BarcodeFragment == null) {
            BarcodeFragment = new BarcodeFragment();

        }
        fragmentList.add(BarcodeFragment);
        if (mNfcFragment == null) {
            mNfcFragment = new NfcFragment();
        }
        fragmentList.add(mNfcFragment);



        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter); //设置viewpager适配器

    }

    // viewpager适配器
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;

        ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragmentList = list;
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }



    private void initMagicIndicator1() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return CHANNELS.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                // simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setText(CHANNELS[index]);
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                simplePagerTitleView.setNormalColor(Color.parseColor("#ffffff"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ff0000"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;

            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setLineHeight(dip2px(context, 4));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setRoundRadius((dip2px(context, 1) / 2));
                indicator.setColors(  Color.parseColor("#ff0000"));

                return indicator;


            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });


        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager); //绑定指示器与viewpager

    }

    public static int dip2px(Context context, double dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.datasync_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        magicIndicator = findViewById(R.id.magic_indicator);
        viewPager = findViewById(R.id.view_pager);


        actionBar.setTitle(getResources().getString(R.string.app_name));
        initViewPager();
        initMagicIndicator1();


        viewPager.setCurrentItem(0);
    }




    public void onResume() {
        super.onResume();
    }





}
