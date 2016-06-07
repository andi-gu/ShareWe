package cn.guyu.NetUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Xml;
import cn.guyu.entity.Haha;
import cn.guyu.entity.NewInfo;
import cn.guyu.entity.Things;

import com.google.gson.Gson;
import com.guyu.httpPath.HttpPath;

public class NetUtils {

	/*
	 * 使用get方式注册数据到服务器  name 昵称（传入的数据） id 账号  password 密码
	 */

	private static final String TAG = "NetUtils";
	private static List<Haha> contentshaha;

	public static String addOfGet(String name, String id, String password) {
		HttpURLConnection conn = null;
		try {

			// String data ="name="+name+"&id="+id+"&password="+password;
			String data = "name=" + URLEncoder.encode(name) + "&id="
					+ URLEncoder.encode(id) + "&password="
					+ URLEncoder.encode(password); // 解决乱码问题

			  URL url = new URL(HttpPath.Register_URL + data);
			
			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);// 连接的超时时间
			conn.setReadTimeout(5000);// 读取数据的超时时间
			System.out.println("name=" + name + "&id=" + id + "&password="
					+ password + "...........................................");
			System.out.println(id+ "：注册时候的id...................................");

			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				String state = getStringInputStream(is);// 将流转换成字符串
				return state;
			} else {
				Log.i(TAG, "访问失败" + responseCode);

			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();// 关闭连接
			}
		}

		return null;
	}

	/*
	 * 使用post方式登录
	 */
	public static String loginOfPost(String id, String password) {
		HttpURLConnection conn = null;
		try {
		
				URL url = new URL(HttpPath.Login_URL);
				//	URL url = new URL("http://172.27.187.1:8080/CoolWeb/LoginServlet");
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);

			// 服务端有时候需要传一下参数
			// conn.setRequestProperty("Content-Length", 234);//设置请求头消息，可以设置多个
			// post请求的参数
			// String data = "id=" + id
			// +"&password="+password+"&JSESSIONID=91FE58FE1A5B755DE6198B063361CED3";
			String data = "id=" + id + "&password=" + password;
			OutputStream out = conn.getOutputStream();// 获得一个输出流，用于向服务器穿数据
			out.write(data.getBytes());// 字节数据不涉及编码问题
			out.flush();
			out.close();
			// 获得相应码
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {

				/*
				 * String cookie = conn.getHeaderField("set_cookie");
				 * 
				 * String sessionid = null; if(cookie != null){ sessionid =
				 * cookie.substring(0, cookie.indexOf(";")); } if(sessionid !=
				 * null) { conn.setRequestProperty("cookie", sessionid); }
				 * System
				 * .out.println(sessionid+"0000000000000000000000000000000000");
				 */

				InputStream is = conn.getInputStream();
				String state = getStringInputStream(is);
				return state;
			} else {

				Log.i(TAG, "访问失败:" + responseCode);
				System.out.println("账号密码——登录00000000000000000000000000000000000000");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return null;

	}
	
	/*
	 * 使用post方式修改完善信息
	 */
	public static String ModifyOfPost(String id,String name,String sex,String image) {
		HttpURLConnection conn = null;
		try {
		//	URL url = new URL("http://192.168.51.44:8080/CoolWeb/ModifyServlet");
				URL url = new URL(HttpPath.Modify_URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(5000);
			
			String data ="id="+ id + "&name=" + name + "&sex=" + sex + "&image=" + image;
			OutputStream out = conn.getOutputStream();// 获得一个输出流，用于向服务器穿数据
			out.write(data.getBytes());// 字节数据不涉及编码问题
			out.flush();
			out.close();
			// 获得相应码
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				String state = getStringInputStream(is);
				return state;
			} else {

				Log.i(TAG, "访问失败:" + responseCode);
				System.out.println("完——善——信——息0失败00000000000000000000000000000000");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return null;

	}
	
	//使用post方式显示用户信息（name,sex）
	
	public  static String  SelectByIdPost(String id){
		HttpURLConnection conn = null;
		
		try {
	
			URL url = new URL(HttpPath.SelectById_URL);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(5000);
			
			String data ="id="+ id ;
			OutputStream out = conn.getOutputStream();// 获得一个输出流，用于向服务器传数据
			out.write(data.getBytes());// 字节数据不涉及编码问题
			out.flush();
			out.close();
			// 获得相应码
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				String state = getStringInputStream(is);
				return state;
			} else {

				Log.i(TAG, "访问失败:" + responseCode);
				System.out.println("获——取——信——息0失败00000000000000000000000000000000");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return null;

	}
	
//获取文章（文章信息）	//新闻集合	
 public static List<NewInfo> getNewsFromIntent() {
	 HttpClient client = null;
	 try { //定义一个客户端
		 client = new DefaultHttpClient();
	//定义get方法
		HttpGet get =new HttpGet(HttpPath.GetNews_URL);
	 //执行请求方法
		
			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				InputStream  is =	response.getEntity().getContent();
				List<NewInfo> newInfoList =	getNewListFromInputStream(is);
				return 	newInfoList;
			}else {
				Log.i(TAG, "访问失败："+statusCode);
			}
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}finally{
			if(client != null){
				client.getConnectionManager().shutdown();
			}
		}
	 return null;
	}	
 
		
 //根据路径得到image图片(Bitmap) 加载网络图片


 public static Bitmap getImage(String path){
	 
	 HttpURLConnection conn = null;
	 Bitmap bitmap = null;
	 try {
		URL url = new URL(path);
		HttpURLConnection  httpconn  = (HttpURLConnection) url.openConnection();
		httpconn.connect();
		
		InputStream is =	httpconn.getInputStream();
		bitmap  = 	BitmapFactory.decodeStream(is);
		is.close();
		httpconn.disconnect();
	} catch (Exception e) {
		
		e.printStackTrace();
	}
 
	return bitmap;
	 
 }
	
	
	
	
	
	
	
/*
 * 从流中解析新闻集合
 * 解析xml数据使用pull解析器
*/
	private static List<NewInfo> getNewListFromInputStream(InputStream is) throws Exception {
	
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int eventType = parser.getEventType();
		
		List<NewInfo>  newInfoList = null;
		NewInfo newInfo = null;
		while(eventType != XmlPullParser.END_DOCUMENT ){
		
			String tagNme = parser.getName();//节点名称
			switch (eventType) {
			case XmlPullParser.START_TAG://<news>
				if("news".endsWith(tagNme)){
					newInfoList = new ArrayList<NewInfo>();
				}else if ("new".equals(tagNme)){
					newInfo = new NewInfo();
				}else if ("title".equals(tagNme)) {
					newInfo.setTitle(parser.nextText());
				}else if ("detail".equals(tagNme)) {
					newInfo.setDetail(parser.nextText());
				}else if ("comment".equals(tagNme)) {
					newInfo.setComment(Integer.valueOf(parser.nextText()));
				}else if ("image".equals(tagNme)) {
					newInfo.setImageUrl(parser.nextText());
				}else if ("content".equals(tagNme)) {
					newInfo.setContentUrl(parser.nextText());
				}
				
					break;
			case XmlPullParser.END_TAG:
				if("new".equals(tagNme)){
					newInfoList.add(newInfo);
					
				}
				break;	
				
			default:
				break;
			}
			eventType = parser.next();
		}
		
		return newInfoList;
	
	
  }

	// 将流转换成字符串
	private static String getStringInputStream(InputStream is)
			throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = is.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		/*is.close();
		return 	bos.toByteArray();  //从流中读出数据返回出去*/
		
		is.close();
		// String html = bos.toString();//把流中数据转换成字符串 采用的是UTF-8
		String html = new String(bos.toByteArray(), "GBK");// 在客户端更改，接收GBK编码格式，转换成字符串
		bos.close();
		return html;

	}

	/*public Runnable getThingsInfo(final String path,final Handler handler) {
		final List<Things> rulist = new ArrayList<Things>();
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// 新建HttpGet对象
				HttpGet httpGet = new HttpGet(path);
				// 获取HttpClient对象
				HttpClient httpClient = new DefaultHttpClient();
				// 获取HttpResponse实例
				HttpResponse httpResp = null;
				try {
					httpResp = httpClient.execute(httpGet);
				
				
				// 判断是够请求成功
				if (httpResp.getStatusLine().getStatusCode() == 200) {
					// 获取返回的数据 
					String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
				
				
					JSONArray array = new JSONArray(result);
					for(int i =array.length()-1; i>0; i--){
						Things thing = new Things();
						JSONObject jst = array.getJSONObject(i);
						thing.setUsername(jst.getString("username"));
						thing.setThingtext(jst.getString("thingtext"));
						thing.setThingdata(jst.getString("thingdata"));
						thing.setThingimgs(jst.getString("thingimgs"));
						thing.setThinglng(jst.getString("thinglng"));
						thing.setThinglat(jst.getString("thinglat"));
						thing.setThingmapimg(jst.getString("thingmapimg"));
						thing.setThingzan(jst.getString("thingzan"));
						thing.setUserid(jst.getString("userid"));
						thing.setUserimg(jst.getString("userimg"));
						thing.setThingmaptext(jst.getString("thingmaptext"));
						rulist.add(thing);
						System.out.println("--------------------"+rulist.size());
					}
				}
					
				else { 
					
					System.out.println("请求失败");
				}
					Message message = handler.obtainMessage();
					message.obj = rulist;
					handler.sendMessage(message);	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		return runnable;

	}*/
/*	public static List<Things> getThingsInfo() {
		final List<Things> rulist = new ArrayList<Things>();
		HttpClient client = null;
		 try { //定义一个客户端
		 client = new DefaultHttpClient();
		
		//定义get方法
			HttpGet get =new HttpGet(HttpPath.GetAllThings_URL);
			// 获取HttpResponse实例
			HttpResponse httpResp = null;
			 //执行请求方法
			HttpResponse response = client.execute(get);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				
				String result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
				JSONArray array = new JSONArray(result);
				for(int i =array.length()-1; i>0; i--){
					Things thing = new Things();
					JSONObject jst = array.getJSONObject(i);
					thing.setUsername(jst.getString("username"));
					thing.setThingtext(jst.getString("thingtext"));
					thing.setThingdata(jst.getString("thingdata"));
					thing.setThingimgs(jst.getString("thingimgs"));
					thing.setThinglng(jst.getString("thinglng"));
					thing.setThinglat(jst.getString("thinglat"));
					thing.setThingmapimg(jst.getString("thingmapimg"));
					thing.setThingzan(jst.getString("thingzan"));
					thing.setUserid(jst.getString("userid"));
					thing.setUserimg(jst.getString("userimg"));
					thing.setThingmaptext(jst.getString("thingmaptext"));
					rulist.add(thing);
					System.out.println("--------------------"+rulist.size());
				}
				
				return 	rulist;
			}else {
				Log.i(TAG, "访问失败："+statusCode);
			}
			
	} catch (Exception e) {
	
		e.printStackTrace();
	}finally{
		if(client != null){
			client.getConnectionManager().shutdown();
		}
	}
		return null;
		
	}*/
	
	public static List<Things> getThingsInfo(){
		List<Things> rulist = new ArrayList<Things>();
		HttpURLConnection conn = null;
		try {
			URL url = new URL(HttpPath.GetAllThings_URL);
			
				try {
					conn = (HttpURLConnection) url.openConnection();
				
				conn.setConnectTimeout(10000);
				conn.setReadTimeout(5000);
				conn.setRequestMethod("POST");
			
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
			 
				   InputStream is = conn.getInputStream();
				   String state = getStringInputStream(is);
				   
				   Gson gson=new Gson();
			
				try {	JSONArray array= new JSONArray(state);
					
				     
			   
			     	for(int i = 0;i <array.length();i++)
			     	{
					Things thing = new Things();
					
//					Things thing=gson.fromJson(state, Things.class);
					
					JSONObject jst = array.getJSONObject(i);
				
					thing.setUsername(jst.getString("username"));
					thing.setThingtext(jst.getString("thingtext"));
					thing.setThingdata(jst.getString("thingdata"));
					thing.setThinglng(jst.getString("thinglng"));
					thing.setThinglat(jst.getString("thinglat"));
					thing.setThingmapimg(jst.getString("thingmapimg"));
					thing.setThingzan(jst.getString("thingzan"));
					thing.setUserid(jst.getString("userid"));
					thing.setUserimg(jst.getString("userimg"));
					thing.setThingmaptext(jst.getString("thingmaptext"));
					
					String imgs= jst.getString("thingimgs");
					
					thing.setThingimgs(gson.fromJson(imgs, List.class));
					
					rulist.add(thing);
					
				 }
				  return rulist;
			     	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}	
			else {
				Log.i(TAG, "访问失败:" + responseCode);
				System.out.println("获——取——信——息0失败00000000000000000000000000000000");
			}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (conn != null) {
				conn.disconnect();
			}
		}return null;
			
	}
}

