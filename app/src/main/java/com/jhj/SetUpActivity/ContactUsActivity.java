package com.jhj.SetUpActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Main.MyAppLication;
import com.jhjpay.zyb.R;
/**
 * 联系我们
 * @author lb
 */
public class ContactUsActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private ImageView img_weixin;
	private TextView tv_phone,tv_weixin,tv_qq,tv_website;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_setup_contact_us);
		
		img_weixin=(ImageView) findViewById(R.id.img_weixin);
		img_back=(ImageButton) findViewById(R.id.img_back);
		tv_website=(TextView) findViewById(R.id.tv_website);
		tv_phone=(TextView) findViewById(R.id.tv_phone);
		tv_weixin=(TextView) findViewById(R.id.tv_weixin);
		tv_qq=(TextView) findViewById(R.id.tv_qq);

		tv_website.setText("http://user.hfpay.net/merchant/account/login");
		tv_phone.setText("4009-222-166");
		tv_weixin.setText("shuang00421");
		tv_qq.setText("941078860");

		img_weixin.setOnClickListener(this);
		img_back.setOnClickListener(this);
		tv_website.setOnClickListener(this);
		tv_phone.setOnClickListener(this);
		tv_weixin.setOnClickListener(this);
		tv_qq.setOnClickListener(this);


	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			ContactUsActivity.this.finish();
			break;
		case R.id.tv_website://网站
			toast("暂不支持使用，敬请期待！");
//			Intent web_intent = new Intent(ContactUsActivity.this,WebsiteActivity.class);
//			web_intent.putExtra("website_url", tv_website.getText().toString());
//			startActivity(web_intent);
			break;
		case R.id.tv_phone://电话
			Intent phone_intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tv_phone.getText().toString()));  
			startActivity(phone_intent);
			break;
		case R.id.tv_weixin://微信
			OpenWeiXin(tv_weixin.getText().toString());
			break;
		case R.id.tv_qq://QQ
			String url = "mqqwpa://im/chat?chat_type=wpa&uin="+tv_qq.getText().toString()+"&version=1";
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			break;
		case R.id.img_weixin:
			
			break;
		default:
			break;
		}
	}

	/**
	 * 打开微信
	 * @param weixin
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void OpenWeiXin(String weixin) {
		try {
			// 获取剪贴板管理服务
			ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			//将文本数据（微信号）复制到剪贴板
			cm.setText(weixin);
			//跳转微信
			Intent intent = new Intent(Intent.ACTION_MAIN);
			ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(cmp);
			startActivity(intent);
			Toast.makeText(this, "微信号已复制到粘贴板，请使用", Toast.LENGTH_LONG).show();
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(this, "您还没有安装微信，请安装后使用",Toast.LENGTH_LONG).show();
		}
	}
	
	public void toast(String str) {
		Toast.makeText(ContactUsActivity.this, str, Toast.LENGTH_SHORT).show();
	}
}
