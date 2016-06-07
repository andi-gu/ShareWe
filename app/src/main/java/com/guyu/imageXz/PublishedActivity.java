package com.guyu.imageXz;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.NetUtils.ImageServerUtils;
import cn.guyu.util.CustomProgressDialog;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.fragmenttabhost.R;
import com.guyu.fragmenttabhost.MainActivity;
import com.guyu.httpPath.HttpPath;

@SuppressLint("ResourceAsColor")
public class PublishedActivity extends Activity {

	private static final String ICICLE_KEY = null;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send,textViewEt,textViewQx,locationInfoTextView,locationImgTextView;
	String shuoshuo;
	SharedPreferences mySharedPreferences ;
	SharedPreferences.Editor Seditor;
	private ImageView  AddressImage;
	private LinearLayout  AddressLin;
	private String address ="";
	private boolean a = true ,flagNet ,flagImag = false;
	private double mLatitude;
	private double mLongtitude;
	private LocationClient locationClient = null;
	private ProgressDialog dialog;
	private String result="";
	private String resultmap ,mapimgeurl="";
	private SharedPreferences PreferencesFab;
	private String id;
	private CustomProgressDialog progressDialog = null;
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			dialog.dismiss();
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
			if(Bimp.drr.size()>0){
			  	Toast.makeText(PublishedActivity.this, "有图片喽", 0).show();
			activity_selectimg_send.setClickable(true);
				
				}else {
					activity_selectimg_send.setTextColor(R.color.baise);
//				//	Toast.makeText(PublishedActivity.this, "没有图片喽", 0).show();
//				//	没有图片不可以点击发布按钮，并且颜色变暗淡
					activity_selectimg_send.setClickable(false);
				}
			break;
			case 2:
				FileUtils.deleteDir();//清空内存卡中存在的照片
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				adapter.notifyDataSetChanged();
				Seditor.putString("textS", null);
				Seditor.commit();
				//activity_selectimg_send.setClickable(false);
				Toast.makeText(getApplicationContext(), "发布成功", Toast.LENGTH_SHORT).show(); //图片上传成功
				//上传成功跳转页面（Activity-MainActivity-fragment）
				Intent intent = new Intent();
				intent.setClass(PublishedActivity.this, MainActivity.class);
				intent.putExtra("datafb", "fbok");
				startActivity(intent);
				PublishedActivity.this.finish();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				stopProgressDialog();
				Toast.makeText(getApplicationContext(), "已添加至服务器", Toast.LENGTH_SHORT).show();
				
				break;
			case 5:
				stopProgressDialog();
				Toast.makeText(getApplicationContext(), "图片添加失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			
			
			
		}
		
	};
	private String name;
	private String image="";
	private int flag;
	private String maptext;

protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//没有titlebar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_selectimg);
		PreferencesFab = getSharedPreferences("Login", MODE_PRIVATE);
		id = PreferencesFab.getString("uid", null);
		name  =  PreferencesFab.getString("uname", null);
		image   = PreferencesFab.getString("uimage", null);
		
		// flag	= PreferencesFab.getInt("uflag", -1);
		
		mySharedPreferences = getSharedPreferences("ssText", Activity.MODE_PRIVATE);
		Seditor =	mySharedPreferences.edit();
		
		
		textViewEt = (TextView) findViewById(R.id.et_fabu);
		String fabushuoshuoText =	mySharedPreferences.getString("textS", "");
		if(fabushuoshuoText!=null){
			textViewEt.setText(fabushuoshuoText);
		}
		
		Init();
		//位置定位
		initLocation();
	}

	//位置定位
	private void initLocation() {
		AddressLin = (LinearLayout) findViewById(R.id.bt_linl_address);
		locationInfoTextView = (TextView) findViewById(R.id.tv_address);
		locationImgTextView = (TextView) findViewById(R.id.tv_addressImg);
		AddressImage= (ImageView) findViewById(R.id.image_address);
		locationClient = new LocationClient(this);
	     //设置定位条件
	     LocationClientOption option = new LocationClientOption();
	     option.setCoorType("bd09ll");
			option.setIsNeedAddress(true);
			option.setOpenGps(true);
			option.setScanSpan(1000);
			locationClient.setLocOption(option);
			
			//注册位置监听器
			locationClient.registerLocationListener(new BDLocationListener() {
				
				@Override
				public void onReceiveLocation(BDLocation location) {
					if (location == null) {
						return;
					}
				//	StringBuffer sb = new StringBuffer(256);
					/*sb.append("\nLatitude : ");
					sb.append(location.getLatitude());
					sb.append("\nLontitude : ");
					sb.append(location.getLongitude());*/
					/*if (location.getLocType() == BDLocation.TypeGpsLocation){
						sb.append("\nSpeed : ");
						sb.append(location.getSpeed());
						sb.append("\nSatellite : ");
						sb.append(location.getSatelliteNumber());
						
					} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
						//sb.append("\nAddress : ");
						sb.append(location.getAddrStr());
					}*/
					
					//sb.append(location.getAddrStr());
					if(a){
						address = location.getAddrStr().toString();
						a =false;
						
						}
			
						
						
					// 更新经纬度
					mLatitude = location.getLatitude();
					mLongtitude = location.getLongitude();
					
				//	sb.append("\n检查位置更新次数：");
				//	sb.append(String.valueOf(LOCATION_COUTNS));
				//	locationInfoTextView.setText(sb.toString());
					
					locationInfoTextView.setText(address);	
				}
			});	
		
		
		
			
			
		//显示位置按钮的点击事件
			AddressLin.setOnClickListener(new OnClickListener() {
				
				
				public void onClick(View v) {
					
					if(flagNet = pandunnet()){
						if (locationClient == null) {
						return;
					}
					if (locationClient.isStarted()) {
						locationInfoTextView.setText("");
						locationImgTextView.setVisibility(View.GONE);
						flagImag = false;
						locationClient.stop();
					}else {
					//	AddressImage.setBackgroundResource(R.drawable.adress_nol);
						locationClient.start();
					
					/*	if(address.equals(null)){
							Toast.makeText(PublishedActivity.this, "null", 0).show();
							locationClient.stop();
							locationImgTextView.setVisibility(View.GONE);
						}
						*/
						
						locationInfoTextView.setText(address);
						locationImgTextView.setVisibility(View.VISIBLE);
						flagImag = true;
//添加位置图片的点击事件
						locationImgTextView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// 获取图片
									Intent intent = new Intent(Intent.ACTION_PICK, null);
									intent.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
									startActivityForResult(intent, 1);		
								}
							});
						
						locationClient.requestLocation();	
					}
				}else {
					Toast toast = Toast.makeText(PublishedActivity.this, "当前网络不可用",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 126);
					toast.show();
				}				
				}
			});
					
	}


	
	protected void onDestroy() {
		super.onDestroy();		
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
		
	}
	
	public String  getthingIfo() throws IOException {
		final List<NameValuePair> Parameters = new ArrayList<NameValuePair>();
		Parameters.add(new BasicNameValuePair("Ttext", textViewEt.getText().toString().trim()));
		Parameters.add(new BasicNameValuePair("Tlng", String.valueOf(mLongtitude)));
		Parameters.add(new BasicNameValuePair("Tlat", String.valueOf(mLatitude)));
		if(!(mapimgeurl == "")){
			mapimgeurl = mapimgeurl.trim();
		}
		Parameters.add(new BasicNameValuePair("Tmapimg",mapimgeurl));
		Parameters.add(new BasicNameValuePair("userid", id));
		Parameters.add(new BasicNameValuePair("username", name));
		if(!(image == "")){
			image = image.trim();
		}
		Parameters.add(new BasicNameValuePair("userimg", image));
		if(address.length()>0){
		maptext = address.trim();
		}
		Parameters.add(new BasicNameValuePair("Tmaptext", maptext));
		Parameters.add(new BasicNameValuePair("numb", String.valueOf(Bimp.drr.size())));
		System.out.println("Parameters:" + Parameters);
		String path=URLEncodedUtils.format(Parameters,"UTF-8");
		System.out.println(path+"cancancancancanshushushushushu");
		return path;

	}
	
//gradview布局
	public void Init() {
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					shuoshuo =	textViewEt.getText().toString();
					System.out.println(shuoshuo+"hshshshshshsh");
					Seditor.putString("textS", shuoshuo.toString());
				//	Seditor.putInt("textFlag", 1);
					Seditor.commit();
					new PopupWindows(PublishedActivity.this, noScrollgridview);
					
				} else {
					Intent intent = new Intent(PublishedActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		dialog=new ProgressDialog(this);
		dialog.setTitle("正在发布...");
		
		
		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
		
//		if(Bimp.drr.size()>0)
//		{
		
//发布按钮
	activity_selectimg_send.setOnClickListener(new OnClickListener() {
			
					
				
			public void onClick(View v) {
				dialog.show();
				final List<String> list = new ArrayList<String>();				
				for (int i = 0; i < Bimp.drr.size(); i++) {
					final String Str = Bimp.drr.get(i).substring( 
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					final String path = FileUtils.SDPATH+Str+".JPEG";
				  
				//	list.add(path);	
					
					System.out.println(path+"ttttttttttttttttttttttttttt"+i);
				new Thread(new Runnable() {
				
					public void run() {
						
						try {
							String pathurl = getthingIfo();
							//如果上传的图片为null,传递一个数据标识一下;
							if(Bimp.drr.size()>0){
						    	result = ImageServerUtils.formUpload(HttpPath.UPLOAD_URL+"?"+pathurl, path);
							}else {
								result = ImageServerUtils.formUpload(HttpPath.UPLOAD_URL+"?"+pathurl, null);
							}
						System.out.println("======="+result);
						
				
							if(!TextUtils.isEmpty(result))
							{
								if(result == "false"){
									handler.sendEmptyMessage(2);
								}
								
								list.add(result);
								if(list.size() == Bimp.drr.size()){
									System.out.println(list.size()+"---"+Bimp.drr.size());
									handler.sendEmptyMessage(2);
								}
								
								
								
								
						    }else{
							
								handler.sendEmptyMessage(3);
							}
						
						
						
					} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
						
				}
			  }).start();
				
			
		    }
				// 高清的压缩图片全部就在  list 路径里面了
				// 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
				// 完成上传服务器后 .........
			}	
	      
       });
//	}else {
		
	//	activity_selectimg_send.setClickable(false);
	//	activity_selectimg_send.setTextColor(R.color.baise);
//	}	
	
		//点击取消按钮
		textViewQx = (TextView) findViewById(R.id.tv_quxiao_btn_fb);
		textViewQx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			DialogPopupWindows();
			}
		});
		
	}


	
	
	
	
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}
		
		
		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		/*Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};
       */
		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								if( Bimp.drr.isEmpty()){
									return;
								}else {
									
									String path = Bimp.drr.get(Bimp.max);
									System.out.println(path);
									Bitmap bm = Bimp.revitionImageSize(path);
									Bimp.bmp.add(bm);
									String newStr = path.substring(
											path.lastIndexOf("/") + 1,
											path.lastIndexOf("."));
									FileUtils.saveBitmap(bm, "" + newStr);
									Bimp.max += 1;
									Message message = new Message();
									message.what = 1;
									handler.sendMessage(message);
									
								}
							/*	String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);*/
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}

		
	}

	
	
	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		
		super.onRestart();
	}
//选择框的PopupWindow
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			update();
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera); //bt1拍照
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);   //bt2相册
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);  //bt3取消
			//bt1拍照
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				
					photo();
					dismiss();
				}
			});
			 //bt2相册
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PublishedActivity.this,
							TestPicActivity.class);
				
					System.out.println("-------------------------------------");
					startActivity(intent);
					dismiss();
					finish();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}
	
	
	private void DialogPopupWindows(){

		
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = View.inflate(PublishedActivity.this, R.layout.dialog_back, null);
			final PopupWindow window = new PopupWindow(view,
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
			window.setFocusable(true);
			window.setBackgroundDrawable(new BitmapDrawable());
			window.showAtLocation(view, Gravity.CENTER, 0, 0);
			final Button bt1 = (Button) view
					.findViewById(R.id.tv_dialog_back);  //退出
			final Button bt2 = (Button) view
					.findViewById(R.id.tv_dialog_qx);    //取消
			//退出
 			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					window.dismiss();
					Seditor.putString("textS", null);
					Seditor.commit();
					/*
					//清空list集合，并且清空缓存的图片
					Bimp.drr.clear();
					Bimp.bmp.clear();
				//	adapter.update();
					adapter.notifyDataSetChanged();*/
					FileUtils.deleteDir();
					Bimp.bmp.clear();
					Bimp.drr.clear();
					Bimp.max = 0;
					adapter.notifyDataSetChanged();
					
				PublishedActivity.this.finish();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				
					window.dismiss();
				
				}
			});
	}
	

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";
//打开照相机意图
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/myimage/", String.valueOf(System.currentTimeMillis())
				+ ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri uri = null;
		switch (requestCode) {
		
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
				
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
			}
			break;
			//
		case 1:
			if (data == null) {
				return;
			}
			
			uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			final String pathmap = cursor.getString(column_index);// 图片在的路径  地图图片
			startProgressDialog();
			new Thread(new Runnable() {
				@Override
				public void run() {
						resultmap = ImageServerUtils.formUpload(HttpPath.MapImageUPLOAD_URL, pathmap);
						mapimgeurl = resultmap;
					//  mapimgeurl = HttpPath.MapImagDOWNLOAD_URL+resultmap;
						System.out.println("result:====="+mapimgeurl);
						if(!TextUtils.isEmpty(resultmap)){
							handler.sendEmptyMessage(4);
						}else{
							handler.sendEmptyMessage(5);
						}
				}
			}).start();
			break;
		default:
			break;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {	
		if((keyCode == KeyEvent.KEYCODE_BACK)&&(event.getRepeatCount() == 0)){
			//DialogPopupWindows();
			
				DialogPopupWindows();
			
			
		}
		return false;
	}
	  /** 
	   * * 点击空白位置 隐藏软键盘
	      */
	 public boolean onTouchEvent(MotionEvent event) {
	        if(null != this.getCurrentFocus()){
	            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	        }
	        return super.onTouchEvent(event);

	 }
		// 判断网络 
	 private boolean pandunnet() {
			boolean networkState = cn.guyu.util.PanduanNet.detect(this);
			return networkState;
		}
	
	 
		public void startProgressDialog(){
			if (progressDialog == null){
				progressDialog = CustomProgressDialog.createDialog(this);
		    	progressDialog.setMessage("添加图片中...");
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
