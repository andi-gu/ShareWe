package cn.guyu.NetUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.fragmenttabhost.R;

public class ImageLoader {

	private ImageView mImageView;
	private ListView mListView;
	private Set<NewAsyncTask> mTask;

	private List<String> URLS;

	public void setURLS(List<String> uRLS) {
		URLS = uRLS;

	}

	private String murl;
	// -------------Lru缓存-----------------------------
	private LruCache<String, Bitmap> mcaches;

	public ImageLoader(ListView listView) {
		mListView = listView;
		mTask = new HashSet<NewAsyncTask>();
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 4;

		mcaches = new LruCache<String, Bitmap>(cacheSize) {

			@SuppressLint("NewApi")
			protected int sizeOf(String key, Bitmap value) {
				// 在每次存入缓存的时候调用
				return value.getByteCount();
			}
		};
	}

	// 添加到缓存
	public void addBotmapToCashe(String url, Bitmap bitmap) {
		if (getBitmapFromCache(url) == null) {
			mcaches.put(url, bitmap);
		}

	}

	// 从缓存中获取图片(数据)
	public Bitmap getBitmapFromCache(String url) {

		return mcaches.get(url);

	}

	/*
	 * //-----------------多线程的方法------------------------------- private Handler
	 * handler = new Handler(){
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * 
	 * super.handleMessage(msg); if(mImageView.getTag().equals(murl)){
	 * mImageView.setImageBitmap((Bitmap) msg.obj); }
	 * 
	 * }
	 * 
	 * }; public void showImageByThread(ImageView imageview,final String url){
	 * mImageView = imageview; murl = url; new Thread(){
	 * 
	 * public void run() { super.run(); Bitmap bitmap = getImage(url); Message
	 * message = Message.obtain(); message.obj = bitmap;
	 * handler.sendMessage(message); }; }.start(); }
	 * //-----------------多线程的方法------------------------------
	 */
	//从网络获取图片
/*	public static Bitmap getImage(String path) {
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			URL url = new URL(path);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	try {
		is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		return null;
	}*/

	public static Bitmap getImage(String path) throws IOException{
	     URL url = new URL(path);
	     HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	     conn.setConnectTimeout(5000);
	     conn.setRequestMethod("GET");
	     if(conn.getResponseCode() == 200){
	     InputStream inputStream = conn.getInputStream();
	     Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
	     return bitmap;
	     }
	     return null;
	 
	    }
	
	
	
	
	public void cancelAllTasks() {
		if (mTask != null) {
			for (NewAsyncTask task : mTask) {
				task.cancel(false);
			}
		}
	}

	// 用来加载从start到end的图片
	public void loadImages(int start, int end) {

		for (int i = start; i < end; i++) {
			String url = URLS.get(i);
			// 从缓存中取出对应图片
			Bitmap bitmap = getBitmapFromCache(url);
			// 如果没有就去下载
			if (bitmap == null) {
				NewAsyncTask task = new NewAsyncTask(url);
				task.execute(url);
				mTask.add(task);
			} else {
				ImageView imageView = (ImageView) mListView
						.findViewWithTag(url);
				try {
					imageView.setImageBitmap(bitmap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e+"----Error");
				}
			}
		}
	}

	// -----------------------AsyncTask的方法---------------------------------------------------------
	public void showImageByAsyncTask(ImageView imageview, String url) {
		// 从缓存中取出对应图片
		Bitmap bitmap = getBitmapFromCache(url);
		// 如果没有就去下载
		if (bitmap == null) {
			imageview.setImageResource(R.drawable.xuan);
		} else {
			imageview.setImageBitmap(bitmap);
		}

	}

	private class NewAsyncTask extends AsyncTask<String, Void, Bitmap> {

		// private ImageView mImageView;
		private String murl;

		public NewAsyncTask(String url) {
			// mImageView = imageview;
			murl = url;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			// 从网络获取图片
			Bitmap bitmap = null ;
			try {
				bitmap = getImage(url);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bitmap != null) {
				// 将不在缓存的图片加入缓存
				addBotmapToCashe(url, bitmap);
			}
			return bitmap;

		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {

			super.onPostExecute(bitmap);
			ImageView imageView = (ImageView) mListView.findViewWithTag(murl);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
			}
			mTask.remove(this);
		}

	}
	
	
	
	
	

}
