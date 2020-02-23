package com.yunbao.main.newview;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.yunbao.common.CommonAppConfig;
import com.yunbao.common.Constants;
import com.yunbao.common.dialog.AbsDialogFragment;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.utils.DpUtil;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;

import com.yunbao.live.http.LiveHttpConsts;
import com.yunbao.live.http.LiveHttpUtil;

public class LiveRedPackSendDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private View mGroupPsq;
    private View mGroupPj;
    private EditText mEditCoinPsq;//拼手气钻石数量
    private EditText mEditCountPsq;//拼手气红包数量
    private EditText mEditCoinPj;//平均钻石数量
    private EditText mEditCountPj;//平均红包数量
    private EditText mEditTitle;//红包标题
    private TextView mBtnSend;//发送按钮
    private TextView mCoinName1;
    private TextView mCoinName2;
    private CheckBox mCheckBox;
    private int mRedPackType;
    private String mStream;
    private String mSendRedPackString;
    private String mZuanString;
    private long mCoinPsq = 100;//拼手气钻石数量
    private long mCountPsq = 10;//拼手气红包数量
    private long mCoinPj = 1;//平均钻石数量
    private long mCountPj = 100;//平均红包数量
    private LiveAudienceViewHolder mHolder;

    @Override
    protected int getLayoutId() {
        return com.yunbao.live.R.layout.dialog_live_red_pack_send;
    }

    @Override
    protected int getDialogStyle() {
        return com.yunbao.live.R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = DpUtil.dp2px(300);
        params.height = DpUtil.dp2px(380);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }


    public void setStream(String stream,LiveAudienceViewHolder holder) {
        mStream = stream;
        mHolder=holder;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (TextUtils.isEmpty(mStream)) {
            return;
        }
        mSendRedPackString = WordUtil.getString(com.yunbao.live.R.string.red_pack_6) + " ";
        mCoinName1 = mRootView.findViewById(com.yunbao.live.R.id.coin_name_1);
        mCoinName2 = mRootView.findViewById(com.yunbao.live.R.id.coin_name_2);
        mZuanString = WordUtil.getString(com.yunbao.live.R.string.red_pack_zuan);
        String coinName = CommonAppConfig.getInstance().getCoinName();
        if (!TextUtils.isEmpty(coinName) && !coinName.startsWith(mZuanString)) {
            mZuanString = coinName;
        }
        mCoinName1.setText(mZuanString);
        mCoinName2.setText(mZuanString);
        mGroupPsq = mRootView.findViewById(com.yunbao.live.R.id.group_psq);
        mGroupPj = mRootView.findViewById(com.yunbao.live.R.id.group_pj);
        mEditCoinPsq = mRootView.findViewById(com.yunbao.live.R.id.edit_coin_psq);
        mEditCountPsq = mRootView.findViewById(com.yunbao.live.R.id.edit_count_psq);
        mEditCoinPj = mRootView.findViewById(com.yunbao.live.R.id.edit_coin_pj);
        mEditCountPj = mRootView.findViewById(com.yunbao.live.R.id.edit_count_pj);

        mEditCoinPsq.setText(String.valueOf(mCoinPsq));
        mEditCountPsq.setText(String.valueOf(mCountPsq));
        mEditCoinPj.setText(String.valueOf(mCoinPj));
        mEditCountPj.setText(String.valueOf(mCountPj));

        mEditCoinPsq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String c = s.toString();
                if (TextUtils.isEmpty(c)) {
                    mCoinPsq = 0;
                } else {
                    mCoinPsq = Long.parseLong(c);
                }
                mBtnSend.setText(mSendRedPackString + StringUtil.toWan3(mCoinPsq) + mZuanString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditCoinPj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String c = s.toString();
                if (TextUtils.isEmpty(c)) {
                    mCoinPj = 0;
                } else {
                    mCoinPj = Long.parseLong(c);
                }
                mBtnSend.setText(mSendRedPackString + StringUtil.toWan3(mCoinPj * mCountPj) + mZuanString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditCountPj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String c = s.toString();
                if (TextUtils.isEmpty(c)) {
                    mCountPj = 0;
                } else {
                    mCountPj = Long.parseLong(c);
                }
                mBtnSend.setText(mSendRedPackString + StringUtil.toWan3(mCoinPj * mCountPj) + mZuanString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRootView.findViewById(com.yunbao.live.R.id.btn_psq).setOnClickListener(this);
        mRootView.findViewById(com.yunbao.live.R.id.btn_pj).setOnClickListener(this);
        mBtnSend = mRootView.findViewById(com.yunbao.live.R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnSend.setText(mSendRedPackString + StringUtil.toWan3(mCoinPsq) + mZuanString);
        mEditTitle = mRootView.findViewById(com.yunbao.live.R.id.edit_title);
        mCheckBox = mRootView.findViewById(com.yunbao.live.R.id.checkbox);
        mRedPackType = Constants.RED_PACK_TYPE_SHOU_QI;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == com.yunbao.live.R.id.btn_psq) {
            mRedPackType = Constants.RED_PACK_TYPE_SHOU_QI;
            if (mGroupPsq != null && mGroupPsq.getVisibility() != View.VISIBLE) {
                mGroupPsq.setVisibility(View.VISIBLE);
            }
            if (mGroupPj != null && mGroupPj.getVisibility() == View.VISIBLE) {
                mGroupPj.setVisibility(View.INVISIBLE);
            }
            mBtnSend.setText(mSendRedPackString + StringUtil.toWan3(mCoinPsq) + mZuanString);

        } else if (i == com.yunbao.live.R.id.btn_pj) {
            mRedPackType = Constants.RED_PACK_TYPE_AVERAGE;
            if (mGroupPj != null && mGroupPj.getVisibility() != View.VISIBLE) {
                mGroupPj.setVisibility(View.VISIBLE);
            }
            if (mGroupPsq != null && mGroupPsq.getVisibility() == View.VISIBLE) {
                mGroupPsq.setVisibility(View.INVISIBLE);
            }
            mBtnSend.setText(mSendRedPackString + StringUtil.toWan3(mCoinPj * mCountPj) + mZuanString);

        } else if (i == com.yunbao.live.R.id.btn_send) {
            sendRedPack();

        }
    }

    /**
     * 发红包
     */
    private void sendRedPack() {
        String coin = null;
        String count = null;
        if (mRedPackType == Constants.RED_PACK_TYPE_SHOU_QI) {
            coin = mEditCoinPsq.getText().toString().trim();
            count = mEditCountPsq.getText().toString().trim();
        } else {
            coin = mEditCoinPj.getText().toString().trim();
            count = mEditCountPj.getText().toString().trim();
        }
        if (TextUtils.isEmpty(coin)) {
            ToastUtil.show(com.yunbao.live.R.string.red_pack_7);
            return;
        }
        if (TextUtils.isEmpty(count)) {
            ToastUtil.show(com.yunbao.live.R.string.red_pack_8);
            return;
        }
        String title = mEditTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            title = mEditTitle.getHint().toString().trim();
        }
        int sendType = mCheckBox.isChecked() ? Constants.RED_PACK_SEND_TIME_NORMAL : Constants.RED_PACK_SEND_TIME_DELAY;
        LiveHttpUtil.sendRedPack(mStream, coin, count, title, mRedPackType, sendType, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    dismiss();
                    mHolder.sendRedPackMessage();
                }
                ToastUtil.show(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.SEND_RED_PACK);
        super.onDestroy();
    }
}
