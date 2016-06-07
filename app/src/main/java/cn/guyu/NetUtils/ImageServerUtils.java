package cn.guyu.NetUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class ImageServerUtils {


	/**
	 * 文件上传
	 * 
	 * @param urlStr 上传路径
	 * @param filePath 图片所在存储卡的路径
	 * @return
	 */
	public static String formUpload(String urlStr, String filePath) {
		String rsp = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "|"; // request头和上传文件内容分隔符
		try {
			URL url = new URL(urlStr);
			 /* 允许Input、Output，不使用Cache 设定传送的method=POST*/
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			File file = new File(filePath);
			String filename = file.getName();
			String contentType = "";
			if (filename.endsWith(".png")) {
				contentType = "image/png";
			}
			if (filename.endsWith(".jpg")) {
				contentType = "image/jpg";
			}
			if (filename.endsWith(".gif")) {
				contentType = "image/gif";
			}
			if (filename.endsWith(".bmp")) {
				contentType = "image/bmp";
			}
			if (contentType == null || contentType.equals("")) {
				contentType = "application/octet-stream";
			}
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\"" + filePath
					+ "\"; filename=\"" + filename + "\"\r\n");
			strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
			out.write(strBuf.toString().getBytes());
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			rsp = buffer.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return rsp;
	}
	//-------------------------------------------------------------
	private static String newName ="image.jpg";
	  /* 上传文件至Server的方法 */
	public static String uploadFile(String actionUrl,String picPath)
    {
      String end ="\r\n";
      String twoHyphens ="--";
      String boundary ="*****";//边界标识
      try
      {
        URL url =new URL(actionUrl);
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        /* 允许Input、Output，不使用Cache */
        con.setDoInput(true);//允许输入流
        con.setDoOutput(true);//允许输出流
        con.setUseCaches(false);//不允许使用缓存
        /* 设置传送的method=POST */
        con.setRequestMethod("POST");
        /* setRequestProperty   设置编码  */
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",// "multipart/form-data"这个参数来说明我们这传的是文件不是字符串了
                           "multipart/form-data;boundary="+boundary);
        
        /* 设置DataOutputStream */
        DataOutputStream ds =
          new DataOutputStream(con.getOutputStream());
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; "+
                      "name=\"file1\";filename=\""+
                      newName +"\""+ end);  
        ds.writeBytes(end);
        /* 取得文件的FileInputStream */
        FileInputStream fStream =new FileInputStream(picPath);
        /* 设置每次写入1024bytes */
        int bufferSize =1024;
        byte[] buffer =new byte[bufferSize];
        int length =-1;
        /* 从文件读取数据至缓冲区 */
        while((length = fStream.read(buffer)) !=-1)
        {
          /* 将资料写入DataOutputStream中 */
          ds.write(buffer, 0, length);
        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
        fStream.close();
        ds.flush();
        /* 取得Response内容 */
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) !=-1 )
        {
          b.append( (char)ch );
        }
       Log.i("gu", "shangchuanchenggong");
      //  showDialog("上传成功"+b.toString().trim());
        /* 关闭DataOutputStream */
        ds.close();
        //返回客户端返回的信息
        return b.toString().trim();
      }
      catch(Exception e)
      {
        //showDialog("上传失败"+e);
    	  return null;
      }
    }
 
	
	
}