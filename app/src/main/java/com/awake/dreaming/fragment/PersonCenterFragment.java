package com.awake.dreaming.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.awake.dreaming.R;
import com.awake.dreaming.activity.AboutUsActivity;
import com.awake.dreaming.activity.CollectionActivity;
import com.awake.dreaming.activity.FeedBackActivity;
import com.awake.dreaming.activity.FootPrintsActivity;
import com.awake.dreaming.activity.HasMemoActivity;
import com.awake.dreaming.activity.HelpCenterActivity;
import com.awake.dreaming.activity.NoMemoActivity;
import com.awake.dreaming.activity.MyCarteActivity;

/**
 * Created by 德胜 on 2017/10/13.
 */

public class PersonCenterFragment extends BaseFragment {

    private final String TAG = "PersonCenterFragment";
    private boolean isHave = true;

    @Override
    protected int getLayout() {
        return R.layout.fragment_person_center;
    }

    @Override
    protected void setLayout() {
        initView();
    }

    @Override
    protected void onViewClick(View view) {

        switch (view.getId()){
            case R.id.ll_collection:{
                Intent intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_card_holder:{
                Intent intent = new Intent(getActivity(), MyCarteActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_memo:{
                if (isHave) {
                    Intent intent = new Intent(getActivity(), HasMemoActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), NoMemoActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.ll_foot_print:{
                Intent intent = new Intent(getActivity(), FootPrintsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_help_center:{
                Intent intent = new Intent(getActivity(), HelpCenterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_feed_back:{
                Intent intent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_about_us:{
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void initView() {
        LinearLayout collection = (LinearLayout) getActivity().findViewById(R.id.ll_collection);
        LinearLayout cardHolder = (LinearLayout) getActivity().findViewById(R.id.ll_card_holder);
        LinearLayout memo = (LinearLayout) getActivity().findViewById(R.id.ll_memo);
        LinearLayout footPrint = (LinearLayout) getActivity().findViewById(R.id.ll_foot_print);
        LinearLayout helpCenter = (LinearLayout) getActivity().findViewById(R.id.ll_help_center);
        LinearLayout feedback = (LinearLayout) getActivity().findViewById(R.id.ll_feed_back);
        LinearLayout aboutUs = (LinearLayout) getActivity().findViewById(R.id.ll_about_us);

        collection.setOnClickListener(this);
        cardHolder.setOnClickListener(this);
        memo.setOnClickListener(this);
        footPrint.setOnClickListener(this);
        helpCenter.setOnClickListener(this);
        feedback.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
    }
}