package com.guyu.fragmenttabhost;
import cn.guyu.NetUtils.ImageLoaderFromN;
import cn.guyu.util.MyOrientationListener;
import cn.guyu.util.MyOrientationListener.OnOrientationListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.fragmenttabhost.R;
import com.guyu.httpPath.HttpPath;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MapImageActivity extends Activity{

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private String lng,lag,mapImage;
	private double mLatitude,mlat,mLongtitude,mlon;
	private LocationClient mLocationClient;
	private Context context;
	private float mCurrentX;
		// 自定义定位图标
	private BitmapDescriptor mIconLocation;
	private MyOrientationListener myOrientationListener;
	
	private Button meMap;
	private boolean locatMe = false;
	private MyLocationLister mLocationLister;
	private RelativeLayout Rlayout;
	private BitmapDescriptor bitmap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_mapimage);
		this.context = this;
		
		meMap = (Button)findViewById(R.id.iv_map_more);
		Rlayout  = (RelativeLayout) findViewById(R.id.id_markr_ly);
		lng =	getIntent().getExtras().getString("thinglng");
		lag =	getIntent().getExtras().getString("thinglat");
		mapImage = getIntent().getExtras().getString("thingmapimg");
		mlat = Double.parseDouble(lag);
		mlon = Double.parseDouble(lng);
		initView();
		// 初始化定位
		initLocation();
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				ImageView iv = (ImageView) findViewById(R.id.id_iv_mapimage);
				Rlayout.setVisibility(View.VISIBLE);
				ImageLoaderFromN.ImageRequest(HttpPath.IpStr+mapImage, iv);
				return true;
			}
		});
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
			
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
			Rlayout.setVisibility(View.GONE);
				
			}
		});
		
		Rlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Rlayout.setVisibility(View.GONE);
				
			}
		});
		
		//我的位置
		meMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				centerToMyLocatin(mLatitude,mLongtitude);
			}
	});	
		
		
	}
	
	
	

	private void initView() {
		Marker marker =  null;
		//直接定位说说发布地点
		  mMapView =  (MapView)findViewById(R.id.id_mapView);
		  mBaiduMap = mMapView.getMap();
		 // MapStatusUpdate msu  = MapStatusUpdateFactory.zoomTo(15.0f);
		  LatLng latLng = new LatLng(mlat, mlon);
		//构建Marker图标  
		  bitmap = BitmapDescriptorFactory  
		      .fromResource(R.drawable.maker);  
		
		  
		//构建MarkerOption，用于在地图上添加Marker  
		      OverlayOptions option = new MarkerOptions()  
		      .position(latLng)  
		      .icon(bitmap);  
		  //在地图上添加Marker，并显示  
		      marker =  (Marker) mBaiduMap.addOverlay(option);
		  
	  
		  MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(latLng, 17.0f);
		  mBaiduMap.setMapStatus(msu);
		  
	}
	
	private void initLocation() {
		mLocationClient = new LocationClient(this);
		mLocationLister = new MyLocationLister();
		mLocationClient.registerLocationListener(mLocationLister);
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		// 初始化图标
				mIconLocation = BitmapDescriptorFactory
						.fromResource(R.drawable.navi_map_gps_locked);
			myOrientationListener = new MyOrientationListener(context);
			myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
				
				@Override
				public void onOrientationChanged(float x) {
					
					mCurrentX = x ;
				}
			});
		
	}
	
	
	protected void onResume()
	{
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	
	protected void onStart()
	{
		super.onStart();
		// 开启定位
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			mLocationClient.start();
		// 开启方向传感器
		myOrientationListener.start();
	}

	
	protected void onPause()
	{
		super.onPause();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onPause();
	}

	
	protected void onStop()
	{
		super.onStop();

		// 停止定位
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
		// 停止方向传感器
		myOrientationListener.stop();

	}

	
	protected void onDestroy()
	{
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}
	
	
	private class MyLocationLister implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLocationData data = new MyLocationData.Builder()//
			.direction(mCurrentX)//
			.accuracy(location.getRadius())//
			.latitude(location.getLatitude())//
			.longitude(location.getLongitude())//
			.build();
			mBaiduMap.setMyLocationData(data);
			MyLocationConfiguration config = new MyLocationConfiguration(
					com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);
			
			//更新经纬度
		 	mLongtitude = location.getLongitude();
			mLatitude = location.getLatitude();
		}
		
	}
	//定位位置
	private void centerToMyLocatin(double a,double b) {
		LatLng latLng = new LatLng(a, b);
		 MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(latLng, 15.0f);
		  mBaiduMap.setMapStatus(msu);
	}	
	
}