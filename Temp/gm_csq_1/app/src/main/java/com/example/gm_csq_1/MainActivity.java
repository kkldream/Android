package com.example.gm_csq_1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int NETWORKTYPE_WIFI = 0;
    private static final int NETWORKTYPE_4G = 1;
    private static final int NETWORKTYPE_2G = 2;
    private static final int NETWORKTYPE_NONE = 3;
    public TextView mTextView;
    public TelephonyManager mTelephonyManager;
    public PhoneStatListener mListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.textview);
//獲取telephonyManager
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//開始監聽
        mListener = new PhoneStatListener();
//監聽訊號強度
        mTelephonyManager.listen(mListener, PhoneStatListener.LISTEN_SIGNAL_STRENGTHS);
    }

    private class PhoneStatListener extends PhoneStateListener {
        //獲取訊號強度
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
//獲取網路訊號強度
//獲取0-4的5種訊號級別，越大訊號越好,但是api23開始才能用
// int level = signalStrength.getLevel();
            int gsmSignalStrength = signalStrength.getGsmSignalStrength();
//獲取網路型別
            int netWorkType = getNetWorkType(MainActivity.this);
            switch (netWorkType) {
                case NETWORKTYPE_WIFI:
                    mTextView.setText("當前網路為wifi,訊號強度為："  + gsmSignalStrength);
                    break;
                case NETWORKTYPE_2G:
                    mTextView.setText("當前網路為2G行動網路,訊號強度為："  + gsmSignalStrength);
                    break;
                case NETWORKTYPE_4G:
                    mTextView.setText("當前網路為4G行動網路,訊號強度為：" +  gsmSignalStrength);
                    break;
                case NETWORKTYPE_NONE:
                    mTextView.setText("當前沒有網路,訊號強度為：" +  gsmSignalStrength);
                    break;
                case -1:
                    mTextView.setText("當前網路錯誤,訊號強度為：" +  gsmSignalStrength);
                    break;
            }
        }
    }
    public static int getNetWorkType(Context context) {
        int mNetWorkType = -1;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return isFastMobileNetwork(context) ? NETWORKTYPE_4G : NETWORKTYPE_2G;
            }
        } else {
            mNetWorkType = NETWORKTYPE_NONE;//沒有網路
        }
        return mNetWorkType;
    }
    /**判斷網路型別*/
    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
//這裡只簡單區分兩種型別網路，認為4G網路為快速，但最終還需要參考訊號值
            return true;
        }
        return false;
    }

}