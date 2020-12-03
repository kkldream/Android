
## 載入庫：
```java
import org.json.JSONArray;
import org.json.JSONObject;
```
## 主要程式：
```java
String jsonArr = "[{a:1,b:2}]";
// String jsonObj = "{a:1,b:2}";

JSONArray jsonArray = new JSONArray(jsonArr);
JSONObject jsonObject = jsonArray.getJSONObject(0);
// JSONObject jsonObject = new JSONObject(jsonObj);
textView.setText(jsonObject.getString("a"));
```