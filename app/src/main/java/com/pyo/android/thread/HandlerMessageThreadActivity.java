package com.pyo.android.thread;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HandlerMessageThreadActivity extends Activity{
	private static final int PROGRESS_DIALOG_ONE = 1;
	
	private MessageHandlerThread messageThread;
	private ProgressDialog progressDialog;
    private EditText backGroundMessage;

	final Handler mainHandler = new Handler() {
		public void handleMessage(Message  message) {
			int increment = message.getData().getInt("increment");
			progressDialog.setProgress(increment);
			if ( increment > 20) {
				dismissDialog(PROGRESS_DIALOG_ONE);
				messageThread.setThreadEndFlag(true);
			}
			backGroundMessage.setText(
					  backGroundMessage.getText().toString() + increment + ":");
		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handler_message_using_layout);
        backGroundMessage = (EditText)findViewById(R.id.back_ground_message);
		final Button btnMessageThread = (Button) findViewById(R.id.btn_message_thread);
		btnMessageThread.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(PROGRESS_DIALOG_ONE, null);
			}
		});
	}
    @Override
	protected Dialog onCreateDialog(int id,Bundle bundle) {
		switch (id) {
	 	 case PROGRESS_DIALOG_ONE:
			
	 		progressDialog = new ProgressDialog(
	 				HandlerMessageThreadActivity.this);
			progressDialog.setMax(20);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("로딩중...잠시만 기다리시기 바랍니다.");
			messageThread = new MessageHandlerThread();
			messageThread.start();
			return progressDialog;
		 default:
			return null;
		}
    }
	private class MessageHandlerThread extends Thread {
        private boolean threadFlag;
		public void run() {
			int increment = 0 ;
			while (!threadFlag) {
				try {
					sleep(500);
					Message  messageThread = mainHandler.obtainMessage();
					Bundle bundle = new Bundle();
					bundle.putInt("increment", increment);
					messageThread.setData(bundle);
					mainHandler.sendMessage(messageThread);
					increment++;
				} catch (InterruptedException e) {
				   //인터럽트가 걸리면 종료 됨
					Log.e("BACK_THREAD_TAG", " 쓰레드 인터럽트 종료 됨!");
				}
			}
		} 
		// 종료하기 위함
		public void setThreadEndFlag(boolean threadFlag) {
			this.threadFlag = threadFlag;
		}
	}
}