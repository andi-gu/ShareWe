package com.guyu.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.NetUtils.NetUtils;
import cn.guyu.entity.Things;
import cn.guyu.util.CustomProgressDialog;

import com.example.fragmenttabhost.R;
import com.guyu.imageShare.ListItemAdapter;
import com.guyu.imageXz.PublishedActivity;

public class FragmentShow extends Fragment {
	private final int SUCCESS = 1;;
	private final int FAILED = 0;
	public View view;
	private TextView tv ;
	private SharedPreferences PreferencesF;
	private int flag;
	/** ListView对象 */
	private ListView listview;
	private ListItemAdapter adapter;
	private List<Things> rulist;
	private CustomProgressDialog progressDialog = null;
	private boolean flagNet;
	
	private Handler handler  = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			
			
			switch(msg.what){
			case SUCCESS:
			stopProgressDialog();
		    rulist = (List<Things>) msg.obj;
			 adapter =	new ListItemAdapter(getActivity(), rulist);
		     listview.setAdapter(adapter);
		     adapter.notifyDataSetChanged();
		     System.out.println(rulist.size());
				break;
			case FAILED:
				stopProgressDialog();
				Toast.makeText(getActivity(), "动态圈|当前网络崩溃了 ", 0).show();
				break;
			default:
				break;
			}
			
			
			
			
			
			
			
		}
		
		
		
		
		
	};

	
	
	// 防止输入框挤压屏幕
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragmentshow, null);
		listview = (ListView) view.findViewById(R.id.listview_shareWe);
		
		
		System.out.println("---fabu---");
		PreferencesF =	getActivity().getSharedPreferences("Login", Activity.MODE_PRIVATE);
		flag	= PreferencesF.getInt("uflag", -1);
		tv = (TextView) view.findViewById(R.id.tv_fabu_btn);
		
		
		
		init();
	    flagNet = pandunnet();
	     if(flagNet){
		
		startProgressDialog();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				rulist =	NetUtils.getThingsInfo();
				Message msg = new Message();
				if(rulist != null){
					msg.what = SUCCESS;
					msg.obj = rulist;
					handler.sendMessage(msg);
					System.out.println("获取了");
				}else {
					msg.what = FAILED;
					handler.sendMessage(msg);
				}
			}
		}).start();
			
	     }else {
	    	
	    		Toast  t = Toast.makeText(getActivity(), "无网络",Toast.LENGTH_SHORT);
	    		 t.setGravity(Gravity.TOP, 0, 147);
	    		 t.show();
		}	
			
		
		
	
		
		return view;
	}

	private void init() {
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(flag <0){
					Toast.makeText(getActivity(), "登录后可发布", Toast.LENGTH_SHORT).show();
				}else {
				
				
				Intent intent = new Intent(getActivity(), PublishedActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.activity_open, 0);
				
				}
			}
		});
		
		
		
		
		
	}

	public void startProgressDialog(){
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(getActivity());
	    	progressDialog.setMessage("加载中...");
		}
		
    	progressDialog.show();
	}
	
	public  void stopProgressDialog(){
		if(progressDialog!= null){
			progressDialog.dismiss();
			progressDialog = null ;
		}
	  
  }
	
	
	private boolean pandunnet() {
		// 判断网络
		boolean networkState = cn.guyu.util.PanduanNet.detect(getActivity());
		return networkState;
	}
	
	
}
