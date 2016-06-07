package com.guyu.fragmenttabhost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmenttabhost.R;
import com.guyu.fragment.FragmentHome;
import com.guyu.fragment.FragmentLooks;
import com.guyu.fragment.FragmentMe;
import com.guyu.fragment.FragmentShop;
import com.guyu.fragment.FragmentShow;


public class MainActivity extends FragmentActivity {
	
	    final private int  SUCCESS  = 0;
	    final private int  ERROR  = 1;
	/**
	 * FragmentTabhost
	 */
	public FragmentTabHost mTabHost;

	/**
	 * 布局填充器
	 * 
	 */
	private LayoutInflater mLayoutInflater;

	/**
	 * Fragment数组界面
	 * 
	 */
	private Class<?> mFragmentArray[] = { FragmentHome.class, FragmentLooks.class,
			FragmentShow.class, FragmentShop.class, FragmentMe.class };
	/**
	 * 存放图片数组
	 * 
	 */
	private int mImageArray[] = { R.drawable.tab_home_btn,
			R.drawable.tab_looks_btn, R.drawable.tab_show_btn,
			R.drawable.tab_shop_btn, R.drawable.tab_me_btn };

	/**
	 * 选修卡文字 
	 */
	private String mTextArray[] = { "首页", "文摘", "动态圈", "段子", "我的" };
	
	

	
	/*Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == SUCCESS) {
			
				mTabHost.setCurrentTab(4);
				System.out.println("收到跳转到（我的信息）wwwwwwwww");
				//finish();
				//MainActivity.this.finishAffinity();
			}else if(msg.what == ERROR){
				Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
			}
			
		}	
	};*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		
		System.out.println("MainActivity——————开始启动主MainActivity00000000000000000000000000000000000");

		
	/*	
		new Thread(new Runnable() {
			//接收传来的值
			Intent intent = getIntent();
		@Override
			public void run() {
				//获得字符串
				String id = intent.getStringExtra("data");
				if(id != null){
						if(id.equals("guyu")){
							Message  msg = new Message();
							msg.what  = SUCCESS;
							handler.sendMessage(msg);
						}
					else {
						Message msg = new Message();
						msg.what  = ERROR;
						handler.sendMessage(msg);
					}
				}
			}
		}).start();
		
		*/
		//获取来自我的头像页面的值，判断是否完成修改信息
		Intent intent = getIntent();
		String id = intent.getStringExtra("data");
		String idfb = intent.getStringExtra("datafb");
		initView();
		if(idfb != null){
			if(idfb.equals("fbok")){
				mTabHost.setCurrentTab(2);
			}
		}
		
		
		if(id != null){
		if(id.equals("guyu")){
			mTabHost.setCurrentTab(4);
		}
		}
		

	/*Bundle bundle = intent.getBundleExtra("data");
		String id =	bundle.getString("uid");
		FragmentHome  fh = new  FragmentHome();
		bundle.putString("vid", id);
		fh.setArguments(bundle);*/
		//mTabHost.setCurrentTab(0);
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		mLayoutInflater = LayoutInflater.from(this);

		// 找到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		// 得到fragment的个数
		int count = mFragmentArray.length;
		for (int i = 0; i < count; i++) {
			// 给每个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, mFragmentArray[i], null);
			
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);
		}
		
	}

	/**
	 *
	 * 给每个Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index) {
		View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageArray[index]);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(mTextArray[index]);
		return view;
	}

	//第一次点击返回按钮，提示“请在按一次返回退出”  两秒内再次按下  退出
	private long temptime=System.currentTimeMillis();
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)//主要是对这个函数的复写
	{
		// TODO Auto-generated method stub 
 
		if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getAction() == KeyEvent.ACTION_DOWN))
		{
			if(System.currentTimeMillis() - temptime >2000) // 2s内再次选择back键有效
			{
				System.out.println(Toast.LENGTH_LONG);
				Toast.makeText(this, "请在按一次返回退出", Toast.LENGTH_SHORT).show();
				temptime = System.currentTimeMillis();
			}
			else {
			
					MainActivity.this.finishAffinity();
					
				//关闭所有亲近的Activity；
				//	finish(); 
			  //  System.exit(0); //凡是非零都表示异常退出!0表示正常退出!
			}
            
			return true; 

		}
		return super.onKeyDown(keyCode, event);
	}
	
	

	
}
