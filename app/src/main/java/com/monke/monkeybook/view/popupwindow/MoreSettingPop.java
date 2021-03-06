//Copyright (c) 2017. 章钦豪. All rights reserved.
package com.monke.monkeybook.view.popupwindow;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.kyleduo.switchbutton.SwitchButton;
import com.monke.monkeybook.R;
import com.monke.monkeybook.help.ReadBookControl;
import com.monke.monkeybook.help.RxBusTag;
import com.monke.monkeybook.utils.barUtil.ImmersionBar;
import com.monke.monkeybook.view.activity.ReadBookActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoreSettingPop extends PopupWindow {

    @BindView(R.id.sb_click_all_next)
    Switch sbClickAllNext;
    @BindView(R.id.sb_click)
    Switch sbClick;
    @BindView(R.id.sb_show_title)
    Switch sbShowTitle;
    @BindView(R.id.sb_showTimeBattery)
    Switch sbShowTimeBattery;
    @BindView(R.id.sb_hideStatusBar)
    Switch sbHideStatusBar;
    @BindView(R.id.ll_hideStatusBar)
    LinearLayout llHideStatusBar;
    @BindView(R.id.ll_showTimeBattery)
    LinearLayout llShowTimeBattery;
    @BindView(R.id.sb_hideNavigationBar)
    Switch sbHideNavigationBar;
    @BindView(R.id.ll_hideNavigationBar)
    LinearLayout llHideNavigationBar;
    @BindView(R.id.sb_showLine)
    Switch sbShowLine;
    @BindView(R.id.sbImmersionBar)
    Switch sbImmersionBar;
    @BindView(R.id.llImmersionBar)
    LinearLayout llImmersionBar;
    @BindView(R.id.llScreenTimeOut)
    LinearLayout llScreenTimeOut;
    @BindView(R.id.tv_screen_time_out)
    TextView tvScreenTimeOut;
    @BindView(R.id.tvJFConvert)
    TextView tvJFConvert;
    @BindView(R.id.llJFConvert)
    LinearLayout llJFConvert;
    @BindView(R.id.tv_screen_direction)
    TextView tvScreenDirection;
    @BindView(R.id.ll_screen_direction)
    LinearLayout llScreenDirection;
    @BindView(R.id.sw_volume_next_page)
    Switch swVolumeNextPage;
    @BindView(R.id.sw_read_aloud_key)
    Switch swReadAloudKey;
    @BindView(R.id.ll_read_aloud_key)
    LinearLayout llReadAloudKey;

    private ReadBookActivity activity;
    private ReadBookControl readBookControl = ReadBookControl.getInstance();

    public interface OnChangeProListener {
        void keepScreenOnChange(int keepScreenOn);

        void refresh();

        void recreate();
    }

    private OnChangeProListener changeProListener;

    @SuppressLint("InflateParams")
    public MoreSettingPop(ReadBookActivity readBookActivity, @NonNull OnChangeProListener changeProListener) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.activity = readBookActivity;
        this.changeProListener = changeProListener;

        View view = LayoutInflater.from(activity).inflate(R.layout.pop_more_setting, null);
        ImmersionBar.navigationBarPadding(activity, view);
        this.setContentView(view);
        ButterKnife.bind(this, view);
        initData();
        bindEvent();

        setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.shape_pop_checkaddshelf_bg));
        setFocusable(true);
        setTouchable(true);
        setClippingEnabled(false);
        setAnimationStyle(R.style.anim_pop_windowlight);
    }

    private void bindEvent() {
        sbHideStatusBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setHideStatusBar(isChecked);
                changeProListener.refresh();
                upView();
            }
        });
        sbHideNavigationBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setHideNavigationBar(isChecked);
                initData();
                changeProListener.recreate();
            }
        });
        swVolumeNextPage.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                readBookControl.setCanKeyTurn(b);
                upView();
            }
        });
        swReadAloudKey.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isPressed()) {
                readBookControl.setAloudCanKeyTurn(b);
            }
        });
        sbClick.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setCanClickTurn(isChecked);
            }
        });
        sbClickAllNext.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setClickAllNext(isChecked);
            }
        });

        sbShowTitle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setShowTitle(isChecked);
                readBookControl.setLineChange(System.currentTimeMillis());
                changeProListener.refresh();
            }
        });
        sbShowTimeBattery.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setShowTimeBattery(isChecked);
                readBookControl.setLineChange(System.currentTimeMillis());
                changeProListener.refresh();
            }
        });
        sbShowLine.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setShowLine(isChecked);
                readBookControl.setLineChange(System.currentTimeMillis());
                changeProListener.refresh();
            }
        });
        sbImmersionBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                readBookControl.setImmersionStatusBar(isChecked);
                readBookControl.setLineChange(System.currentTimeMillis());
                RxBus.get().post(RxBusTag.IMMERSION_CHANGE, true);
                changeProListener.refresh();
            }
        });
        llScreenTimeOut.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.keep_light))
                    .setSingleChoiceItems(activity.getResources().getStringArray(R.array.screen_time_out), readBookControl.getScreenTimeOut(), (dialogInterface, i) -> {
                        readBookControl.setScreenTimeOut(i);
                        upScreenTimeOut(i);
                        changeProListener.keepScreenOnChange(i);
                        dialogInterface.dismiss();
                    })
                    .create();
            dialog.show();
        });
        llJFConvert.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.jf_convert))
                    .setSingleChoiceItems(activity.getResources().getStringArray(R.array.convert_s), readBookControl.getTextConvert(), (dialogInterface, i) -> {
                        readBookControl.setTextConvert(i);
                        upFConvert(i);
                        changeProListener.recreate();
                        dialogInterface.dismiss();
                    })
                    .create();
            dialog.show();
        });
        llScreenDirection.setOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(activity.getString(R.string.screen_direction))
                    .setSingleChoiceItems(activity.getResources().getStringArray(R.array.screen_direction_list_title), readBookControl.getScreenDirection(), (dialogInterface, i) -> {
                        readBookControl.setScreenDirection(i);
                        upScreenDirection(i);
                        changeProListener.recreate();
                        dialogInterface.dismiss();
                    })
                    .create();
            dialog.show();
        });
    }

    private void initData() {
        upScreenDirection(readBookControl.getScreenDirection());
        upScreenTimeOut(readBookControl.getScreenTimeOut());
        upFConvert(readBookControl.getTextConvert());
        swVolumeNextPage.setChecked(readBookControl.getCanKeyTurn());
        swReadAloudKey.setChecked(readBookControl.getAloudCanKeyTurn());
        sbHideStatusBar.setChecked(readBookControl.getHideStatusBar());
        sbHideNavigationBar.setChecked(readBookControl.getHideNavigationBar());
        sbClick.setChecked(readBookControl.getCanClickTurn());
        sbClickAllNext.setChecked(readBookControl.getClickAllNext());
        sbShowTitle.setChecked(readBookControl.getShowTitle());
        sbShowTimeBattery.setChecked(readBookControl.getShowTimeBattery());
        sbShowLine.setChecked(readBookControl.getShowLine());
        sbImmersionBar.setChecked(readBookControl.getImmersionStatusBar());
        upView();
    }

    private void upView() {
        if (readBookControl.getHideStatusBar()) {
            llShowTimeBattery.setVisibility(View.VISIBLE);
        } else {
            llShowTimeBattery.setVisibility(View.GONE);
        }
        if (readBookControl.getCanKeyTurn()) {
            llReadAloudKey.setVisibility(View.VISIBLE);
        } else {
            llReadAloudKey.setVisibility(View.GONE);
        }
    }

    private void upScreenTimeOut(int screenTimeOut) {
        tvScreenTimeOut.setText(activity.getResources().getStringArray(R.array.screen_time_out)[screenTimeOut]);
    }

    private void upFConvert(int fConvert) {
        tvJFConvert.setText(activity.getResources().getStringArray(R.array.convert_s)[fConvert]);
    }

    private void upScreenDirection(int screenDirection) {
        String[] screenDirectionListTitle = activity.getResources().getStringArray(R.array.screen_direction_list_title);
        if (screenDirection >= screenDirectionListTitle.length) {
            tvScreenDirection.setText(screenDirectionListTitle[0]);
        } else {
            tvScreenDirection.setText(screenDirectionListTitle[screenDirection]);
        }
    }

}
