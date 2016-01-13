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
			//AsyncTask�� �ݵ�� Main UI Thread�������� ����Ǿ�� ��
			new AsyncTaskThreadImpl().execute(0);
		}
	   });
   }
   private  class  AsyncTaskThreadImpl extends 
                AsyncTask<Integer, Integer, String>{
	  private boolean threadFlag;
	   //��׶��� �۾��� ȣ��Ǳ����� ����
	 //���� ��׶��� �۾��� ����Ǳ� ���� �����ؾ� �ϴ� �ʱ�ȭ �۾��� ����
	   //Main UI Thread�������� ���� ��
	 @Override
	 public void onPreExecute(){
		 //���α׷��� ���̾� �α׸� ���� �Ѵ�.
		 showDialog(PROGRESS_DIALOG_ONE, null); 
	 }
	 //��׶��忡�� ���� �� ������ ���⿡ ���� 
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
				   //���ͷ�Ʈ�� �ɸ��� ���� ��
					Log.e("BACK_THREAD_TAG", " ������ ���ͷ�Ʈ ���� ��!");
					threadFlag = true;
				}
		 }
		 return "��׶��� ������ �����!" ;
	 }
	 //publicProgress(,)���� ������ �۾� ����� UI Thread�������� ����
	 @Override
	 public void onProgressUpdate(Integer...  progressValues){
		 progressDialog.setProgress(progressValues[0]);
		 backGroundMessage.setText(
				  backGroundMessage.getText().toString() +progressValues[0] +":");
	 }
	 //doInBackground(,) �޼ҵ尡 ����� �� UI Thread���� ����Ǿ�� �ϴ�
	 //������ ���⼭ ����
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

			progressDialog.setMessage("�ε���...��ø� ��ٸ��ñ� �ٶ��ϴ�.");
		    
			return progressDialog;
		 default:
			return null;
		}
   }
}
