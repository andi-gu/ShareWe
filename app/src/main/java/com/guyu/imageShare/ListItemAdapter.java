package com.guyu.imageShare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.entity.Things;
import cn.guyu.util.RelativeDateFormat;

import com.example.fragmenttabhost.R;
import com.guyu.fragmenttabhost.MapImageActivity;
import com.guyu.httpPath.HttpPath;
import com.guyu.imageXz.NoScrollGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 首页ListView的数据适配器   //整个ListView
 * 
 * @author Administrator
 * 
 */
public class ListItemAdapter extends BaseAdapter {

	private Context mContext;
	private List<Things> things;
	private Date data;
	private Date date;
	
//	ArrayList<String> imageUrls;
	
	public ListItemAdapter(Context ctx, List<Things> things) {
		this.mContext = ctx;
		this.things = things;
	}

	@Override
	public int getCount() {
		return things == null ? 0 : things.size();
	}

	@Override
	public Object getItem(int position) {
		return things.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.list_item_share,null);
			holder.iv_tou =(ImageView) convertView.findViewById(R.id.image_tou_share);
			holder.tv_nicheng = (TextView) convertView.findViewById(R.id.tv_title_share);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content_share);
			holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);
			holder.tv_address =(TextView) convertView.findViewById(R.id.tv_address_share);
			holder.tv_zan =(TextView) convertView.findViewById(R.id.tv_zan_share);
			holder.tv_data =(TextView) convertView.findViewById(R.id.tv_data_share);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Things ShareThing = things.get(position);
		holder.tv_nicheng.setText(ShareThing.getUsername());
		holder.tv_content.setText(ShareThing.getThingtext());
		
		holder.tv_address.setText(ShareThing.getThingmaptext());
		
		if(ShareThing.getThingmaptext()==null || ShareThing.getThingmaptext().trim().length()==0){
			holder.tv_address.setVisibility(View.GONE);
		}else{
			holder.tv_address.setVisibility(View.VISIBLE);
			holder.tv_address.setText(ShareThing.getThingmaptext());
			final Bundle bundle=new Bundle();
			bundle.putString("thinglat",ShareThing.getThinglat());//纬度
			bundle.putString("thinglng",ShareThing.getThinglng());//经
			bundle.putString("thingmapimg",ShareThing.getThingmapimg());
			holder.tv_address.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent= new Intent(mContext, MapImageActivity.class);
					intent.putExtras(bundle);
					mContext.startActivity(intent);
					Toast.makeText(mContext," position is:"+position,Toast.LENGTH_SHORT).show();

				}
			});

		}
		
		
		holder.tv_zan.setText(ShareThing.getThingzan());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			date = format.parse(ShareThing.getThingdata());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.tv_data.setText(RelativeDateFormat.format(date));
		

		
		
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();//
		ImageLoader.getInstance().displayImage(HttpPath.IpStr+ShareThing.getUserimg(), holder.iv_tou, options);//显示头像
		//解析图片集合适配到gridviw上。 
		 List<String> imageUrls=ShareThing.getThingimgs();
		 List<String> imageIpUrls = new  ArrayList<String>();
		 
		 for(int i=0 ;i<imageUrls.size();i++){
	    	String iUrl	= HttpPath.IpStr+imageUrls.get(i);
	    	imageIpUrls.add(iUrl);
		 }
		
	    final ArrayList<String> imageUrl = (ArrayList<String>) imageIpUrls;
	    
		if (imageIpUrls == null || imageIpUrls.size() == 0) { // 没有图片资源就隐藏GridView
			holder.gridview.setVisibility(View.GONE);
		} else {
			//九宫格图片绑定适配器
			holder.gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
			holder.gridview.setAdapter(new NoScrollGridAdapter(mContext, imageIpUrls));
			imageIpUrls = null ;
		}
		// 点击回帖九宫格，查看大图
		holder.gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				imageBrower(position, imageUrl);
			}
		});
		return convertView;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position,   ArrayList<String>  urls2) {
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}

	/**
	 * listview组件复用，防止“卡顿”
	 * 
	 * @author guyu
	 * 
	 */
	class ViewHolder {
		private ImageView iv_tou;
		private TextView tv_nicheng;
		private TextView tv_content;
		private NoScrollGridView gridview;
		private TextView tv_address;
		private TextView tv_zan;
		private TextView tv_data;
		
		
	}
}
