package cn.guyu.NetUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.fragmenttabhost.R;

public class ImageLoaderFromN {
	
	public static void ImageRequest(String url ,final ImageView iv){
		
		ImageRequest request = new ImageRequest(url, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap arg0) {
				iv.setImageBitmap(arg0);
				
			}
		}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
		
			public void onErrorResponse(VolleyError arg0) {
				iv.setBackgroundResource(R.drawable.app_logo);
				
			}
		
		});

	MyApplication.getHttpQueues().add(request);
	
	}
	
	
}
