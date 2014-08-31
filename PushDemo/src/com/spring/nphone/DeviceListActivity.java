package com.spring.nphone;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spring.nphone.db.BindDbHelper;
import com.spring.nphone.domain.User;

public class DeviceListActivity extends ActionBarActivity {

	private ListView listView;
	private List<User> users = new ArrayList<User>();
	private ArrayAdapter<User> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_list_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		listView = (ListView) this.findViewById(R.id.listView);
		adapter = new ArrayAdapter<User>(DeviceListActivity.this, android.R.layout.simple_list_item_1, users);
		listView.setAdapter(adapter);
		new ReadTask().execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_device:
		{
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), AddDevice.class);
			startActivityForResult(intent, 0);
		}
			break;
		case R.id.action_delete_device:
		{
			BindDbHelper.getInstance(getApplicationContext()).cleanDevices();
			new ReadTask().execute();
		}
			break;
		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0 == 0){
			if(arg1 == RESULT_OK){
				new ReadTask().execute();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.add_device, menu);
    	return super.onCreateOptionsMenu(menu);	
	}
	
	class ReadTask extends AsyncTask<Void, Integer, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			users.clear();
			List<User> resutl = BindDbHelper.getInstance(getApplicationContext()).getDevices();
			if (null != resutl) {
				users.addAll(resutl);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
		}
	}

}
