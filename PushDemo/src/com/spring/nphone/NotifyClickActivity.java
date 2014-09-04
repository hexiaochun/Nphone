package com.spring.nphone;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.google.gson.Gson;
import com.spring.nphone.domain.NotifyObject;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class NotifyClickActivity extends ActionBarActivity {

	private TextView name;
	private TextView phone;
	private TextView type;
	private TextView content;
	
	public static final String JSONSTR = "jsonstr";
	
	private String str = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_click_layout);
		
		name = (TextView) this.findViewById(R.id.name);
		phone = (TextView) this.findViewById(R.id.phone);
		type = (TextView) this.findViewById(R.id.type);
		content  = (TextView) this.findViewById(R.id.content);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		str = getIntent().getStringExtra(JSONSTR);
		
		Gson gson = new Gson();
		NotifyObject notifyObject = gson.fromJson(str, NotifyObject.class);
		
		if(notifyObject!=null){
			name.setText(notifyObject.getName());
			phone.setText(notifyObject.getPhone());
			type.setText(notifyObject.getNotifyType().name());
			content.setText(notifyObject.getContent());
		}
		
	}
	
}
