package com.guyu.fragmenttabhost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.example.fragmenttabhost.R;

public class SettingActivity extends Activity {

	private LinearLayout linearLayoutBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		init();
	}

	private void init() {
		linearLayoutBack = (LinearLayout) findViewById(R.id.tv_back_btn);
		linearLayoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent();
				 * intent.setClass(SettingActivity.this, MainActivity.class);
				 * startActivity(intent);
				 */
				finish();
			}
		});
	}

}
