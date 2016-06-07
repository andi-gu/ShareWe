package cn.guyu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;



public class SaveImagetoSD {
	


public  static String SaveImage(String Imagename, Bitmap mp) {
	FileOutputStream out = null;
	String path ="";
	
	// 判断是否可以对SDcard进行操作
	if (Environment.getExternalStorageState().equals(
			Environment.MEDIA_MOUNTED))
	{ // 获取SDCard指定目录下
		String sdCardDir = Environment
				.getExternalStorageDirectory() + "/CoolImage/";
		File dirFile = new File(sdCardDir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(sdCardDir, Imagename+ ".jpg");// 在SDcard的目录下创建图片文

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
		
		path=Environment.getExternalStorageDirectory()
				+ "/CoolImage/"+ Imagename +".jpg";
			
	}
	return path;
}

//发送广播通知图片内容提供者，相册中已经添加新图片
public  static void scanPhoto(Context ctx, String imgFileName) {
    Intent mediaScanIntent = new Intent(
            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File file = new File(imgFileName);
    Uri contentUri = Uri.fromFile(file);
    mediaScanIntent.setData(contentUri);
    ctx.sendBroadcast(mediaScanIntent);
}



}