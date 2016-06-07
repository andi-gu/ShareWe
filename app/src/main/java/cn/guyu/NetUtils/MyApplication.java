package cn.guyu.NetUtils;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application{

	public static RequestQueue queues;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
		.showImageForEmptyUri(com.example.fragmenttabhost.R.drawable.ic_launcher) //
		.showImageOnFail(com.example.fragmenttabhost.R.drawable.ic_launcher) //
		.cacheInMemory(true) //
		.cacheOnDisk(true) //
		.build();//
		
ImageLoaderConfiguration config = new ImageLoaderConfiguration//
.Builder(getApplicationContext())//
		.defaultDisplayImageOptions(defaultOptions)//
		.discCacheSize(50 * 1024 * 1024)//
		.discCacheFileCount(100)// 缓存一百张图片
		.writeDebugLogs()//
		.build();//
ImageLoader.getInstance().init(config);
		
		
		queues = Volley.newRequestQueue(getApplicationContext());
	}
	public static RequestQueue getHttpQueues(){
		return queues;
		
	}
}
