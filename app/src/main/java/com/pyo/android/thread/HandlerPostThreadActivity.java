package com.pyo.android.thread;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HandlerPostThreadActivity extends Activity{
	private static final int PROGRESS_DIALOG_ONE = 1;
	
	private PostHandlerThread postThread;
	private ProgressDialog progressDialog;
    private EditText backGroundMessage;
	private int   progressDialogIncrement;
	
	private Handler mainHandler = new Handler();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_runnable_using_layout);
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
	protected Dialog onCreateDialog(int id,Bundle bundle){
		switch (id) {
	 	 case PROGRESS_DIALOG_ONE:
			
	 		progressDialog = new ProgressDialog(HandlerPostThreadActivity.this);
			progressDialog.setMax(20);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("로딩중...잠시만 기다리시기 바랍니다.");
			postThread = new PostHandlerThread();
			postThread.start();
			return progressDialog;
		 default:
			return null;
		}
    }
    public class PostRunnable implements Runnable{
				public void run(){
						progressDialog.setProgress(
								++progressDialogIncrement);
						if ( progressDialogIncrement > 20) {
								dismissDialog(PROGRESS_DIALOG_ONE);
								postThread.interrupt();
						}
						backGroundMessage.setText(
									  backGroundMessage.getText().toString() +
									  progressDialogIncrement+ ":");
				}
	}
	private class PostHandlerThread extends Thread {
       // private boolean threadFlag;
		public void run() {
			while (!isInterrupted()) {
				try {
					sleep(500);
					mainHandler.post(new PostRunnable());
					/*mainHandler.post(new Runnable(){
						public void run(){
							progressDialog.setProgress(++progressDialogIncrement);
							if ( progressDialogIncrement > 20) {
								dismissDialog(PROGRESS_DIALOG_ONE);
								postThread.interrupt();
							}
							backGroundMessage.setText(
									  backGroundMessage.getText().toString() +
									  progressDialogIncrement+ ":");
						}
					});*/
				} catch (InterruptedException e) {
				   //인터럽트가 걸리면 종료 됨
					Log.e("BACK_THREAD_TAG", " 쓰레드 인터럽트 종료 됨!");
				//	threadFlag = true;
					this.interrupt();
				}
			}
		}
	}
}