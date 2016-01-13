package com.pyo.android.thread;

import  android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ThreadSimpleActivity extends Activity{
    
	private long  backThreadValue1;
	private long  backThreadValue2;
	private long  mainThreadValue;
	//��׶��忡�� ���� �� ������ ����
	private SimpleThreadOne  backGroundThread1;
	private SimpleThreadTwo  backGroundThread2;
	@Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	LayoutInflater mainInflater = LayoutInflater.from(this);
    	View layoutView = mainInflater.inflate(R.layout.thread_simple_1_layout, null);
    	setContentView(layoutView);
    	
       Button btnThreadResult = (Button)layoutView.findViewById(R.id.btn_thread_result);	
       Button uiThreadIncrementBtn = (Button)layoutView.findViewById(R.id.btn_ui_thread_increment);
       //������ On/Off
       final ToggleButton threadToggle = (ToggleButton)layoutView.findViewById(R.id.btn_thread_toggle);
       //textOn : �޼��� ǥ��
       threadToggle.setChecked(true);
       
       final EditText mainEdit = (EditText)layoutView.findViewById(R.id.main_edit);
       final EditText backEdit1 = (EditText)layoutView.findViewById(R.id.back_edit_1);
       final EditText backEdit2 = (EditText)layoutView.findViewById(R.id.back_edit_2);
       
       //��׶��� �����带 On/Off ��
       threadToggle.setOnClickListener(new View.OnClickListener(){
       	  @Override
       	  public void onClick(View threadButton){
	       		if(!threadToggle.isChecked()){
	       			/*
	       			 *  Thread�� ���� ��Ŵ(1��/3��)
	       			 */
	       			backGroundThread1 = new SimpleThreadOne("BACK_GROUND_THREAD_1", 1000);
	       			backGroundThread2 = new SimpleThreadTwo("BACK_GROUND_THREAD_2", 3000);
	       			backGroundThread1.start();
	       			backGroundThread2.start();
	       			((TextView)findViewById(R.id.textView1)).setText(" ������ ��ŸƮ ��! ");
	       			threadToggle.setChecked(false);
	       		}else{
	       		   mainEdit.setText("");
	 			   backEdit1.setText(backGroundThread1.getName() + " �����!");
	 			   backEdit2.setText(backGroundThread2.getName() + " �����!" );
	       		   backGroundThreadFinish();
	       		   ((TextView)findViewById(R.id.textView1)).setText(" �� �׶��� ������ ���� ��! ");
	       		   threadToggle.setChecked(true);
	       		   Toast.makeText(ThreadSimpleActivity.this, "Back Ground Thread�� ���� �˴ϴ� ",Toast.LENGTH_SHORT).show();
	       	    }
       	  }
       });
       //��ġ �� �� ���� Main-Thread���� 1�� ���� ��
       uiThreadIncrementBtn.setOnClickListener(new View.OnClickListener(){
      	  @Override
      	  public void onClick(View threadButton){
      		  ++mainThreadValue;
      		  Toast.makeText(ThreadSimpleActivity.this, "Main_UI������ Ŭ���� ���� 1�� ���� �մϴ�",Toast.LENGTH_SHORT).show();
      	  }
         });
       //Main�� Back Ground���� ���� ������ ���� ��� ��
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
	//Back Key �Ǵ� Activity�� Fore Ground�� �� �� ����
	@Override
	public void onPause(){
		super.onPause();
		backGroundThreadFinish();
	}
	/*
	 *  �� �׶��� �����带 ���� ��Ŵ
	 */
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
	private class SimpleThreadOne extends Thread{
		private long delayTime;
		private boolean threadFinishFlag;
		public SimpleThreadOne(String threadName,int delayTime){
			super(threadName);
			this.delayTime = delayTime;
		}
		public void run(){
			while(!threadFinishFlag){
				try{
					sleep(delayTime);
					++backThreadValue1;
				}catch(InterruptedException ie){
					Log.e("InterruptedTag", getName() + " Thread ���ͷ�Ʈ �߻�!");
					threadFinishFlag = true;
				}
			}
		}
		
	}
	private class SimpleThreadTwo extends Thread{
		private long delayTime;
		private boolean threadFinishFlag;
		public SimpleThreadTwo(String threadName,int delayTime){
			super(threadName);
			this.delayTime = delayTime;
		}
		public void run(){
			while(!threadFinishFlag){
				try{
					sleep(delayTime);
					++backThreadValue2;
				}catch(InterruptedException ie){
					Log.e("InterruptedTag", getName() + " Thread ���ͷ�Ʈ �߻�!");
					threadFinishFlag = true;
				}
			}
		}
	}
}