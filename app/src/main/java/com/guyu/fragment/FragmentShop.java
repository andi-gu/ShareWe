package com.guyu.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.guyu.NetUtils.ImageLoader;
import cn.guyu.entity.Haha;
import cn.guyu.util.CustomProgressDialog;
import cn.guyu.util.RelativeDateFormat;

import com.example.fragmenttabhost.R;
import com.guyu.fragment.FragmentShop.MyAdapterH.ViewHodler;
import com.guyu.fragmenttabhost.HahItemActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.show.api.ShowApiRequest;

public class FragmentShop extends Fragment implements OnRefreshListener {

	protected static final int ERROR = 1;
	protected static final int SUCCESS = 0;
	// private View view;
	private ListView lv_xiaohua;
	List<Haha> contentshaha;
	ImageView tximage;
	MyAdapterH adapterH;

	private CustomProgressDialog progressDialog = null;
	ViewHodler viewHolder;
	Haha hahainfo;
	private boolean flagNet,isLoading;
	private boolean mFirstIn = true;
	private List<String> URLS = new ArrayList<String>();
	private ImageLoader mImageLoader;
	private int pagename;
	String num;
	private SwipeRefreshLayout swip;
	private View footer;

	/*
	 * Handler handler = new Handler(){
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * 
	 * super.handleMessage(msg); if(msg.what == SUCCESS){ //
	 * progressDialog.dismiss(); Bitmap bm = (Bitmap) msg.obj; // String url
	 * =(msg.arg1)+"";
	 * 
	 * String url = hahainfo.getImg(); String tag = (String)
	 * viewHolder.ivhaha.getTag(); if(tag.equals(url)) {
	 * viewHolder.ivhaha.setImageBitmap(bm); }
	 * 
	 * } } };
	 */

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmentshop, null);
		
		
		flagNet = pandunnet();
		
		lv_xiaohua = (ListView) view.findViewById(R.id.xiaohua_list);

		swip = (SwipeRefreshLayout) view.findViewById(R.id.swip_haha);
		
		swip.setOnRefreshListener(this);
		swip.setColorScheme(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		
		

		// 初始化让 加载更多隐藏
		inflater = LayoutInflater.from(getActivity());
		footer = inflater.inflate(R.layout.load_more_footer, null, false);
		footer.setVisibility(View.GONE);
		lv_xiaohua.addFooterView(footer);

		contentshaha = new ArrayList<Haha>();
		adapterH = new MyAdapterH(contentshaha, lv_xiaohua);
		lv_xiaohua.setAdapter(adapterH);
		/*
		 * //加载进度条 //显示ProgressDialog progressDialog =
		 * ProgressDialog.show(getActivity(), "请 稍 等. . . . . . . .",
		 * "数 据 加 载 中 . . .", true, false);
		 */
		// 判断网络.成功执行加载，不成功显示网络连接失败
				
				if(flagNet){
					startProgressDialog();
					//Toast.makeText(getActivity(), "加载中...", Toast.LENGTH_SHORT).show();
					pagename = 1;
					System.out.println("pagename======one======"+pagename);
					goNet("1");
				}else {
					stopProgressDialog();
					Toast toast = Toast.makeText(getActivity(), "无网络",Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP, 0, 147);
					toast.show();
				//有没有一种办法，判断过网络连接不成功后停止执行后面的代码。
				}
		
		
		//点击item---------------
	lv_xiaohua.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				hahainfo = contentshaha.get(position);	
				/*Toast.makeText(getActivity(), hahainfo.getImg(), 0).show();//根据位置获取相应的信息
				Toast.makeText(getActivity(), hahainfo.getTitle(), 0).show();//根据位置获取相应的信息*/		
				Intent intent=new Intent(getActivity(),HahItemActivity.class);
				//携带要传递的信息
				Bundle bundle = new Bundle();
				bundle.putString("ImageUrl", hahainfo.getImg());
				bundle.putString("Title", hahainfo.getTitle());
				intent.putExtra("HahaData", bundle); 
				// intent.putExtra("ImageUrl", hahainfo.getImg());
				// intent.putExtra("Title", hahainfo.getTitle());
				 startActivity(intent);
			}
		});
		return view;
	}

	// 访问showApi数据
	private void goNet(String pagenames) {
		new ShowApiRequest("http://route.showapi.com/341-2", "13914",
				"a8f3136c512c4b859a2ca887df0e620d")
				.setResponseHandler(resHandler).addTextPara("page", pagenames)
				.addTextPara("paraName2", "这是参数2").post();
	}

	
	// 异步加载
	final AsyncHttpResponseHandler resHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable e) {
			// 做一些异常处理
			e.printStackTrace();
			stopProgressDialog();
			Toast.makeText(getActivity(), "访问网络失败  报错码为" + statusCode, Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			try {
				System.out.println("guyuyyuyuyuyuuuuuuuuuuuuuuuuuuuuuuuuuuu");
				System.out.println("response is :"+ new String(responseBody, "utf-8"));
				String json = new String(responseBody, "utf-8");

				JSONObject obj = new JSONObject(json); // 根据具体的数据格式进行解析：1.将json字符串转换为json对象
				int key = obj.getInt("showapi_res_code");
				if (key == 0) {
					obj = obj.getJSONObject("showapi_res_body"); // 2.根据key值得到下一个json对象
					JSONArray array = obj.getJSONArray("contentlist"); // 3,在这个json对象中获得json数组
					for (int i = 0; i < array.length(); i++) { // 解析json数组
						JSONObject jsonObject = array.getJSONObject(i);
						String url = jsonObject.getString("img");

						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
						Date date = format.parse(jsonObject.getString("ct"));

						Haha haha = new Haha(
						// jsonObject.getString("ct"),
								RelativeDateFormat.format(date),
								jsonObject.getString("title"),
								url);

						System.out.println(haha.toString()+ "________________________");
						URLS.add(url);
						contentshaha.add(haha);
					}
				} else {
					Toast.makeText(getActivity(),"您的操作过于频繁！请稍后再试~" + statusCode, 0).show();
				}
				stopProgressDialog();
				// 进度条消失 　progressDialog = ProgressDialog.show(MyActivity.this,
				// "请稍等...", "获取数据中...", true);
				footer.setVisibility(View.GONE);
				isLoading = false;

				if (swip.isShown()) { // 取消动画就
					swip.setRefreshing(false);
				}

				mImageLoader.setURLS(URLS);
				adapterH.notifyDataSetChanged();
				// 加载图片
				mImageLoader.loadImages(0, 2);
				/*
				 * if(mFirstIn){ mImageLoader.loadImages(0, 2); mFirstIn =
				 * false; }
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	class MyAdapterH extends BaseAdapter implements OnScrollListener {

		List<Haha> conHahas;
		private int mStart, mEnd;

		public MyAdapterH(List<Haha> conHahas, ListView listview) {
			this.conHahas = conHahas;
			mImageLoader = new ImageLoader(listview);
			// 通过循环将conHahas数组中的图片的url放到了一个静态的数组里
			/*
			 * URLS = new String[ conHahas.size()]; for(int i = 0;i < conHahas.size(); i++){ URLS[i] = conHahas.get(i).getImg();
			 * System.out.println("++++++++++++++++++++++++++++" +conHahas.get(i).getImg()); }
			 */
			// 注册ListView的滚动事件
			// mFirstIn = true;
			listview.setOnScrollListener(this);
		}
		
		@Override
		public int getCount() {
			return conHahas.size();
		}

		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			View view = null;
			if (converView == null) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				view = inflater.inflate(R.layout.listview_item_xiaohua, null);
				tximage = (ImageView) view.findViewById(R.id.xiaohu_img_tx); // 头像
																				// (固定的)

				viewHolder = new ViewHodler();
				viewHolder.tvhaha = (TextView) view
						.findViewById(R.id.list_text_xiaohua); // 标题
				viewHolder.ivhaha = (ImageView) view
						.findViewById(R.id.list_image_xiaohua);// 图片
				/*int screenWidth = getScreenWidth(getActivity());
				ViewGroup.LayoutParams lp = viewHolder.ivhaha.getLayoutParams();
				lp.width = screenWidth;
				lp.height = LayoutParams.WRAP_CONTENT;
				viewHolder.ivhaha.setLayoutParams(lp);

				viewHolder.ivhaha.setMaxWidth(screenWidth);
				viewHolder.ivhaha.setMaxHeight(screenWidth * 5); */
				
				viewHolder.timehaha = (TextView) view
						.findViewById(R.id.list_data__xiaohua); // 时间

				view.setTag(viewHolder); // 设置Tag
			} else {
				view = converView;
				viewHolder = (ViewHodler) view.getTag();
			}
			hahainfo = conHahas.get(position); // 根据位置获取所在的 笑话信息

			viewHolder.ivhaha.setTag(hahainfo.getImg());
			viewHolder.ivhaha.setImageResource(R.drawable.xuan);// 重点1，为每个ImageView设置一个tag，值为图片网址（保证tag的唯一性）
			viewHolder.tvhaha.setText(hahainfo.getTitle());
			viewHolder.timehaha.setText(hahainfo.getCt());
			// new ImageLoader().showImageByThread(viewHolder.ivhaha,
			// hahainfo.getImg());
			// new ImageLoader().showImageByAsyncTask(viewHolder.ivhaha,
			// hahainfo.getImg());
			mImageLoader.showImageByAsyncTask(viewHolder.ivhaha,
					hahainfo.getImg());

			return view;

		}

		class ViewHodler {
			public ImageView ivhahaT;
			public ImageView ivhaha;
			public TextView tvhaha;
			public TextView timehaha;
		}

		public Object getItem(int position) {
			return conHahas.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		private int total = 0;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			mStart = firstVisibleItem;
			mEnd = firstVisibleItem + visibleItemCount;
			total = totalItemCount;
			// 第一次显示时候调用
			if (mFirstIn && visibleItemCount > 0) {
				// mImageLoader .loadImages(0, 2);
				// mFirstIn = false;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {

				// 判断滑动到底部了，进行加载更多
				if (total == mEnd) {
					if (!isLoading&&flagNet) {
						isLoading = true;
						
						footer.setVisibility(View.VISIBLE);
						// 延迟两秒请求数据
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								pagename++;
								System.out.println("pagename======two======"+pagename);
								num = String.valueOf(pagename);
								goNet(num);
								Toast.makeText(getActivity(),
										"当前加载 第" + pagename + "页", 0).show();
							}
						}, 2000);
						
					}
				} else {
				
					mImageLoader.loadImages(mStart, mEnd);
				}

			} else {
				// 停止任务
				mImageLoader.cancelAllTasks();
			}
		}
	}
	//下啦刷新
	@Override
	public void onRefresh() {
	//	Toast.makeText(getActivity(), "开始刷新啦", Toast.LENGTH_SHORT).show();
		// （这里只是起到模拟加载的作用）
		// contentshaha.clear();
		// pagename=1;
		// goNet("1");
		
		if(flagNet){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (swip.isShown()) {
					swip.setRefreshing(false);
				}
				Toast.makeText(getActivity(), "已是最新数据！", Toast.LENGTH_SHORT)
						.show();
			}
		}, 3000);
		}
		else {
			swip.setRefreshing(false);
			Toast toast = Toast.makeText(getActivity(), "网络连接失败,请检查网络设置",Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP, 0, 66);
			toast.show();
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










/*//获取屏幕的宽度  
	public static int getScreenWidth(Context context) {  
	    WindowManager manager = (WindowManager) context  
	            .getSystemService(Context.WINDOW_SERVICE);  
	    Display display = manager.getDefaultDisplay();  
	    return display.getWidth();  
	}  
	//获取屏幕的高度  
	public static int getScreenHeight(Context context) {  
	    WindowManager manager = (WindowManager) context  
	            .getSystemService(Context.WINDOW_SERVICE);  
	    Display display = manager.getDefaultDisplay();  
	    return display.getHeight();  
	}  */
