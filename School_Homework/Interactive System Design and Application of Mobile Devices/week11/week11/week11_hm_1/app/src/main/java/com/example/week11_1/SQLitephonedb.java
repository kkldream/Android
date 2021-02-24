package com.example.week11_1;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//SQLiteOpenHelper繼承建立資料庫
public class SQLitephonedb extends SQLiteOpenHelper {
	// 資料庫名字
	private static final String DBNAME = "phoneDB";
	// 資料庫版本
	private final static int DB_Ver = 1;
	// 資料表名字
	private static final String TABLENAME = "MYTABLE";
	// 欄位1名字
	private static final String FIELD01_NAME = "_id";
	// 欄位2名字
	private static final String FIELD02_NAME = "_text1";
	// 欄位3名字
	private static final String FIELD03_NAME = "_text2";
	// 宣告游標
	public Cursor cursor;

	// 建構資料庫和版本
	public SQLitephonedb(Context context) {
		super(context, DBNAME, null, DB_Ver);
		// TODO Auto-generated constructor stub
	}

	// Android 載入時找不到資料庫檔案時，就會建立資料表(同時會建立出資料庫)
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String create_sql = "CREATE TABLE " + TABLENAME + "(" + FIELD01_NAME
				+ " INTEGER PRIMARY KEY, " + FIELD02_NAME + " TEXT, "
				+ FIELD03_NAME + " TEXT);";
		db.execSQL(create_sql);
	}

	// 當版本有更新時，會進行變更
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String drop_table1 = "drop table if exists " + TABLENAME;
		db.execSQL(drop_table1);
		onCreate(db);
	}

	// 新增
	public void insert(String text1, String text2) {
		// 取得資料
		SQLiteDatabase db = getReadableDatabase();
		// 新增指令
		// 方法1. insert into 資料表 values (新增資料須符合欄位數量)
		// 方法2. insert into 資料表(欄位名稱) values (只在此欄位新增資料)
		// 如果是加入文字檔前後要加入'
		String insert = "insert into " + TABLENAME
				+ "(_text1,_text2) values ('" + text1 + "','" + text2 + "')";
		// 在資料庫中使用指令
		db.execSQL(insert);
		// 指標重新搜尋
		cursor.requery();
	}

	// 查詢
	public ArrayList<String> select(int i) {
		SQLiteDatabase db = getReadableDatabase();
		// 搜尋指令
		// select 欄位 from 資料表 *為全部資料的意思
		// 指定搜尋指令
		// select 欄位 from 資料表 where 欄位=想要找的資料
		String selectQuery = "SELECT * FROM " + TABLENAME;
		// 載入資料庫中，並讓游標指向資料
		Cursor c = db.rawQuery(selectQuery, null);
		// 建立陣列
		ArrayList<String> text = new ArrayList<String>();
		// 游標移動
		while (c.moveToNext()) {
			// 新增資料至陣列中
			text.add(c.getString(i));
		}
		cursor.requery();
		return text;
	}

	// 刪除
	public void delete(int id) {
		SQLiteDatabase db = getReadableDatabase();
		// 刪除指令
		// delete from 資料表
		// 指定刪除指令
		// delete from 資料表 where 欄位=想要找的資料
		String delete = "delete from " + TABLENAME + " where _id=" + id;
		db.execSQL(delete);
		cursor.requery();
	}

	// 更新
	public void update(int id, String newname, String newphone) {
		SQLiteDatabase db = getReadableDatabase();
		// 更新指令
		// update 資料表 set 要改變的欄位=要改變的值 where 搜尋的欄位=搜尋的關鍵字
		String update = "update MYTABLE set _text1='" + newname
				+ "', _text2='" + newphone + "' where _id=" + id;
		db.execSQL(update);
		cursor.requery();
	}

	public void curosr() {
		SQLiteDatabase db = getReadableDatabase();
		// 取得資料記錄，並指向游標
		cursor = db.query(TABLENAME, null, null, null, null, null, null);
	}

}
