package com.jhj.WalletActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.Main.MyAppLication;
import com.jhjpay.zyb.R;

public class NoticeDetailsByWebView extends Activity {

	private ImageButton img_back;
	private TextView tv_title;
	private WebView notice_webview;
	private ProgressBar progressBar;
	private String title,notice_url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_wallet_notice_details);
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NoticeDetailsByWebView.this.finish();
			}
		});
		progressBar=(ProgressBar) findViewById(R.id.progressBar);
		tv_title=(TextView) findViewById(R.id.tv_title);
		title=getIntent().getStringExtra("title");
		notice_url=getIntent().getStringExtra("notice_url");

		if(!TextUtils.isEmpty(title)){
			tv_title.setText(title);
		}
		if(TextUtils.isEmpty(notice_url)){
			notice_webview.setVisibility(View.GONE);
			return;
		}
		init();
	}
	private void init(){
		notice_webview = (WebView) findViewById(R.id.notice_webview);
		//WebView加载web资源
		notice_webview.loadUrl(notice_url);
		//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		notice_webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		notice_webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					// 网页加载完成
					Drawable drawable = getResources().getDrawable(
							R.drawable.pg_l);
					progressBar.setProgressDrawable(drawable);
				} else {
					// 加载中
					progressBar.setProgress(newProgress);
				}

			}
		});
	}

}
