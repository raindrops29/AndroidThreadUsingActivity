package com.pyo.android.thread;

import java.util.Set;
import java.util.TreeMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ThreadSampleListActivity extends ListActivity{
	private TreeMap<String,Intent> actions = new TreeMap<String,Intent>();

	@Override
	protected void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
		
	    addActionMap("1.SimpleThread(Thread)",ThreadSimpleActivity.class);
	    addActionMap("2.SimpleRunnable(Thread)",RunnableSimpleActivity.class);
	    addActionMap("3.MessageHandlerThread",HandlerMessageThreadActivity.class);
	    addActionMap("4.PostHandlerThread",HandlerPostThreadActivity.class);
	    addActionMap("5.AsyncTaskThread", AsyncTaskThreadActivity.class);
		Set<String> keys = actions.keySet();
		String [] keyNames = new String[keys.size()];
		keyNames = keys.toArray(keyNames);
		
		setListAdapter(new ArrayAdapter<String>(this,
				         android.R.layout.simple_list_item_1, keyNames));
	}
	private  void  addActionMap(String keyName, Class<?> className){
		actions.put(keyName,new Intent(this, className));
	}
	public  void onListItemClick(ListView listView, View item, int position, long id){
		String keyName =  (String)listView.getItemAtPosition(position);
		startActivity(actions.get(keyName));
	}
}
