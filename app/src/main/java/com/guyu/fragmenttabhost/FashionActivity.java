package com.guyu.fragmenttabhost;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.fragmenttabhost.R;
import com.guyu.fragmenttabhost.LoadMoreListView.OnLoadMore;


public class FashionActivity extends Activity implements OnRefreshListener, OnLoadMore  {

	private LoadMoreListView listView;
	private SwipeRefreshLayout swip;
	private int page = 1;
	ArrayList<String> data1 = new ArrayList<String>();
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fashion_view);
		initView();
		initData(1);
	
	}
	private void initView() {
		listView = (LoadMoreListView) findViewById(R.id.listView);
		listView.setLoadMoreListen(this);
		swip = (SwipeRefreshLayout) findViewById(R.id.swip_index);
		swip.setOnRefreshListener(this);
	
		swip.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data1);
	}
	
	private void initData(int page2) {
		// TODO Auto-generated method stub
		if(page2==1){
			for (int i = 0; i < 10; i++) {
				data1.add((i+1)+"");
				listView.setAdapter(adapter);
			}
		}else{
			data1.add("新加的第"+(9+page2)+"个");
		}
	}
	
	@Override
	public void onRefresh() {
		Toast.makeText(this, "开始刷新啦", Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(swip.isShown()){
					swip.setRefreshing(false);
				}
				Toast.makeText(FashionActivity.this, "刷新完成了", Toast.LENGTH_SHORT).show();
			}
		}, 3000);
	}
	
	
	
	
	
	public void loadMore() {
		page++;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				initData(page);
				listView.onLoadComplete();
				Toast.makeText(FashionActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
				adapter.notifyDataSetChanged();
			}
		}, 3000);
	}
	
	
	
	
	
	

}
