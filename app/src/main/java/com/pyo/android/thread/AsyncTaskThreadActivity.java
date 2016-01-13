package com.pyo.android.thread;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AsyncTaskThreadActivity extends Activity {
	private static final int PROGRESS_DIALOG_ONE = 1;
	
	private ProgressDialog progressDialog;
	private EditText backGroundMessage;

   @Override 
   public void onCreate(Bundle  savedInstanceState){
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.asynctask_using_layout);
	   
	   backGroundMessage = 
			   (EditText)findViewById(R.id.back_ground_message);
	   Button asyncTaskThreadStart = 
			   (Button)findViewById(R.id.btn_asynctask_thread);
	   asyncTaskThreadStart.setOnClickListener(
			   new View.OnClickListener(){	
		@Override
		public void onClick(View v){
			//AsyncTask는 반드시 Main UI Thread영역에서 실행되어야 함
			new AsyncTaskThreadImpl().execute(0);
		}
	   });
   }
   private  class  AsyncTaskThreadImpl extends 
                AsyncTask<Integer, Integer, String>{
	  private boolean threadFlag;
	   //백그라운드 작업이 호출되기전에 실행
	 //보통 백그라운드 작업이 진행되기 전에 실행해야 하는 초기화 작업을 구현
	   //Main UI Thread영역에서 실행 됨
	 @Override
	 public void onPreExecute(){
		 //프로그래스 다이얼 로그를 생성 한다.
		 showDialog(PROGRESS_DIALOG_ONE, null); 
	 }
	 //백그라운드에서 실행 할 로직을 여기에 구현 
	 @Override
	 public String doInBackground(Integer... startTaskObj){
		 int progressDialogIncrement = startTaskObj[0].intValue();
		 while (!threadFlag){
				try {
					Thread.sleep(500);				
					if (progressDialogIncrement > 20) {
					  threadFlag = true;
					}
					publishProgress(++progressDialogIncrement);
				}catch(InterruptedException e) {
				   //인터럽트가 걸리면 종료 됨
					Log.e("BACK_THREAD_TAG", " 쓰레드 인터럽트 종료 됨!");
					threadFlag = true;
				}
		 }
		 return "백그라운드 쓰레드 종료됨!" ;
	 }
	 //publicProgress(,)에서 던져진 작업 목록을 UI Thread영역에서 실행
	 @Override
	 public void onProgressUpdate(Integer...  progressValues){
		 progressDialog.setProgress(progressValues[0]);
		 backGroundMessage.setText(
				  backGroundMessage.getText().toString() +progressValues[0] +":");
	 }
	 //doInBackground(,) 메소드가 종료될 때 UI Thread에서 실행되어야 하는
	 //로직을 여기서 구현
	 @Override
	 public void onPostExecute(String resultMessage){
		 backGroundMessage.setText(resultMessage);
		 dismissDialog(PROGRESS_DIALOG_ONE);
	 }
   }
   @Override
	protected Dialog onCreateDialog(int id,Bundle bundle) {
		switch (id) {
	 	 case PROGRESS_DIALOG_ONE:
			
	 		progressDialog = new ProgressDialog(AsyncTaskThreadActivity.this);
			progressDialog.setMax(20);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

			progressDialog.setMessage("로딩중...잠시만 기다리시기 바랍니다.");
		    
			return progressDialog;
		 default:
			return null;
		}
   }
}
