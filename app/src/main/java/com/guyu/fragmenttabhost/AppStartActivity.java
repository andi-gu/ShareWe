package com.guyu.fragmenttabhost;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;

import com.example.fragmenttabhost.R;

	
public class AppStartActivity extends Activity {
	
	SharedPreferences.Editor edit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		//没有titlebar
				requestWindowFeature(Window.FEATURE_NO_TITLE);
		  // final View view = View.inflate(this, R.layout.start_activity, null);
		   setContentView(R.layout.start_activity);
	
		  
			
		   
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				 
			SharedPreferences  Preferences = getSharedPreferences("ssText", Activity.MODE_PRIVATE);
			edit = Preferences.edit();
			edit.putInt("textFlag", 1);
			edit.commit();
			int a =	Preferences.getInt("textFlag", 1);
			if(a == 1){
				edit.putString("textS", "");
				edit.commit();
				}
				
				
				redirectTo();
				
			}
		}, 3000);
		
	}
		/**
				     * 跳转到...
				     */
					private void redirectTo() {
						 Intent intent = new Intent(AppStartActivity.this,MainActivity.class);
					        startActivity(intent);
					        System.out.println("bidddddddddd-----");
					        finish();
						
					}
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if ((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getRepeatCount() == 0)) {
				  return false;
				  
				  }
			return false;
		}
					
					
				

}
