package com.guyu.fragmenttabhost;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.fragmenttabhost.R;
import com.example.fragmenttabhost.R.color;

public class WebDIYActivity extends Activity {
	final Activity activity=this;
	WebView mWebView;
	String str,title;
	TextView text,web_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web_diy);
		web_title=(TextView)findViewById(R.id.web_title);
		 text=(TextView)findViewById(R.id.txt_fanhui);
		 text.setOnClickListener(new OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				 text.setTextColor(color.huise);
				 onBackPressed();
				
			}
		});
		 
		 
		Bundle bundle=this.getIntent().getExtras();
			str=bundle.getString("diy");
			title=bundle.getString("title");
			web_title.setText(title);//换标题
			
			mWebView = (WebView) findViewById(R.id.webView1);	        
	        mWebView.setWebViewClient(new HelloWebViewClient());	            
			mWebView.getSettings().setSupportZoom(true);
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.getSettings().setBlockNetworkImage(false);
			mWebView.loadUrl(str);  
	        mWebView.setWebChromeClient(new WebChromeClient()
	        {	    
	    public  void onProgressChanged(WebView view,int progress) 
	    {
			// TODO Auto-generated method stub
	    	activity.setTitle("网页加载中...");
	    	activity.setProgress(progress*100);
	    	if(progress==100)
	    	{
	    		activity.setTitle(" 加载完成!");
	    	}
		}


	        });
	    }	   
	    
	    private class HelloWebViewClient extends WebViewClient {
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        
	        	view.loadUrl(url);
	            return true;
	        }

	    }
}
