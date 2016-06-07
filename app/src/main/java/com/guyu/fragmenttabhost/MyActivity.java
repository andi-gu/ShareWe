package com.guyu.fragmenttabhost;


import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.NetUtils.ImageLoaderFromN;
import cn.guyu.NetUtils.ImageServerUtils;
import cn.guyu.NetUtils.NetUtils;
import cn.guyu.util.SaveImagetoSD;

import com.example.fragmenttabhost.R;
import com.guyu.httpPath.HttpPath;

public class MyActivity extends Activity {

	private LinearLayout linearLayoutsex, linearLayoutChoice, linearLayoutSave,
			linearLayoutBack;
	private TextView textViewm, textViewFm, textViewquxiao, tvsex;
	private EditText edname;
	private ImageView  txImage;
	private SharedPreferences Preferences;
	private Editor Editor;
	final private int SUCCESS = 0;
	final private int ERROR = 3;
	final private int WRONG = 4;
	String id;
	private Button BttouImage;
	private String path ;
	private String touImage ="";
	private String Hresult="";
	private boolean  flag  = false ;
	private boolean net;
	private String     ImageSevletUrl;
	private String touImageurl;

	
	Handler handler = new Handler() {

		

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 1:
				
				//Toast.makeText(getApplicationContext(), "头像上传成功", Toast.LENGTH_SHORT).show();
				//touImage = HttpPath.HandImagDOWNLOAD_URL+Hresult;
				touImage = Hresult;
				System.out.println(touImage);
				touImageurl	= HttpPath.IpStr + touImage;
				 ImageLoaderFromN.ImageRequest(touImageurl, txImage);
				 //服务器图片uri保存到本地
				// Editor.putString("uImageSevlet", touImage); 
				 Editor.putString("uimage", touImage); 
				 Editor.commit();
				 flag = true;
				
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "连接服务器失败", Toast.LENGTH_SHORT).show();
				break;
			case SUCCESS:
			     String namex =	(String) msg.obj;
			     Editor.putString("uname", namex); 
				 Editor.commit();
				Toast.makeText(MyActivity.this, "保存成功···", Toast.LENGTH_SHORT).show();
				// 保存成功传值给MainActivity
				// 修改成功后跳转到 我 页面（Activity-MainActivity-fragment）
				Intent intent = new Intent();
				intent.setClass(MyActivity.this, MainActivity.class);
					// String gu = "guyu";
				intent.putExtra("data", "guyu");
				startActivity(intent);
				finish();
				break;
			case WRONG:
				Toast.makeText(MyActivity.this, "保存失败···", Toast.LENGTH_SHORT)
				.show();
				break;
			case ERROR:
				Toast.makeText(MyActivity.this, "网络超时", Toast.LENGTH_SHORT)
				.show();
				break;
				
			default:
				break;
			}
			
			
			

		}

	};

	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);

		Preferences = getSharedPreferences("Login", MODE_PRIVATE);
		Editor = Preferences.edit();
		//获取服务器图片URL。可能为null
	  //  ImageSevletUrl  =  Preferences.getString("uImageSevlet", null);
		 ImageSevletUrl  =  Preferences.getString("uimage", null);
		
         id = Preferences.getString("uid", null);
		
		linearLayoutBack = (LinearLayout) findViewById(R.id.tv_back_btn);
      	BttouImage = (Button) findViewById(R.id.frametoux);	
      	txImage = (ImageView) findViewById(R.id.xinxi_img);
  
       ImageLoaderFromN.ImageRequest(HttpPath.IpStr + ImageSevletUrl , txImage);
      	
		linearLayoutsex = (LinearLayout) findViewById(R.id.linearlayout_sex);
		linearLayoutChoice = (LinearLayout) findViewById(R.id.LinearLayout_sex_nn);
		linearLayoutSave = (LinearLayout) findViewById(R.id.linearlayout_save);

		edname = (EditText) findViewById(R.id.ed_name);
		tvsex = (TextView) findViewById(R.id.tv_sex);
		textViewm = (TextView) findViewById(R.id.man);
		textViewFm = (TextView) findViewById(R.id.nv);
		textViewquxiao = (TextView) findViewById(R.id.quxiao);

		linearLayoutBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	
		net =pandunnet();
		if(net){
		
		//点击头像上传
		BttouImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ShowPickDialog();
			}

			private void ShowPickDialog() {
		
				new AlertDialog.Builder(MyActivity.this)
				.setTitle("设置头像")
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// 获取图片
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, 1);

					}
				})
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						// 调用拍照
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 下面这句指定调用相机拍照后的照片存储的路径文件名id账号代替
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										id + ".jpg")));
						startActivityForResult(intent, 2);
					}
				}).show();
		
			}
		});
		/* 选择框显示 */
		linearLayoutsex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				linearLayoutChoice.setVisibility(View.VISIBLE);
				/* 点击 男 */
				textViewm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {	tvsex.setText("男");
						linearLayoutChoice.setVisibility(View.GONE);
					}
				});
				/* 点击 女 */
				textViewFm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						tvsex.setText("女");
						linearLayoutChoice.setVisibility(View.GONE);
					}
				});
				/* 点击取消 */
				textViewquxiao.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						linearLayoutChoice.setVisibility(View.GONE);
					}
				});

			}
		});

		//设置flag :处理不上传图片之上传昵称性别的情况。
		if( !flag ){ //没上传头像，把本地的赋值给他
			touImage  =   ImageSevletUrl ;
		}
		
		
		// 点击保存信息,上传到服务器
		linearLayoutSave.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
			    final String name = edname.getText().toString();
				final String sex = tvsex.getText().toString();
				
				System.out.println("———————从本地读出的ididididididididid值为：" + id);
				System.out.println("完善信息：--id:" + id + "-昵称："+ name+ "-性别：" + sex);
				
				if (name.equals("") || sex.equals("")) {
					Toast.makeText(MyActivity.this, "信息不能为空", 0).show();
				} else {

					new Thread(new Runnable() {

						@Override
						public void run() {

							final String state = NetUtils.ModifyOfPost(id,
									name, sex, touImage);

							if (state != null) {

								if (state.equals("Msuccess")) {
									Message msg = new Message();
									msg.what = SUCCESS;
									msg.obj = name;
									handler.sendMessage(msg);

								} else {
									Message msg = new Message();
									msg.what = WRONG;
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.what = ERROR;
								handler.sendMessage(msg);

							}
						}}).start();
					}

			}

		});
		
		
		
	}

	}
	
	
	

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			if(data != null){
				startPhotoZoom(data.getData());
			}
			break;
		// 如果是调用相机拍照时
		case 2:
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/" + id + ".jpg");
			startPhotoZoom(Uri.fromFile(temp));
		
			break;
		// 取得裁剪后的图片
		case 3:
			/*
			 * 非空判断大家一定要验证，如果不验证的话， * 在剪裁之后如果发现不满意，要重新裁剪，丢弃  当前功能时，会报NullException，
			 */
			if (data != null) {
				setPicToView(data);
				
			}
			break;
		default:
			break;

		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	/*
	 * 保存裁剪之后的图片数据 *
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras  = picdata.getExtras();
		if(extras != null){
			Bitmap photo = 	extras.getParcelable("data");
			/*Drawable drawable = new BitmapDrawable(photo);			
			// draw转换为String
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
			byte[] b = stream.toByteArray(); // 将图片流以字符串形式存储下来
			userimg = new String(Base64Coder.encodeLines(b));	*/
			 //txImage.setImageBitmap(photo);
			 path = 	SaveImagetoSD.SaveImage(id, photo);
			 /* Editor.putString("uImage", path); 
			 Editor.commit();*/
			 
			 
			new Thread(uploadImageRunnable).start();
		
		}
		
	}

	Runnable uploadImageRunnable = new Runnable(){
	@Override
		public void run() {
			Hresult =	ImageServerUtils.formUpload(HttpPath.HandImageUPLOAD_URL, path);
			if(!TextUtils.isEmpty(Hresult)){
				handler.sendEmptyMessage(1);
			}else{
				handler.sendEmptyMessage(2);
			}	
		}
		
	};



	// 裁剪图片方法实现
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 500);
		intent.putExtra("outputY", 500);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}



/*	//封装参数
		@SuppressLint("SimpleDateFormat")
		public String  getrubbishIfo() throws IOException {
			final List<NameValuePair> Parameters = new ArrayList<NameValuePair>();
			Parameters.add(new BasicNameValuePair("runame", thingname.getText().toString().trim()));
			Parameters.add(new BasicNameValuePair("ruleibie", fenlei.getSelectedItem().toString()));
			Parameters.add(new BasicNameValuePair("rubeizhu", beizhu.getText().toString().trim()));
			Parameters.add(new BasicNameValuePair("rufreetime", freetime.getSelectedItem().toString()));
			Parameters.add(new BasicNameValuePair("userid", userid));
			Parameters.add(new BasicNameValuePair("userphone", userphone));
			Parameters.add(new BasicNameValuePair("address", address.getText().toString().trim()));
			Parameters.add(new BasicNameValuePair("username", username));
			Parameters.add(new BasicNameValuePair("rudate", ""));

			System.out.println("Parameters:" + Parameters);
			String path=URLEncodedUtils.format(Parameters,"UTF-8");
			System.out.println(path+"0000000000000000000");
			return path;

		}*/
	
	
	
	
	public boolean onTouchEvent(MotionEvent event) {
	        if(null != this.getCurrentFocus()){
	            /**
	             * 点击空白位置 隐藏软键盘
	             */
	            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	        }
	        return super.onTouchEvent(event);

	 }
	 
	public boolean onKeyDown(int keyCode, KeyEvent event) {	
		if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getRepeatCount() == 0)){
			//DialogPopupWindows();
			
			finish();
			
			
		}
		return false;
	}
	
	private boolean pandunnet() {
		// 判断网络
		boolean networkState = cn.guyu.util.PanduanNet.detect(MyActivity.this);
		return networkState;
	}
}
