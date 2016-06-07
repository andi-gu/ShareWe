package com.guyu.fragmenttabhost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.NetUtils.NetUtils;
import cn.guyu.entity.Users;
import cn.guyu.util.CustomProgressDialog;

import com.example.fragmenttabhost.R;
import com.google.gson.Gson;

public class LoginActivity extends Activity implements OnClickListener {

	@SuppressWarnings("unused")
	private String TAG = "LoginActivity";
	TextView tvback, tvzhuce;
	Button chongzBt, loginBt;
	private CustomProgressDialog progressDialog = null;
	EditText idEd, passwordEd;
	LinearLayout linearLayoutBack;
	private SharedPreferences preferences;
	private Editor editor;
	private String name;
	
	final private int SUCCESS = 0;
	final private int ERROR = 1;
	final private int WRONG = 2;

	@SuppressLint("HandlerLeak")
	
	private Handler handler = new Handler() {
		private String TAG;
		private String sex;
		private String image;

		/* 接收消息 */

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Log.i(TAG, "what = " + msg.what);
			if (msg.what == SUCCESS) {
				
				// 携带参数保存到本地XML中
				stopProgressDialog();
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				Users user =(Users) msg.obj;
				name =  user.getUsername();
			 	sex = user.getUsersex();
			 	image = user.getUserimge();
			 	//登录成功保存当前用户信息
			 	editor.putString("uimage", image);
			 	editor.putString("uname", name);
			 	editor.putString("usex", sex);
			 	editor.putString("uid", user.getUserid());
			 	System.out.println(user.getUserid()+"-----------"+name);
			 	
			 	//设置标识验证登录状态
			 	editor.putInt("uflag", 1);
				editor.commit();
				/*editor.putString("uid", (String) msg.obj);
				editor.putInt("uflag", 1);
				editor.commit();*/

				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				startActivity(intent);

				finish();
			} else if (msg.what == WRONG) {
				Toast.makeText(LoginActivity.this, "账号或密码不正确",
						Toast.LENGTH_SHORT).show();
			} else if (msg.what == ERROR) {
				Toast.makeText(LoginActivity.this, "登录超时", Toast.LENGTH_SHORT)
						.show();
			}
			 stopProgressDialog();
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.login_view);
		idEd = (EditText) findViewById(R.id.et_d_id);
		passwordEd = (EditText) findViewById(R.id.et_d_mima);
		/*linearLayoutBack = (LinearLayout) findViewById(R.id.tv_back_btn);*/
		tvzhuce = (TextView) findViewById(R.id.tv_zhuce);
		chongzBt = (Button) findViewById(R.id.chongz1);
		loginBt = (Button) findViewById(R.id.login1);

		/*linearLayoutBack.setOnClickListener(this);*/
		tvzhuce.setOnClickListener(this);
		chongzBt.setOnClickListener(this);
		loginBt.setOnClickListener(this);

		// 定义采用SharedPreferences方式存储数据
		preferences = getSharedPreferences("Login", MODE_PRIVATE);
		editor = preferences.edit();
		
		name = preferences.getString("uname", "");
		
		// 从本地获取登录的账号，如果不为空。显示登录状态
		int flag	= preferences.getInt("uflag", -1);
		if (flag > 0) {
			Toast.makeText(LoginActivity.this, "当前已处于登录状态", Toast.LENGTH_SHORT)
					.show();
			finish();
		}

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		/*case R.id.tv_back_btn:
			
			 * intent.setClass(LoginActivity.this, MainActivity.class);
			 * startActivity(intent);
			 
			finish();
			break;*/

		case R.id.tv_zhuce:
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.chongz1:
			idEd.setText("");
			passwordEd.setText("");
			break;
		case R.id.login1:

			final String id = idEd.getText().toString();
			final String password = passwordEd.getText().toString();

			if (id.equals("") || password.equals("")) {
				Toast.makeText(this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
			} else { 
				//Looper.prepare();
				 startProgressDialog();
				new Thread(new Runnable() {
					Gson gson = new Gson();
					@Override
					public void run() {
						
						final String state = NetUtils.loginOfPost(id, password);
						String stateFlag =	NetUtils.SelectByIdPost(id);
						
						
						if ((state != null) && (stateFlag != null)) {
							 Users u= gson.fromJson(stateFlag, Users.class);
							if (state.equals("success")) {
								
								Message msg = new Message();
								msg.what = SUCCESS;
								//	msg.obj = id;
									msg.obj = u;
								handler.sendMessage(msg);

							} else {
								System.out.println("state");
								Message msg = new Message();
								msg.what = WRONG;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = ERROR;
							handler.sendMessage(msg);
						}

					}
				}).start();

			}

		default:
			break;
		}
	}

	public void startProgressDialog(){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
	    	progressDialog.setMessage("登录中...");
		}
		
    	progressDialog.show();
	}
	
	public  void stopProgressDialog(){
		if(progressDialog!= null){
			progressDialog.dismiss();
			progressDialog = null ;
		}
	  
  }
	
	
	
	 public boolean onTouchEvent(MotionEvent event) {
	        if(null != this.getCurrentFocus()){
	            /**
	             * 点击空白位置 隐藏软键盘
	             */
	            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	        }
	        return super.onTouchEvent(event);

}
	 

}