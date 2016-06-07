package com.guyu.fragmenttabhost;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.guyu.NetUtils.NetUtils;
import cn.guyu.util.CustomProgressDialog;

import com.example.fragmenttabhost.R;

public class RegisterActivity extends Activity {
	
	private SharedPreferences preferences;
	private Editor editor;
	LinearLayout linearLayoutBack;
	/* TextView tvback; */
	EditText etusername, etid, etpassword, etzcpassword;
	Button regbutton;
	String state;
	private String TAG = "RegisterActivity";
	private CustomProgressDialog progressDialog = null;

	final private int SUCCESS = 0;
	final private int ERROR = 1;
	final private int WRONG = 2;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.i(TAG, "what = " + msg.what);
			if (msg.what == SUCCESS) {
				stopProgressDialog();
				Toast.makeText(RegisterActivity.this, "注册成功|请完善信息",
						Toast.LENGTH_LONG).show();   //	/*msg.obj*/
				//注册成功将保存到本地
				String  id = (String) msg.obj;
				editor = preferences.edit();
				editor.putString("uid", id );
				editor.commit();
				//保存完id跳转到完善信息页面
				Intent intent = new Intent();
				intent.setClass(RegisterActivity.this, MyActivity.class);
			/*	intent.putExtra("id",id);  刚开始想的是传值注册到注册完善信息*/
				intent.putExtra("zhuce", "okLogin");
				startActivity(intent);
				finish();
			} else if (msg.what == WRONG) {
				Toast.makeText(RegisterActivity.this, "注册失败,或者该账户已存在",
						Toast.LENGTH_SHORT).show();

			} else if (msg.what == ERROR) {
				Toast.makeText(RegisterActivity.this, "网络超时",
						Toast.LENGTH_SHORT).show();
  
			}
			stopProgressDialog();
		}

	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_view);
		linearLayoutBack = (LinearLayout) findViewById(R.id.tv_back_btn);

		// 不获取昵称了
		// etusername = (EditText) findViewById(R.id.et_name);
		etid = (EditText) findViewById(R.id.et_id);
		etpassword = (EditText) findViewById(R.id.et_mima);
		etzcpassword = (EditText) findViewById(R.id.et_zcmima);
		regbutton = (Button) findViewById(R.id.regbutton);
		
		// 从本地获取登录的账号，如果不为空。显示登录状态
		preferences = getSharedPreferences("Login", MODE_PRIVATE);
	
		int flag = preferences.getInt("uflag", -1);
		if (flag > 0) {
			Toast.makeText(RegisterActivity.this, "当前已处于登录状态",
					Toast.LENGTH_SHORT).show();
			/*
			 * Intent intent = new Intent();
			 * intent.setClass(RegisterActivity.this, MainActivity.class);
			 * startActivity(intent);
			 */
			finish();
		}

		linearLayoutBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Intent intent = new Intent();
				 * intent.setClass(RegisterActivity.this, MainActivity.class);
				 * startActivity(intent);
				 */
				finish();
			}
		});

		regbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// final String username = etusername.getText().toString();
				final String username = "";
				final String id         = etid.getText().toString();
				final String password   = etpassword.getText().toString();
				final String zcpassword = etzcpassword.getText().toString();

				System.out.println("id=" + id + "&password=" + password
						+ ">注册的账号密码>>>>>>>>>>>>>>>>");

				if (id.equals("") || password.equals("")
						|| zcpassword.equals("")) {
					Toast.makeText(RegisterActivity.this, "注册信息不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					if (password.equals(zcpassword)) {

						/*
						 * new Thread(new Runnable() {
						 * 
						 * @Override public void run() {
						 * 
						 * //参数中昵称为空 、后期将改变方法为NetUtils.addOfGet(id,password);
						 * state = NetUtils.addOfGet(username,id,password);
						 * 
						 * 
						 * //执行任务在主线程中 runOnUiThread(new Runnable() {
						 * 
						 * @Override public void run() {
						 * //Toast.makeText(RegisterActivity.this, state+"~~",
						 * Toast.LENGTH_SHORT).show();
						 * 
						 * if(state.equals("success")){
						 * Toast.makeText(RegisterActivity.this,"注册成功|请完善信息",
						 * 1).show(); Intent intent = new Intent();
						 * intent.setClass(RegisterActivity.this,
						 * MyActivity.class); startActivity(intent); finish(); }
						 * else{ Toast.makeText(RegisterActivity.this,
						 * "注册失败,或者该账户已存在", Toast.LENGTH_SHORT).show(); }
						 * 
						 * } }); } }).start();
						 */
						// 换一种方法——————————————————————————————
						startProgressDialog();
						new Thread(new Runnable() {

							@Override
							public void run() {
								// 参数中昵称为空
								// 、后期将改变方法为NetUtils.addOfGet(id,password);
								final String state = NetUtils.addOfGet(
										username, id, password);
								if (state != null) {

									if (state.equals("success")) {
										Message msg = new Message();
										msg.what = SUCCESS;
										msg.obj  = id ;
										handler.sendMessage(msg);
									} else {
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

					} else {
						Toast.makeText(RegisterActivity.this, "两次密码不同",
								Toast.LENGTH_SHORT).show();
					}

				}
			}

		});

	}
	
	
	public void startProgressDialog(){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(this);
	    	progressDialog.setMessage("正在注册...");
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
	 

	public void init() {

	}

}
