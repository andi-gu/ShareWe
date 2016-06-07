package com.guyu.fragmenttabhost;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.NetUtils.ImageLoader;
import cn.guyu.util.SaveImagetoSD;

import com.example.fragmenttabhost.R;

public class HahItemActivity extends Activity {

	
	ImageView mImageView;
	PhotoViewAttacher mAttacher;
	
	private ProgressBar HahprogressBar;
	
	final private int SUCCESS = 0;
	final private int ERROR = 1;
	TextView  tv_Title;
	private ImageView iv_hahaItem,backHahaImag,saveHahaImag;

	Bitmap btImage, btImageUI,btImag;
	private FileOutputStream out;
	private String imageName;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == SUCCESS) {
				btImageUI = (Bitmap) msg.obj;
				mImageView.setImageBitmap(btImageUI);
				mAttacher.update();
				HahprogressBar.setVisibility(View.GONE);
			} else {
				// Toast.makeText(HahItemActivity.this, "加载失败",
				// Toast.LENGTH_SHORT).show();
			}
		}

	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//全屏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.hah_item);
		mImageView = (ImageView) findViewById(R.id.iv_hahaItem);
		tv_Title = (TextView) findViewById(R.id.tv_haha_content);
		/*Drawable bitmap = getResources().getDrawable(R.drawable.xuan);
		mImageView.setImageDrawable(bitmap);*/
		backHahaImag = (ImageView) findViewById(R.id.haha_back_bt);
		saveHahaImag = (ImageView) findViewById(R.id.haha_save_bt);
		HahprogressBar = (ProgressBar) findViewById(R.id.progressBar_loading);
		HahprogressBar.setVisibility(View.VISIBLE);
		mAttacher = new PhotoViewAttacher(mImageView);
		mAttacher.setScaleType(ScaleType.FIT_CENTER);
		
	/*	final String urlImage = (String) getIntent().getStringExtra("ImageUrl");
		final String Title = (String) getIntent().getStringExtra("TitleUrl");*/
		Bundle bundle = getIntent().getBundleExtra("HahaData");
		final String urlImage = bundle.getString("ImageUrl");
		String Title = bundle.getString("Title");
		System.out.println(Title+"这事内容");
		System.out.println(urlImage+"这事链接");
		tv_Title.setText(Title);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// btImage = getBitmap(urlImage);
					btImage = ImageLoader.getImage(urlImage);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (btImage != null) {
					Message msg = new Message();
					msg.what = SUCCESS;
					
					msg.obj = btImage;
					handler.sendMessage(msg);
				}
			}
		}).start();
		
		mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
		mAttacher.setOnPhotoTapListener(new PhotoTapListener());
		mAttacher.setOnLongClickListener(new longClickLister());

		
		System.out.println("onclik_______________________________________________________");
		backHahaImag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
		
		saveHahaImag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 保存图片到sd卡中
				System.out.println(Environment.getExternalStorageDirectory()
						+ "/Cool/" + "000000000000000000000000000");
				// saveBitmapToSD(btImage);
				//mPopupWindow.dismiss();
				// File file = new
				// File(Environment.getExternalStorageDirectory(),
				// System.currentTimeMillis()+".jpg");

				saveImagetoSD(btImageUI);
				
			}

	
		});
	}
	
	/**
	 * @author:Jack Tony
	 * @tips :点击的监听器
	 * @date :2014-8-3
	 */
	private class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View view, float x, float y) {
			//System.out.println("tap");
			//Toast.makeText(HahItemActivity.this, "点我结束", 0).show();
			finish();
		}

	}

	/**
	 * @author:Jack Tony
	 * @tips :进行缩放的监听器
	 * @date :2014-8-3
	 */
	private class MatrixChangeListener implements OnMatrixChangedListener {

		@Override
		public void onMatrixChanged(RectF rect) {
			//System.out.println("listener");
			//Toast.makeText(HahItemActivity.this, "kankan我", 0).show();
		}
	}
	
	private class longClickLister implements OnLongClickListener{

		@Override
		public boolean onLongClick(View v) {
			//Toast.makeText(HahItemActivity.this, "长按我", 0).show();
			saveImagetoSD(btImageUI);
			return false;
		}
		
	}

	private void saveImagetoSD(Bitmap mp) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))// 判断是否可以对SDcard进行操作
		{ // 获取SDCard指定目录下
			String sdCardDir = Environment
					.getExternalStorageDirectory() + "/CoolImage/";
			File dirFile = new File(sdCardDir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			imageName = System.currentTimeMillis()+ ".jpg";
			File file = new File(sdCardDir, imageName);// 在SDcard的目录下创建图片文

			try {
				out = new FileOutputStream(file);
				mp.compress(Bitmap.CompressFormat.JPEG, 90, out);
				System.out.println("_____________sd__________________________");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String imagePath = Environment.getExternalStorageDirectory()+ "/CoolImage/"+imageName;
			SaveImagetoSD.scanPhoto(this, imagePath);
			
			Toast.makeText(
					HahItemActivity.this,
					"已经保存至" + Environment.getExternalStorageDirectory()
							+ "/CoolImage/" + "目录下", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(HahItemActivity.this, "存储失败", 0).show();
		}
	}
}
