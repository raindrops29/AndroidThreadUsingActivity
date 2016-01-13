package com.pyo.android.thread;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RunnableSimpleActivity extends Activity{
    
	private long  backThreadValue1;
	private long  backThreadValue2;
	private long  mainThreadValue;
	//�ٲ� �κ�
	private Thread  backGroundThread1;
	private Thread  backGroundThread2;
	@Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	LayoutInflater mainInflater = LayoutInflater.from(this);
    	View layoutView = mainInflater.inflate(R.layout.runnable_simple_1_layout, null);
    	setContentView(layoutView);
    	
       Button btnThreadResult = (Button)layoutView.findViewById(R.id.btn_thread_result);	
       Button uiThreadIncrementBtn = (Button)layoutView.findViewById(R.id.btn_ui_thread_increment);
       final ToggleButton threadToggle = (ToggleButton)layoutView.findViewById(R.id.btn_thread_toggle);
       //textOn : �޼��� ǥ��
       threadToggle.setChecked(true);
       
       final EditText mainEdit = (EditText)layoutView.findViewById(R.id.main_edit);
       final EditText backEdit1 = (EditText)layoutView.findViewById(R.id.back_edit_1);
       final EditText backEdit2 = (EditText)layoutView.findViewById(R.id.back_edit_2);
       
       threadToggle.setOnClickListener(new View.OnClickListener(){
       	  @Override
       	  public void onClick(View threadButton){
	       		if(!threadToggle.isChecked()){
	       			//�ٲ� �κ�
	       			backGroundThread1 = new Thread(new SimpleThreadOne(1000),"BACK_GROUND_THREAD_1");
	       			backGroundThread2 = new Thread(new SimpleThreadTwo(3000),"BACK_GROUND_THREAD_2");
	       			backGroundThread1.start();
	       			backGroundThread2.start();
	       			((TextView)findViewById(R.id.textView1)).setText("Runnable ��ŸƮ ��! ");
	       			threadToggle.setChecked(false);
	       		}else{
	       		   mainEdit.setText("");
	 			   backEdit1.setText(backGroundThread1.getName() + " �����!");
	 			   backEdit2.setText(backGroundThread2.getName() + " �����!" );
	       		   backGroundThreadFinish();
	       		   ((TextView)findViewById(R.id.textView1)).setText(" �� �׶��� ������ ���� ��! ");
	       		   threadToggle.setChecked(true);
	       		   Toast.makeText(getApplication(), "Back Ground Thread�� ���� �˴ϴ� ",Toast.LENGTH_SHORT).show();
	       	    }
       	  }
       });
       uiThreadIncrementBtn.setOnClickListener(new View.OnClickListener(){
      	  @Override
      	  public void onClick(View threadButton){
      		  ++mainThreadValue;
      		  Toast.makeText(getApplication(), "Main_UI������ Ŭ���� ���� 1�� ���� �մϴ�",Toast.LENGTH_SHORT).show();
      	  }
         });
       btnThreadResult.setOnClickListener(new View.OnClickListener(){
    	  @Override
    	  public void onClick(View threadButton){
    		 mainEdit.setText(Thread.currentThread().getName() + " Ŭ�� Ƚ��  => " + mainThreadValue);  
    		 if( backGroundThread1 != null &&  backGroundThread2 != null &&
    				 backGroundThread1.isAlive() && backGroundThread2.isAlive() ){
    			   backEdit1.setText(backGroundThread1.getName() + " ������  => "  + backThreadValue1);
    			   backEdit2.setText(backGroundThread2.getName() + " ������  => "  + backThreadValue2);
    	     }else{
    	    	 Toast.makeText(getApplicationContext(), "��׶��� �����尡 Ȱ��ȭ ���� �ʾҳ׿�", Toast.LENGTH_LONG).show();
    	     }
    	  }
       });
    }
	@Override
	public void onPause(){
		super.onPause();
		backGroundThreadFinish();	
	}
	//�ٲ� �κ�
	private  void   backGroundThreadFinish(){
		 if( backGroundThread1 != null){
 			  backGroundThread1.interrupt();
 			  backGroundThread1 = null;
 			  backThreadValue1 = 0 ;
 		  }
 		 if( backGroundThread2 != null){
			  backGroundThread2.interrupt();
			  backGroundThread2 = null;
			  backThreadValue2 = 0 ;
		  }
	}
	//�ٲ� �κ�
	private class SimpleThreadOne implements Runnable{
		private long delayTime;
		private boolean threadFinishFlag;
		//�ٲ� �κ�
		public SimpleThreadOne(int delayTime){
			this.delayTime = delayTime;
		}
		public void run(){
			//�ٲ� �κ�
			while( !threadFinishFlag){
				try{
					//�ٲ� �κ�
					Thread.sleep(delayTime);
					++backThreadValue1;
				}catch(InterruptedException ie){
					Log.e("InterruptedTag", Thread.currentThread().getName() + " Thread ���ͷ�Ʈ �߻�!");
	                threadFinishFlag = true;
				}
			}
		}
	}
	//�ٲ� �κ�
	private class SimpleThreadTwo implements Runnable{
		private long delayTime;
		private boolean threadFinishFlag;
		public SimpleThreadTwo(int delayTime){
			this.delayTime = delayTime;
		}
		public void run(){
			//�ٲ� �κ�
			while( !threadFinishFlag){
				try{
					//�ٲ� �κ�
					Thread.sleep(delayTime);
					++backThreadValue2;
				}catch(InterruptedException ie){
					Log.e("InterruptedTag", Thread.currentThread().getName() + " Thread ���ͷ�Ʈ �߻�!");
				    threadFinishFlag = true;
				}
			}
		}
	}
}
