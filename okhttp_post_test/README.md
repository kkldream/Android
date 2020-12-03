# okHttp
使用 okHttp3 第三方庫實現 Post 功能  
環境：API 30

## 載入庫：
```java
implementation 'com.squareup.okhttp3:okhttp:4.7.2'
```

## 修改屬性：
API 28 以上限制 APP 所有未加密 http 連接
```xml
<application
    ...
    android:usesCleartextTraffic="true" >
    ...
</application>
<uses-permission android:name="android.permission.INTERNET" />
```

## 參考連接：
* [OkHttp 官方網址](https://square.github.io/okhttp/)
* [碼農日常-『Android studio』以okHttp第三方庫取得網路資料(POST、GET、WebSocket)](https://thumbb13555.pixnet.net/blog/post/325387050-okhttp)