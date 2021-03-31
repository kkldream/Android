package com.example.gm_get_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    //Log TAG
    private final String TAG = "訊息 ";

    //Delay time
    private final short TIME = 3000;
    private final short SOCKET_CONNECT_OUT_TIME = 3000;

    //Socket Connect Server
    private final String DNS_ADDRESS = "125.227.185.199";
    private final short DNS_PORT = 10143;

    private final String IP_ADDRESS = "61.220.209.2199";
    private final short IP_PORT = 9003;

    //Data
    private String[] dataArray = {"888889", "0040080", "", "A", "N3725.3199", "E-1225.04", "0.0", "0.0", "", "1023E", "G000000", "00171365", "", "", "", ":;:;:;:"};

    //Thread | 線程
    private Handler mThreadHandler;
    private HandlerThread mThread;

    private Handler reSendHandler;
    private HandlerThread reSendThread;

    private Handler reConnectHandler;
    private HandlerThread reConnectThread;

    //Socket
    private Socket socket;
    private PrintWriter printWriter;

    //GPS
    private Location lastKnownLocation;
    private LocationManager locationManager;

    //Sensor | 傳感器
    private SensorManager sensorManager;
    private Sensor sensorOrientation;
    private Sensor sensorGravity;

    //Connectivity | 網路資訊
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    //Telephony | 網路信號
    private TelephonyManager telephonyManager;

    //Other | 其他
    private int csq = 0;
    private String dBm = "";
    private String bestProvider;
    private boolean reConnectStatus = false;
    private ArrayList failData = new ArrayList();
    private File fileFailData = new File(getSDPath() + File.separator + "failData.txt");

    //View | 介面
    private Button BTN_EXIT;
    private Button BTN_CLEAR_LOG;
    private Button BTN_DISCONNECT;

    private TextView TXT_LOG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
        initialize();
        dataInitialize();
        pushData();

        reConnectThread = new HandlerThread("reConnectThread");
        reSendThread = new HandlerThread("reSendThread");
        mThread = new HandlerThread("mThread");

        reConnectThread.start();
        reSendThread.start();
        mThread.start();

        reConnectHandler = new Handler(reConnectThread.getLooper());
        reSendHandler = new Handler(reSendThread.getLooper());
        mThreadHandler = new Handler(mThread.getLooper());

        mThreadHandler.post(Connection);
    }

    //檢查權限
    private void checkPermission() {
        List<String> permissionList = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)    // GPS
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED)    // Sensor
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                permissionList.add(Manifest.permission.BODY_SENSORS);
            } else {
                Log.e(TAG, "檢查權限 API版本不足");
            }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)    // GPS
            permissionList.add(Manifest.permission.READ_PHONE_STATE);

        if (!permissionList.isEmpty())
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[0]), 1);
    }

    /**-----------------------------------------------------------  Initialize  ---------------------------------------------------------- **/

    private void initialize() {
        //View
        BTN_DISCONNECT = findViewById(R.id.btnDisconnect);
        BTN_CLEAR_LOG = findViewById(R.id.btnClearLog);
        BTN_EXIT = findViewById(R.id.btnExit);
        TXT_LOG = findViewById(R.id.textViewLog);

        //Setting
        TXT_LOG.setMovementMethod(new ScrollingMovementMethod());

        //Listener
        BTN_DISCONNECT.setOnClickListener(disConnect);
        BTN_CLEAR_LOG.setOnClickListener(clearLog);
        BTN_EXIT.setOnClickListener(exit);
    }

    private void dataInitialize() {
        //取得 GPS & Sensor % Telephony 服務
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // 檢查GPS權限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "請同意GPS權限", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // 檢查 GPS & 網路 定位服務
        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            Toast.makeText(MainActivity.this, "請開啟GPS定位服務或網路服務", Toast.LENGTH_LONG).show();
            return;
        }

        //查找到服务信息
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//精确度要求
        criteria.setSpeedRequired(true);//是否包含移动速度
        criteria.setAltitudeRequired(true);//是否包括高度
        criteria.setBearingRequired(true);//是否包括方位
        criteria.setCostAllowed(true);//是否允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//对耗电量的要求

        //找到最佳的Provider
        bestProvider = locationManager.getBestProvider(criteria, true);

        //取得GPS的位置訊息
        lastKnownLocation = locationManager.getLastKnownLocation(bestProvider);

        //取得 Orientation | Gravity Sensor的訊息
        sensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        if (sensorOrientation == null)
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);

        if (sensorGravity == null)
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_DELAY_NORMAL);

        if (lastKnownLocation == null)
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, locationListener);
    }

    /**-----------------------------------------------------------  Initialize  ---------------------------------------------------------- **/

    /**-----------------------------------------------------------  Runnable  --------------------------------------------------------- **/

    //Socket傳送線程
    private Runnable Connection = new Runnable() {
        @Override
        public void run() {
            try {
                socketConnect(DNS_ADDRESS, DNS_PORT, "DNS", 0);
                Thread.sleep(2000);

                if (socket.isConnected()){
                    printWriter = new PrintWriter(socket.getOutputStream());
                    if (csq > 5){
                        printWriter.write(">" + csq + "\r\n");
                        printWriter.flush();
                    }
                }

                //網路斷掉Socket也就斷
                while (socket.isConnected()) {
                    dataArray[2] = getDate("hhmmss");
                    dataArray[4] = getLatitude();
                    dataArray[5] = getLongitude();  //用虛擬機要關掉
                    dataArray[6] = getSpeed();
                    dataArray[8] = getDate("ddMMyy");
                    dataArray[15] = ":;:;:;:";

                    final String data = getCheckSum();

                    if (!checkInternet()) {
                        Log.e(TAG, "網路異常");
                        failData.add(data);
                        if (!reConnectStatus)
                            reConnectHandler.post(reConnect);
                    }else {
                        printWriter.write(">" + data + "\r\n");
                        printWriter.flush();
                        Log.i(TAG + "已發送", ">" + data);
                    }

                    if (checkInternet() && failData.size() >= 10) {
                        reSendHandler.post(reSend);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TXT_LOG.append(">" + data + "\r\n");
                        }
                    });

                    Thread.sleep(TIME);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                finish();
                Log.e(TAG + "Socket傳送資料錯誤", e.toString());
            }
        }
    };

    //補傳線程
    private Runnable reSend = new Runnable() {
        @Override
        public void run() {
            try {
                while (!failData.isEmpty()) {
                    Log.e(TAG, "補傳: " + failData.get(0));
                    printWriter.write(failData.get(0) + "\r\n");
                    printWriter.flush();
                    failData.remove(0);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reSendHandler.removeCallbacks(reSend);
        }
    };

    private Runnable reConnect = new Runnable() {
        @Override
        public void run() {
            reConnectStatus = true;
            socketConnect(DNS_ADDRESS, DNS_PORT, "DNS", 0);
            reConnectStatus = false;
            reConnectHandler.removeCallbacks(reConnect);
        }
    };

    /**-----------------------------------------------------------  Runnable  --------------------------------------------------------- **/

    /**-----------------------------------------------------------  Function  ---------------------------------------------------------- **/

    //將SD卡的資料存入failData
    private void pushData() {
        try {
            if (!fileFailData.exists())
                return;
            InputStreamReader inputStreamReader;
            inputStreamReader = new InputStreamReader(new FileInputStream(fileFailData));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String currText = bufferedReader.readLine();

            while (currText != null) {
                failData.add(currText);
                currText = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //取得SD卡路徑
    private String getSDPath(){
        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return String.valueOf(Environment.getExternalStorageDirectory());
        return null;
    }

    //儲存failData至記憶卡
    private void saveFailData() {
        try {
            if (!fileFailData.exists()){
                fileFailData.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(fileFailData, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            while (!failData.isEmpty()){
                bufferedWriter.write(failData.get(0) + "\r\n");
                bufferedWriter.flush();
                failData.remove(0);
            }
            bufferedWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //檢查網路連線狀態
    private boolean checkInternet() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    //取得電信APN前五碼
    private String getAPN() {
        if (checkInternet()){
            try {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                return (telephonyManager.getSubscriberId()).substring(0, 5);
            }catch (Error e){
                e.printStackTrace();
            }
        }
        return null;
    }

    //取得緯度
    private String getLatitude() {
        if (lastKnownLocation != null) {
            double latitude = lastKnownLocation.getLatitude(); //緯度
            String stringLatitude = ddToDMS(latitude);
            dataArray[3] = "A";
            if (latitude < 0) {
                return "S" + stringLatitude;
            } else {
                return "N" + stringLatitude;
            }
        }
        dataArray[3] = "V";
        return null;
    }

    //取得經度
    private String getLongitude() {
        if (lastKnownLocation != null) {
            double longitude = lastKnownLocation.getLongitude(); //經度
            String stringLongitude = ddToDMS(longitude);
            dataArray[3] = "A";
            if (longitude < 0) {
                return "W" + stringLongitude;
            } else {
                return "E" + stringLongitude;
            }
        }
        dataArray[3] = "V";
        return null;
    }

    //取得加速度
    private String getSpeed() {
        if (lastKnownLocation != null) {
            return String.valueOf(lastKnownLocation.getSpeed()); //加速度
        }
        return null;
    }

    //取得時間及日期
    private String getDate(String formatDate) {
        DateFormat dateFormat = new SimpleDateFormat(formatDate);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(cal.getTime());
    }

    //轉換經緯度格式至度分
    @SuppressLint("DefaultLocale")
    private String ddToDMS(double dd) {
        double lat = (int) dd;
        double min = Math.abs((dd - lat) * 60);
        return String.format("%.0f", lat) + String.format("%.4f", min);
    }

    //取得CheckSum，並將資料整合
    private String getCheckSum() {
        String data = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            data = String.join(",", dataArray);
        }else {
            for (String curData : dataArray){
                data += curData + ",";
            }
            data = data.substring(data.length()-1);
        }

        int temp = 0;
        char[] mChar = data.toCharArray();
        for (int i = 0; i < mChar.length; i++) {
            if (i+1 < mChar.length) {
                temp = ((int) (mChar[i]) ^ (int) (mChar[i+1]));
                mChar[i+1] = (char) temp;
            }
        }
        return data + "*" + Integer.toHexString(temp);
    }


    //Socket連線迴圈
    private void socketConnect(String ip, int port, String type, int connectIPTime) {
        try {
            socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(ip, port), SOCKET_CONNECT_OUT_TIME);
            } catch (IOException ignored) {}

            int times = 0;
            if (!socket.isConnected()) {
                Log.e(TAG, "Socket 連線失敗");
                while (times < 3 && !socket.isConnected()) {
                    if (type.equals("IP"))
                        connectIPTime++;

                    Log.e(TAG, "Socket 連線失敗 第" + (times + 1) + "次");
                    Log.e(TAG, "連線IP次數: " + connectIPTime);
                    Log.e(TAG, "Type: " + type);

                    times++;
                    Thread.sleep(3000); //間隔15秒

                    socket = new Socket();
                    try {
                        socket.connect(new InetSocketAddress(ip, port), SOCKET_CONNECT_OUT_TIME);
                    } catch (IOException ignored) {}

                    if (connectIPTime == 4) {
                        Log.e(TAG, "重開系統");
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;
                    }
                }
                if (!socket.isConnected() && connectIPTime != 4) {
                    Log.e(TAG, "休息一分鐘");    //Thread.sleep(60000);
                    Thread.sleep(6000);
                    socketConnect(IP_ADDRESS, IP_PORT, "IP", connectIPTime);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Socket斷線
    private void disConnect() {
        try {
            //關閉線程且確認Android SDK版本
            if (mThreadHandler != null) {
                mThreadHandler.removeCallbacks(Connection);
                mThread.quit();
            }

            if (reSendHandler != null) {
                reSendHandler.removeCallbacks(reSend);
                reSendThread.quit();
            }

            //  確認TelephonyManager 狀態
            if (telephonyManager != null)
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

            //  確認LocationManager狀態
            if (locationManager != null)
                locationManager.removeUpdates(locationListener);

            //  確認SensorManager狀態
            if (sensorManager != null)
                sensorManager.unregisterListener(sensorEventListener);

            //  確認網路連線狀態
            if (!checkInternet()){
                return;
            }else if (socket.isConnected()){
                printWriter.close();
                socket.close();
            }
            Log.e(TAG + "嘗試斷線", "Socket狀態: " + socket.isClosed());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG + "斷開連線錯誤", e.toString());
        }
    }

    /**------------------------------------------------------------  Function  ---------------------------------------------------------- **/

    /**------------------------------------------------------------  Listener  ----------------------------------------------------------- **/

    //Location GPS
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(TAG + "位置改變 ", "緯度:" + location.getLatitude() + " 經度:" + location.getLongitude());
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MainActivity.this, "請開啟GPS定位服務或網路服務", Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String provider) {}
        public void onProviderDisabled(String provider) {}
    };

    //Sensor 方位
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            if(event.sensor.getType() == Sensor.TYPE_GRAVITY){
                Log.i(TAG, "G-Sensor: " + "X: " + event.values[0]+ " Y: " +event.values[1]+ " Z: " +event.values[2]);
            }

            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION){
                if (sensorOrientation == null)
                    dataArray[7] = "null";
                dataArray[7] = (new DecimalFormat("000.0").format(event.values[0]));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    //PhoneState 網路
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            try {
                Method methodGetLteDbm = SignalStrength.class.getMethod("getLteDbm");
                dBm = String.valueOf(methodGetLteDbm.invoke(signalStrength));
                if (Integer.parseInt(dBm) < 0)
                    csq =Math.abs(((-113 - (int) methodGetLteDbm.invoke(signalStrength)) / 2));
                else
                    csq = 99;

                Log.e(TAG, "網路品質dBm: " + dBm);
                Log.e(TAG, "網路品質CSQ: " + csq);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    };

    /**------------------------------------------------------------  Listener  ----------------------------------------------------------- **/

    /**------------------------------------------------------------  On click  ----------------------------------------------------------- **/

    //清空Log
    private View.OnClickListener clearLog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TXT_LOG.setText("");
        }
    };

    //斷線
    private View.OnClickListener disConnect = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            disConnect();
        }
    };

    //關閉程式
    private View.OnClickListener exit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            System.exit(0);
        }
    };

    /**-------------------------------------------------------------  On click  ------------------------------------------------------------ **/


    /**------------------------------------------------------------  生命週期  ----------------------------------------------------------- **/
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager.requestLocationUpdates(bestProvider, 1000, 1, locationListener);   //Location
        sensorManager.registerListener(sensorEventListener, sensorOrientation, SensorManager.SENSOR_DELAY_NORMAL);  //Sensor Orientation
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        disConnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveFailData();
        disConnect();
    }

    /**------------------------------------------------------------  生命週期  ----------------------------------------------------------- **/
}
