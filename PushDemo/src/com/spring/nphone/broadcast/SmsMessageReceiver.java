package com.spring.nphone.broadcast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;
import com.spring.nphone.Utils;
import com.spring.nphone.db.BindDbHelper;
import com.spring.nphone.domain.BaseObjce;
import com.spring.nphone.domain.NotifyObject;
import com.spring.nphone.domain.NotifyType;
import com.spring.nphone.domain.User;
import com.spring.nphone.service.BaiduServiceClient;

public class SmsMessageReceiver extends BroadcastReceiver {

	public final String TAG = this.getClass().getSimpleName();

	public String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	@Override
	public void onReceive(final Context context, Intent intent) {

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				String content = "";
				String fromAddress = "";
				String fromDisplayName = "";
				Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[] { "_id", "date", "address", "body", "read" },
						"date>?", new String[] { System.currentTimeMillis() / 1000 - 5 + "" }, "date desc");
				if (cursor != null) {
					Log.i(TAG, "receiver msg...");
					while (cursor.moveToNext()) {
						fromAddress = cursor.getString(cursor.getColumnIndex("address"));
						content = cursor.getString(cursor.getColumnIndex("body"));
						content = StringFilter(content);
						fromDisplayName = fromAddress;
						cursor.close();
						break;
					}
					Uri uri;
					String[] projection;

					try {
						uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(fromAddress));
						projection = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME };

						// Query the filter URI
						cursor = context.getContentResolver().query(uri, projection, null, null, null);
						if (cursor != null) {
							if (cursor.moveToFirst())
								fromDisplayName = cursor.getString(0);
							cursor.close();
						}

					} catch (Throwable e) {
					}
					final Gson gson = new Gson();
					NotifyObject msg = new NotifyObject();
					msg.setContent(content);
					msg.setName(fromDisplayName);
					msg.setNotifyType(NotifyType.MESSAGE);
					msg.setTime(System.currentTimeMillis());
					msg.setPhone(fromAddress);
					String str = gson.toJson(msg);
					final BaseObjce baseObjce = new BaseObjce();
					baseObjce.setTitle(fromDisplayName);
					if (content.length() > 20) {
						baseObjce.setDescription(content.substring(0, 20));
					} else {
						baseObjce.setDescription(content);
					}
					baseObjce.setCustom_content(str);

					List<User> users = BindDbHelper.getInstance(context).getDevices();
					if (users == null) {
						users = new ArrayList<User>();
						users.add(Utils.user);
					}
					Iterator<User> iterator = users.iterator();
					while (iterator.hasNext()) {
						User user = iterator.next();
						BaiduServiceClient.getInstance().pushNotification(user, gson.toJson(baseObjce));
						Log.i(TAG, gson.toJson(baseObjce));
					}
				}
			}
		}).start();
	}
}
