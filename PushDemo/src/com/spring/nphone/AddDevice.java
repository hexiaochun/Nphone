package com.spring.nphone;

import com.spring.nphone.db.BindDbHelper;
import com.spring.nphone.domain.User;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDevice extends ActionBarActivity {

	private EditText userid ;
	
	private EditText channelid ;
	
	private Button submit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_device_layout);
		
		userid = (EditText) this.findViewById(R.id.userid);
		channelid = (EditText) this.findViewById(R.id.channelid);
		submit  = (Button) this.findViewById(R.id.but_submit);
		
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(channelid.getText().toString().trim().length()<10){
					return;
				}
				if(userid.getText().toString().trim().length()<10){
					return;
				}
				User user = new User();
				user.setChannelId(Long.parseLong(channelid.getText().toString().trim()));
				user.setUserId(userid.getText().toString().trim());
				int status = BindDbHelper.getInstance(getApplicationContext()).addDevice(user);
				if(status == -1){
					Toast.makeText(getApplicationContext(), "已经存在不用加入", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "加入成功", Toast.LENGTH_SHORT).show();
				}
				setResult(RESULT_OK);
				onBackPressed();
			}
		});
	}
	
}
