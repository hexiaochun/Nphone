package com.spring.nphone.broadcast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.spring.nphone.Utils;
import com.spring.nphone.db.BindDbHelper;
import com.spring.nphone.domain.BaseObjce;
import com.spring.nphone.domain.NotifyObject;
import com.spring.nphone.domain.NotifyType;
import com.spring.nphone.domain.User;
import com.spring.nphone.service.BaiduServiceClient;

public class PhoneCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		MyPhoneStateListener phoneListener = new MyPhoneStateListener(context);
		TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	public class MyPhoneStateListener extends PhoneStateListener {
		Context context;

		public MyPhoneStateListener(Context context) {
			this.context = context;
		}

		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				Log.d("DEBUG", "IDLE");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.d("DEBUG", "OFFHOOK");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				Log.d("DEBUG", "RINGING");

				String fromDisplayName = incomingNumber;
				Uri uri;
				String[] projection;

				try {
					uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));
					projection = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME };

					// Query the filter URI
					Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
					if (cursor != null) {
						if (cursor.moveToFirst())
							fromDisplayName = cursor.getString(0);

						cursor.close();
					}
				} catch (Throwable e) {
				}

				final Gson gson = new Gson();
				NotifyObject msg = new NotifyObject();
				msg.setContent("来电");
				msg.setName(fromDisplayName);
				msg.setNotifyType(NotifyType.PHONE);
				msg.setTime(System.currentTimeMillis());
				msg.setPhone(incomingNumber);
				String str = gson.toJson(msg);
				final BaseObjce baseObjce = new BaseObjce();
				baseObjce.setTitle(fromDisplayName);
				baseObjce.setDescription(incomingNumber);
				baseObjce.setCustom_content(str);
				new Thread(new Runnable() {
					@Override
					public void run() {
						List<User> users = BindDbHelper.getInstance(context).getDevices();
						if (users == null) {
							users = new ArrayList<User>();
							users.add(Utils.user);
						}
						Iterator<User> iterator = users.iterator();
						while (iterator.hasNext()) {
							User user = iterator.next();
							BaiduServiceClient.getInstance().pushNotification(user, gson.toJson(baseObjce));
						}
					}
				}).start();

				break;
			}
		}
	}
}
