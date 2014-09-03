package com.spring.nphone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.google.gson.Gson;
import com.spring.nphone.db.BindDbHelper;
import com.spring.nphone.domain.BaseObjce;
import com.spring.nphone.domain.NotifyObject;
import com.spring.nphone.domain.NotifyType;
import com.spring.nphone.domain.User;
import com.spring.nphone.service.BaiduServiceClient;

/*
 * 云推送Demo主Activity。
 * 代码中，注释以Push标注开头的，表示接下来的代码块是Push接口调用示例
 */
public class PushDemoActivity extends ActionBarActivity  {

    private static final String TAG = PushDemoActivity.class.getSimpleName();

    private TextView userid;
    
    private TextView channelid;
    
    public static PushDemoActivity activity = null;
    
    OnClickListener onClickListener = new OnClickListener() {
		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			String str = ((TextView)v).getText().toString().trim();
			int sdk = android.os.Build.VERSION.SDK_INT;
			if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			    clipboard.setText(str);
			} else {
			    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); 
			    android.content.ClipData clip = android.content.ClipData.newPlainText("text label",str);
			    clipboard.setPrimaryClip(clip);
			}
			Toast.makeText(getApplicationContext(), "复制成功", Toast.LENGTH_SHORT).show();
		}
	};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.logStringCache = Utils.getLogText(getApplicationContext());

        Resources resource = this.getResources();
        String pkgName = this.getPackageName();

        setContentView(R.layout.main);
        
        userid  = (TextView) this.findViewById(R.id.userid);
        channelid =  (TextView) this.findViewById(R.id.changeId);
        
        userid.setOnClickListener(onClickListener);
        channelid.setOnClickListener(onClickListener);
        // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
        // 这里把apikey存放于manifest文件中，只是一种存放方式，
        // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
        // "api_key")
        // 通过share preference实现的绑定标志开关，如果已经成功绑定，就取消这次绑定
            PushManager.startWork(getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    Utils.getMetaValue(PushDemoActivity.this, "api_key"));
            // Push: 如果想基于地理位置推送，可以打开支持地理位置的推送的开关
            // PushManager.enableLbs(getApplicationContext());

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                getApplicationContext(), resource.getIdentifier(
                        "notification_custom_builder", "layout", pkgName),
                resource.getIdentifier("notification_icon", "id", pkgName),
                resource.getIdentifier("notification_title", "id", pkgName),
                resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier(
                "simple_notification_icon", "drawable", pkgName));
        PushManager.setNotificationBuilder(this, 1, cBuilder);
        activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_activity_actions, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.action_settings:
		{
			Intent intent = new Intent();
			intent.setClass(this, DeviceListActivity.class);
			startActivity(intent);
		}
			break;
		default:
			break;
		}
    	
    	
    	return super.onOptionsItemSelected(item);
    }
    
    public void updateBindInfo(User user){
    	userid.setText(user.getUserId()+"");
    	channelid.setText(user.getChannelId()+"");
    }

    @Override
    public void onDestroy() {
        Utils.setLogText(getApplicationContext(), Utils.logStringCache);
        super.onDestroy();
    }

    public void sendNotifation(View view){
    	new Thread(new Runnable() {
			@Override
			public void run() {
				Gson gson = new Gson();
				NotifyObject msg = new NotifyObject();
				msg.setContent("content");
				msg.setName("spring");
				msg.setNotifyType(NotifyType.MESSAGE);
				msg.setTime(System.currentTimeMillis());
				msg.setPhone("181632510808");
				String str = gson.toJson(msg);
				BaseObjce baseObjce = new BaseObjce();
				baseObjce.setTitle("new msg");
				baseObjce.setDescription("test!!!");
				baseObjce.setCustom_content(str);
				
				List<User> users = BindDbHelper.getInstance(getApplicationContext()).getDevices();
				if(null==users){
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
    }
    

}
