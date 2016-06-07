package com.guyu.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.NetUtils.NetUtils;
import cn.guyu.entity.NewInfo;
import cn.guyu.util.CustomProgressDialog;

import com.example.fragmenttabhost.R;
import com.guyu.fragmenttabhost.WebDIYActivity;
import com.loopj.android.image.SmartImageView;

public class FragmentLooks extends Fragment{
	
private View view;
private final int SUCCESS = 1;;
private final int FAILED = 0;
private ListView lvNews;
private List<NewInfo> newInfoList;
private MyAdapter adapter;
private NewInfo newInfo;
private boolean flagNet;
private CustomProgressDialog progressDialog = null;

private  Handler handler = new Handler(){

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message msg) {
		
		super.handleMessage(msg);
		
		switch(msg.what){
		case SUCCESS:
		 stopProgressDialog();
		 newInfoList = (List<NewInfo>) msg.obj;
		 adapter =	new MyAdapter(newInfoList,lvNews);
	     lvNews.setAdapter(adapter);
	     adapter.notifyDataSetChanged();
	     System.out.println(newInfoList.size());
			break;
		case FAILED:
			stopProgressDialog();
			Toast.makeText(getActivity(), "文摘|当前网络崩溃了 ", 0).show();
			break;
		default:
			break;
		}
		
	}
	
	


};




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		view = inflater.inflate(R.layout.fragmentlook, null);
	     lvNews =	(ListView) view.findViewById(R.id.lv_newss);
	     flagNet = pandunnet();
	     if(flagNet){
	    	    
	   startProgressDialog();
       new Thread(new Runnable() {
	
		public void run() {
			 newInfoList = NetUtils.getNewsFromIntent();
			Message msg = new Message();
			if(newInfoList != null){
				msg.what = SUCCESS;
				msg.obj = newInfoList;
				handler.sendMessage(msg);
				System.out.println("你看见了吗");
			}else {
				msg.what = FAILED;
				handler.sendMessage(msg);
			}
	 		
		}
	}).start();
 }else {
	 stopProgressDialog();
	Toast  t = Toast.makeText(getActivity(), "无网络",Toast.LENGTH_SHORT);
	 t.setGravity(Gravity.TOP, 0, 147);
	 t.show();
}  
       lvNews.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {

			newInfo = newInfoList.get(position);
			Intent intent = new Intent(getActivity(),WebDIYActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("diy", newInfo.getContentUrl());
			bundle.putString("title","最美分享");
			intent.putExtras(bundle);
			startActivity(intent);
			
		}
    	   
       });
       
       
	  return view;
		
		
	}
	class MyAdapter extends BaseAdapter{
		
		private List<NewInfo> newinfos;
		

		public MyAdapter(List<NewInfo> newinfos,ListView listview){
			this.newinfos=newinfos;
			
		}

		@Override
		public int getCount() {
			
			return newinfos.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView == null){
				LayoutInflater inflater =	getActivity().getLayoutInflater();
				view = inflater.inflate(R.layout.listview_item, null);
			}else {
				view = convertView;
			}
			SmartImageView sivIcon = (SmartImageView) view.findViewById(R.id.siv_listview_item_icon);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_listview_title);
			TextView tvDetail = (TextView)view.findViewById(R.id.tv_listview_datail);
		
			newInfo = newinfos.get(position);
		   	sivIcon.setImageUrl(newInfo.getImageUrl());
			tvTitle.setText(newInfo.getTitle());
			tvDetail.setText(newInfo.getDetail());
		
			
			return view;
		}
		@Override
		public Object getItem(int position) {
		
			return newinfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		
		
	}
	private boolean pandunnet() {
		// 判断网络
		boolean networkState = cn.guyu.util.PanduanNet.detect(getActivity());
		return networkState;
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
	
}