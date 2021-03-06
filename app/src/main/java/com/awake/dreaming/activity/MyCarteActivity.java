package com.awake.dreaming.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awake.dreaming.R;
import com.awake.dreaming.fragment.BusinessCardFragment;
import com.awake.dreaming.fragment.MyCardFragment;
import com.awake.dreaming.utils.StatusBarUtils;
import com.awake.dreaming.widget.TopBar1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdesheng on 2017/10/10 0010.
 */

public class MyCarteActivity extends BaseActivity {

    private List<Fragment> fragments;

    private TextView tvMyCard;
    private TextView tvBusinessCard;
    private TextView tvMyIndicator;
    private TextView tvBusinessIndicator;

    private void initView() {
        TopBar1 topBar1 = (TopBar1) findViewById(R.id.my_carte_topBar);
        topBar1.titleBack.setOnClickListener(this);

        tvMyCard = (TextView) findViewById(R.id.tv_my_card);
        tvBusinessCard = (TextView) findViewById(R.id.tv_business_card);
        tvMyIndicator = (TextView) findViewById(R.id.tv_my_indicator);
        tvBusinessIndicator = (TextView) findViewById(R.id.tv_business_indicator);
        tvMyIndicator.setLayoutParams(new LinearLayout.LayoutParams(tvMyCard.getWidth(), 8));
        tvMyIndicator.setGravity(Gravity.CENTER_HORIZONTAL);

        fragments = new ArrayList<>();
        fragments.add(new MyCardFragment());
        fragments.add(new BusinessCardFragment());

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_card);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled
                    (int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0){
                    tvMyCard.setTextColor(Color.parseColor("#50cdff"));
                    tvMyIndicator.setBackgroundColor(Color.parseColor("#50cdff"));
                    tvBusinessCard.setTextColor(Color.parseColor("#000000"));
                    tvBusinessIndicator.setBackgroundColor(Color.parseColor("#ffffff"));

                    tvMyIndicator.setLayoutParams
                            (new LinearLayout.LayoutParams(tvMyCard.getWidth(), 8));
                    tvMyIndicator.setGravity(Gravity.CENTER_HORIZONTAL);
                }else if (position == 1){
                    tvMyCard.setTextColor(Color.parseColor("#000000"));
                    tvMyIndicator.setBackgroundColor(Color.parseColor("#ffffff"));
                    tvBusinessCard.setTextColor(Color.parseColor("#50cdff"));
                    tvBusinessIndicator.setBackgroundColor(Color.parseColor("#50cdff"));
                    tvBusinessIndicator.setLayoutParams
                            (new LinearLayout.LayoutParams(tvMyCard.getWidth(),8));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("TAG", "position: " + position );
                Log.e("TAG", "positionOffset: " + positionOffset );
                Log.e("TAG", "positionOffsetPixels: " + positionOffsetPixels );
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(0, R.anim.animation_x_off);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_my_carte;
    }

    @Override
    protected void setLayout() {
        StatusBarUtils.transparentStatusBar(this);
        initView();
    }

    @Override
    protected void onViewClick(View view) {
        switch (view.getId()){
            case R.id.topbar_back:{
                finish();
                break;
            }
        }
    }

    private class PageAdapter extends FragmentPagerAdapter{

        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
