package com.guyu.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.NetUtils.ImageLoaderFromN;
import cn.guyu.NetUtils.NetUtils;
import cn.guyu.entity.Users;

import com.example.fragmenttabhost.R;
import com.google.gson.Gson;
import com.guyu.fragmenttabhost.AboutCoolActivity;
import com.guyu.fragmenttabhost.MainActivity;
import com.guyu.fragmenttabhost.MyActivity;
import com.guyu.httpPath.HttpPath;

public class FragmentMe extends Fragment implements OnClickListener {

	private View view;
	private ImageView TouImage;
	private TextView textRoll, textname, textsex;
	private Button button;
	private SharedPreferences preferences;
	LinearLayout linearLayoutBack, linearLayoutfabu, linearLayouthuifu,
			linearLayoutzan, linearLayoutabout, linearLayoutuichu;
	private Editor edit;
	
	String 	name, sex;
	private String TouImageUrl;
	final private int SUCCESS = 0;

	final private int ERROR = 1;
		private String image;
		boolean net ;
	
		
	Handler handler = new Handler(){

	

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what){
			case SUCCESS:
				Users user =(Users) msg.obj;
			 	name =  user.getUsername();
			 	sex = user.getUsersex();
			 	image = user.getUserimge();
			 	System.out.println(image+"okokkokkkkkkkoko");
			 	edit.putString("uImageSevlet", image); 
			 	edit.commit();
			 	ImageLoaderFromN.ImageRequest(HttpPath.IpStr+image, TouImage);
			 	
			/*   else {
				   TouImage.setImageResource(R.drawable.app_logo);
			}*/
			 	
			 	
			 	if( (name!= null && sex != null)&&(name.length()>0&&sex.length()>0)){
			 		textname.setText(name);
				 	textsex.setText(sex);
				 //textRoll.setVisibility(View.GONE);
				 	}else {
				 		textRoll.setVisibility(View.VISIBLE);
					}
			 
				break;
				
			 	case ERROR:
			 	  //  ImageLoaderFromN.ImageRequest(image, TouImage);
			 		Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT).show();
			 		break;
		
			}	
		}		
	};
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*
		 * preferences = getActivity().getSharedPreferences("Login",
		 * Context.MODE_PRIVATE); edit = preferences.edit();
		 */
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragmentme, null);
		net = pandunnet();
		// 定义采用SharedPreferences方式存储数据
		preferences = getActivity().getSharedPreferences("Login",
				Context.MODE_PRIVATE);
		edit = preferences.edit();

		//获取标识判断是否登录
		int flag = preferences.getInt("uflag", -1);
    	TouImageUrl =preferences.getString("uImage", "");

		if (flag < 0) {
			Toast.makeText(getActivity(), "您还没有登录，请登录", 0).show();
			MainActivity activity = (MainActivity) getActivity();
			activity.mTabHost.setCurrentTab(0);
		}
		
		TouImage =(ImageView)view.findViewById(R.id.metoux);
		textname = (TextView) view.findViewById(R.id.tv_username);
		textsex = (TextView) view.findViewById(R.id.tv_usersex);
		textRoll = (TextView) view.findViewById(R.id.tv_roll);
		// 如果昵称和性别都不为空，则取消滚动
		/*if ((!TextUtils.isEmpty(name)) && (!TextUtils.isEmpty(sex))) {
			textRoll.setText("");textRoll
			
		}*/
		if(!net){
			textRoll.setVisibility(View.GONE);
			TouImage.setBackgroundResource(R.drawable.app_logo);
			Toast.makeText(getActivity(), "无网络", Toast.LENGTH_SHORT).show();
		}

		button = (Button) view.findViewById(R.id.framegeren);
		linearLayoutfabu = (LinearLayout) view
				.findViewById(R.id.linearLayout_fabu);
		linearLayouthuifu = (LinearLayout) view
				.findViewById(R.id.linearLayout_huifu);
		linearLayoutzan = (LinearLayout) view
				.findViewById(R.id.linearLayout_zan);
		linearLayoutabout = (LinearLayout) view
				.findViewById(R.id.linearLayout_about);
		linearLayoutuichu = (LinearLayout) view
				.findViewById(R.id.linearLayout_tuichu);
	
		button.setOnClickListener(this);
		linearLayoutabout.setOnClickListener(this);
		linearLayoutuichu.setOnClickListener(this);
		
		
		
		if((flag > 0) && net	){
			
		final	String id = preferences.getString("uid", null);
		new Thread(new Runnable() {
			Gson gson = new Gson();
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				String  state =	NetUtils.SelectByIdPost(id);
			   
			    if(state != null && state.length() > 0){
			    	 Users u= gson.fromJson(state, Users.class);
			    	 Message msg = new Message();
						msg.what = SUCCESS;
						msg.obj = u;
						handler.sendMessage(msg);
						
			    }else {
			    	Message msg = new Message();
					msg.what = ERROR;
					handler.sendMessage(msg);
				}
			    
				
			}
		}).start();
		
		

	
	}
		return view;
}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		MainActivity activity = (MainActivity) getActivity();
		switch (v.getId()) {
		/*
		 * //返回按钮 case R.id.tv_back_btn: // activity.mTabHost.setCurrentTab(0);
		 * 
		 * break;
		 */

		// 我的信息	
		case R.id.framegeren:
			intent.setClass(getActivity(), MyActivity.class);
			
			startActivity(intent);
			break;
			
			
			
			
		// 我炫过的

		// 评论回复

		// 关于炫Cool
		case R.id.linearLayout_about:
			intent.setClass(getActivity(), AboutCoolActivity.class);
			startActivity(intent);
			break;
		// 退出按钮
		case R.id.linearLayout_tuichu:
		//	String	touImageFme= HttpPath.ImageHead_URL;
			String	touImageFme= HttpPath.ImageHead;
			edit.putString("uimage", touImageFme); 
			edit.putInt("uflag", -1);
			edit.commit();
			Toast.makeText(getActivity(), "已退出", 0).show();
			activity.mTabHost.setCurrentTab(0);
			break;
		default:
			break;
		}

	}
	
	private boolean pandunnet() {
		// 判断网络
		boolean networkState = cn.guyu.util.PanduanNet.detect(getActivity());
		return networkState;
	}
	


}