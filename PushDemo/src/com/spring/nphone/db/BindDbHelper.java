package com.spring.nphone.db;

import java.util.ArrayList;
import java.util.List;

import com.spring.nphone.domain.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BindDbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_NAME = "cellphone.db";

	private BindDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	private static Object object = new Object();

	private static BindDbHelper dbHelper;

	public static BindDbHelper getInstance(Context context) {
		if (null == dbHelper) {
			synchronized (object) {
				if (null == dbHelper) {
					dbHelper = new BindDbHelper(context);
				}
			}
		}
		return dbHelper;
	}

	private String table_name = "device";
	
	private String userId = "userId";
	
	private String channelid = "channelid";
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		String sql = "CREATE TABLE "+table_name+" ( "+userId +" TEXT, "+channelid  +" TEXT );"; 
		arg0.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		arg0.execSQL("DROP TABLE IF EXISTS "+table_name);  
		this.onCreate(arg0);
	}
	/**
	 * add -1 存在
	 * @param user
	 */
	public int addDevice(User user){
		ContentValues values = new ContentValues();
		values.put(userId, user.getUserId());
		values.put(channelid, user.getChannelId());
		Cursor cursor = this.getWritableDatabase().rawQuery("select * from "+table_name+" where "+userId+" = ? and "+channelid+" = ?",
				new String[]{user.getUserId(),user.getChannelId()+""});
		if(cursor.getCount()>0){
			return -1;
		}else{
			long row = this.getWritableDatabase().insert(table_name, null, values);
			return (int)row;
		}
	}
	public List<User> getDevices(){
		Cursor cursor = this.getWritableDatabase().rawQuery("select * from "+table_name, null);
		List<User> users = new ArrayList<User>();
		while (cursor.moveToNext()) {
			User user = new User();
			user.setUserId(cursor.getString(cursor.getColumnIndex(userId)));
			user.setChannelId(Long.parseLong(cursor.getString(cursor.getColumnIndex(channelid))));
			users.add(user);
		}
		
		if(users.size()==0){
			return null;
		}else{
			return users;
		}
		
	}
	
	public void cleanDevices(){
		this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+table_name); 
		String sql = "CREATE TABLE "+table_name+" ( "+userId +" TEXT, "+channelid  +" TEXT );"; 
		this.getWritableDatabase().execSQL(sql);
	}
	
}
