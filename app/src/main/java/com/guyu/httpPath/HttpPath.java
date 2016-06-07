package com.guyu.httpPath;
public class HttpPath {
	
	
	//public static String  IpStr ="http://169.254.157.72:8080";
	public static String  IpStr ="http://192.168.253.1:8080";//校园网ip
	
	/**
	 * 图片上传路径 172.27.187.1（无线）  169.254.157.72（本地）
	 */
	public static final String UPLOAD_URL= IpStr + "/CoolWeb/UploadServlet";
	public static final String UPLOAD_URLHH= IpStr + "/CoolWeb/UploadImageServlet/";
	//   
	/**
	 * 图片下载路径
	 */
	public static final String DOWNLOAD_URL= IpStr + "/CoolWeb/ImageUpload/";

	/**
	 * 头像图片上传路径
	 */
	public static final String HandImageUPLOAD_URL= IpStr + "/CoolWeb/HeadImageUploadServlet";
	
	/**
	 * 头像图片下载路径
	 */
	public static final String HandImagDOWNLOAD_URL= IpStr + "/CoolWeb/TouImageUpload/";
	
	public static final String HandImagDOWNLOAD ="/CoolWeb/TouImageUpload/";
	
	/**
	 * map图片上传路径
	 */
	public static final String MapImageUPLOAD_URL= IpStr + "/CoolWeb/MapImageUploadServlet";
	
	/**
	 * map图片下载路径
	 */
	public static final String MapImagDOWNLOAD_URL= IpStr + "/CoolWeb/MapImageUpload/";
	/**
	 * 获取所以things 路径
	 */
	public static final String GetAllThings_URL= IpStr + "/CoolWeb/getAllThingServlet";
	/**
	 * 注册path
	 */
	public static final String Register_URL= IpStr + "/CoolWeb/AddServlet?";
	/**
	 * 登录path
	 */
	public static final String Login_URL= IpStr + "/CoolWeb/LoginServlet";
	/**
	 * 修改path
	 */
	public static final String Modify_URL= IpStr + "/CoolWeb/ModifyServlet";
	/**
	 * 获取用户信息path
	 */
	public static final String SelectById_URL= IpStr + "/CoolWeb/SelectByIdServlet";
	
	/**
	 * 文章信息path
	 */
	public static final String GetNews_URL= IpStr + "/CoolWeb/new.xml";
	/**
	 *默认头像
	 */
	public static final String ImageHead_URL= IpStr + "/CoolWeb/TouImageUpload/app_logo.png";
	public static final String ImageHead="/CoolWeb/TouImageUpload/app_logo.png";
	
	
}