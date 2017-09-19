package com.example.Main;

import com.jhjpay.zyb.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * 注册--网页版
 * @author lb
 *
 */
public class ZhuCeActivity extends Activity {

	private WebView webView;
	private ProgressBar pg1;
	private ImageButton img_back;
	private static final String zcUrl = "http://cnyssj.com/dfweb/sys/sysuser/register?appDownload=false";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_zhuce);
		
		init();
		webView.loadUrl(zcUrl);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ZhuCeActivity.this.finish();
			}
		});
		// TODO 自动生成的方法存根
		webView=(WebView) findViewById(R.id.webview1);
		pg1=(ProgressBar) findViewById(R.id.progressBar1);

		webView.setWebViewClient(new WebViewClient(){
			//覆写shouldOverrideUrlLoading实现内部显示网页
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO 自动生成的方法存根
				view.loadUrl(url);
				return true;
			}
		});
		WebSettings seting=webView.getSettings();
		seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
		webView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO 自动生成的方法存根

				if(newProgress==100){
					pg1.setVisibility(View.GONE);//加载完网页进度条消失
				}
				else{
					pg1.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
					pg1.setProgress(newProgress);//设置进度值
				}

			}
		});

	}


	//设置返回键动作（防止按返回键直接退出程序)
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			if(webView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
				webView.goBack();
				return true;
			}
			else {//当webview处于第一页面时,直接退出程序
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
