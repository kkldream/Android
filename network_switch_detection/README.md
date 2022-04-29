# 網路切換監聽

https://ithelp.ithome.com.tw/articles/10188791

AndroidManifest.xml：
```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

code：
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // 註冊mConnReceiver，並用IntentFilter設置接收的事件類型為網路開關
        this.registerReceiver(mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // 解除註冊
        this.unregisterReceiver(mConnReceiver);
    }

    // 建立一個BroadcastReceiver，名為mConnReceiver
    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 當使用者開啟或關閉網路時會進入這邊

            // 判斷目前有無網路
            if(isNetworkAvailable()) {
                // 以連線至網路，做更新資料等事情
                Toast.makeText(MainActivity.this, "有網路!", Toast.LENGTH_SHORT).show();
            }
            else {
                // 沒有網路
                Toast.makeText(MainActivity.this, "沒網路!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 回傳目前是否已連線至網路
    public boolean isNetworkAvailable()
    {
        ConnectivityManager cm = 
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null &&
                networkInfo.isConnected();
    }
}
```

