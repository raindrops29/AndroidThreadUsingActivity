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
	//백그라운드에서 돌아 갈 쓰레드 선언
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
       //쓰레드 On/Off
       final ToggleButton threadToggle = (ToggleButton)layoutView.findViewById(R.id.btn_thread_toggle);
       //textOn : 메세지 표시
       threadToggle.setChecked(true);
       
       final EditText mainEdit = (EditText)layoutView.findViewById(R.id.main_edit);
       final EditText backEdit1 = (EditText)layoutView.findViewById(R.id.back_edit_1);
       final EditText backEdit2 = (EditText)layoutView.findViewById(R.id.back_edit_2);
       
       //백그라운드 쓰레드를 On/Off 함
       threadToggle.setOnClickListener(new View.OnClickListener(){
       	  @Override
       	  public void onClick(View threadButton){
	       		if(!threadToggle.isChecked()){
	       			/*
	       			 *  Thread를 생성 시킴(1초/3초)
	       			 */
	       			backGroundThread1 = new SimpleThreadOne("BACK_GROUND_THREAD_1", 1000);
	       			backGroundThread2 = new SimpleThreadTwo("BACK_GROUND_THREAD_2", 3000);
	       			backGroundThread1.start();
	       			backGroundThread2.start();
	       			((TextView)findViewById(R.id.textView1)).setText(" 쓰레드 스타트 됨! ");
	       			threadToggle.setChecked(false);
	       		}else{
	       		   mainEdit.setText("");
	 			   backEdit1.setText(backGroundThread1.getName() + " 종료됨!");
	 			   backEdit2.setText(backGroundThread2.getName() + " 종료됨!" );
	       		   backGroundThreadFinish();
	       		   ((TextView)findViewById(R.id.textView1)).setText(" 백 그라운드 쓰레드 종료 됨! ");
	       		   threadToggle.setChecked(true);
	       		   Toast.makeText(ThreadSimpleActivity.this, "Back Ground Thread가 종료 됩니다 ",Toast.LENGTH_SHORT).show();
	       	    }
       	  }
       });
       //터치 할 때 마다 Main-Thread에서 1씩 증가 함
       uiThreadIncrementBtn.setOnClickListener(new View.OnClickListener(){
      	  @Override
      	  public void onClick(View threadButton){
      		  ++mainThreadValue;
      		  Toast.makeText(ThreadSimpleActivity.this, "Main_UI에서는 클릭시 마다 1씩 증가 합니다",Toast.LENGTH_SHORT).show();
      	  }
         });
       //Main과 Back Ground에서 각각 증가된 값을 출력 함
       btnThreadResult.setOnClickListener(new View.OnClickListener(){
    	  @Override
    	  public void onClick(View threadButton){
    		 mainEdit.setText(Thread.currentThread().getName() + " 클릭 횟수  => " + mainThreadValue);  
    		 if( backGroundThread1 != null &&  backGroundThread2 != null &&
    				 backGroundThread1.isAlive() && backGroundThread2.isAlive() ){
    			   backEdit1.setText(backGroundThread1.getName() + " 증가값  => "  + backThreadValue1);
    			   backEdit2.setText(backGroundThread2.getName() + " 증가값  => "  + backThreadValue2);
    	     }else{
    	    	 Toast.makeText(getApplicationContext(), "백그라운드 쓰레드가 활성화 되지 않았네요", Toast.LENGTH_LONG).show();
    	     }
    	  }
       });
    }
	//Back Key 또는 Activity가 Fore Ground로 갈 때 동작
	@Override
	public void onPause(){
		super.onPause();
		backGroundThreadFinish();
	}
	/*
	 *  백 그라운드 쓰레드를 종료 시킴
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
					Log.e("InterruptedTag", getName() + " Thread 인터럽트 발생!");
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
					Log.e("InterruptedTag", getName() + " Thread 인터럽트 발생!");
					threadFinishFlag = true;
				}
			}
		}
	}
}