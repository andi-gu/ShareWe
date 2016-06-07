package com.guyu.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fragmenttabhost.R;
import com.guyu.fragmenttabhost.FashionActivity;
import com.guyu.fragmenttabhost.LoginActivity;
import com.guyu.fragmenttabhost.MainActivity;
import com.guyu.fragmenttabhost.RegisterActivity;

public class FragmentHome extends Fragment {

	private ViewPager viewPager;
	private LinearLayout pointGroup, looks, show, shop, fashion, me;
	private TextView imageDes, DateShow, idShow;
	private SimpleDateFormat sdf;
	ImageView settingBtn, loginbtn, register;
	// Button setting;
	// final Context context = getActivity();

	// 设置一个标志为真（控制原点显示的）
	private boolean flag = true;

	// 图片的资源ID
	private final int[] imageIds = { R.drawable.cc, R.drawable.ff,
			R.drawable.pp, R.drawable.qq, R.drawable.vv};
	private final String[] imageDescriptions = { "如果我在上海遇见你", "普罗旺斯之恋·薰衣草",
			"世界那么大，你是否愿意跟我去看看", "生活不止眼前，还有诗和远方", "在最美的地方遇见你"};

	private ArrayList<ImageView> imageList;
	// 上一个页面
	private int lastPointPosition;
	View view;

	// public FragmentTabHost mTabHost;

	private SharedPreferences preferences;
	private MainActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		
		/*
		 * 自动循环 此时又一下方法 1.定时器 Timer 2.开启子线程 while true 循环 3.ColckManger
		 * 4_用handler发送延时信息，实现循环
		 */
		isRunning = true;
		handler.sendEmptyMessageDelayed(0, 4000);

		preferences = getActivity().getSharedPreferences("Login",
				Context.MODE_PRIVATE);

	}

	/*
	 * 单独写一个Date方法，可供其他需要调用 private String getDate(){ return new
	 * SimpleDateFormat("yyyy年MM月d日 HH:mm:ss EEE").format(new Date()); }
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragmenthome, null);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		pointGroup = (LinearLayout) view.findViewById(R.id.point_group);
		imageDes = (TextView) view.findViewById(R.id.msg);
		// 获取时间布局位置
		DateShow = (TextView) view.findViewById(R.id.tv_date_show);
		// 格式化日期
		sdf = new SimpleDateFormat("yyyy年MM月d日 EEE", Locale.getDefault());
		String nowDate = sdf.format(new Date());
		DateShow.setText(nowDate);
		idShow = (TextView) view.findViewById(R.id.id_show);

		// 默认值是CoolShow 获取的是 id
		int  idflag = preferences.getInt("uflag", -1);
		if(idflag > 0){
			String uid = preferences.getString("uid", "Cool");
			idShow.setText(uid + "");
		}
		

		/*
		 * Intent intent = getIntent(); Bundle bundle =
		 * intent.getBundleExtra("data");
		 * idShow.setText(bundle.getString("uid"));
		 */
		// Bundle bundle=getArguments();
		// String od=bundle.getString("uid");
		// idShow.setText(od);
		/*
		 * Bundle dataBundle = getActivity().getIntent().getExtras(); String id
		 * = dataBundle.getString("uid"); idShow.setText(id);
		 */

		// setting = (Button) view.findViewById(R.id.iv_setting_btn);
	//	settingBtn = (ImageView) view.findViewById(R.id.iv_setting_btn);
		loginbtn = (ImageView) view.findViewById(R.id.iv_login_btn);
		register = (ImageView) view.findViewById(R.id.iv_register_btn);
		looks = (LinearLayout) view.findViewById(R.id.linearLayout_looks);
		show = (LinearLayout) view.findViewById(R.id.linearLayout_show);
		shop = (LinearLayout) view.findViewById(R.id.linearLayout_shop);
		fashion = (LinearLayout) view.findViewById(R.id.linearLayout_fashion);
		me = (LinearLayout) view.findViewById(R.id.linearLayout_me);
		init();

		// 刚出来设置为第一条内容
		imageDes.setText(imageDescriptions[0]);

		imageList = new ArrayList<ImageView>();
		for (int i = 0; i < imageIds.length; i++) {

			// 初始化图片资源
			ImageView image = new ImageView(getActivity());// 在fragment中获得上下文对象用的是getActivity()
			image.setBackgroundResource(imageIds[i]);
			imageList.add(image);

			// 添加指示点
			ImageView point = new ImageView(getActivity());

			LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(5,5);
			Params.rightMargin = 20;
			point.setLayoutParams(Params);

			point.setBackgroundResource(R.drawable.point_bg);
			point.setEnabled(false);
			if (i == 0) {

				point.setEnabled(flag);
				flag = false;
			} else {
				point.setEnabled(false);
			}
			pointGroup.addView(point);
		}

		// 设置适配器（一个位置给一个view）
		viewPager.setAdapter(new MyPagerAdapter(imageList));
		// 双向循环
		viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
				% imageList.size());
		// 添加一个事件监听
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			/*
			 * 页面切换后调用 position 新的页面位置
			 */
			@Override
			public void onPageSelected(int position) {
				// 循环转换图片
				position = position % imageList.size();
				// 设置文字描述
				imageDes.setText(imageDescriptions[position]);
				/*
				 * 改变指示点状态 把当前点enbale 为true
				 */
				pointGroup.getChildAt(position).setEnabled(true);
				/* 把上一个点设为flase */
				pointGroup.getChildAt(lastPointPosition).setEnabled(false);

				lastPointPosition = position;
			}

			/*
			 * 页面不断滑动的时候，回调 （不常用）
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			/*
			 * 当页面状态发生改变的时候，回调 （不常用）
			 */
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		return view;
	}

	public void init() {

		activity = (MainActivity) getActivity();
		
		/*// 设置按钮点击事件

		settingBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SettingActivity.class);
				FragmentHome.this.startActivity(intent);

			}
		});*/
		
		// 登陆按钮点击事件

		loginbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();

				intent.setClass(getActivity(), LoginActivity.class);
				FragmentHome.this.startActivity(intent);

			}
		});
		// 注册按钮点击事件

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), RegisterActivity.class);
				FragmentHome.this.startActivity(intent);

			}
		});
		// Looks/美文

		looks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				activity.mTabHost.setCurrentTab(1);

			}
		});
		// share We

		shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				activity.mTabHost.setCurrentTab(2);

			}
		});
		// 开心一刻

		show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				activity.mTabHost.setCurrentTab(3);

			}
		});
		// 关于我

		fashion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				activity.mTabHost.setCurrentTab(4);
				

			}
		});
		// setting

		me.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.setClass(getActivity(), FashionActivity.class);
				FragmentHome.this.startActivity(intent);
			}
		});

	}

	/*
	 * 判断自带滚动
	 */
	private boolean isRunning = false;
	/*
	 * This Handler class should be static or leaks might occu叹号的时候会容易造成内存泄漏
	 * Message msg
	 */
	private Handler handler = new Handler(new Handler.Callback()

	{
		@Override
		public boolean handleMessage(Message msg) {
			// 让viewPager滑动到下一页
			try {
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			} catch (Exception e) {

				System.out.println("异常输出是"+e.toString());
				e.printStackTrace();
			}
			if (isRunning) {
				handler.sendEmptyMessageDelayed(0, 2000);
			}
			return isRunning;
		}
	});

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRunning = false;
	}

}