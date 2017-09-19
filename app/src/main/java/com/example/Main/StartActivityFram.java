package com.example.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.jhjpay.zyb.R;
import com.jhj.ViewPagerAndTabhost.MainActivity_TabHost;
import com.jhj.network.Data_zyb;

public class StartActivityFram extends Activity {
	AnimationDrawable ab;
	ImageView image;
	// 下载app的名字
	public static final String appName = "下载测试";

	/** 使用SharedPreferences 来储存与读取数据 **/

	public final static String SHARED_MAIN = "main";
	SharedPreferences_util su = new SharedPreferences_util();
	Editor editor;
	Data_zyb dz = new Data_zyb();
	private boolean login_off;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设备终端号
		if (su.getPrefboolean(StartActivityFram.this, "on_off", false)) {
			su.setPrefboolean(StartActivityFram.this, "on_off", true);
		}

		setContentView(R.layout.activity_start);
		MyAppLication.getInstance().addActivity(this);
		
		dz.setFdm(su.getPrefString(StartActivityFram.this, "fdm", null));
		image = (ImageView) findViewById(R.id.image_gif);
		image.setBackgroundResource(R.drawable.animation_list);
		ab = (AnimationDrawable) image.getBackground();
		ab.setOneShot(false);
		if (ab.isRunning()) {
			ab.stop();
		}
		ab.start();
		AlphaAnimation aa = new AlphaAnimation(0.3f, 8.5f);
		aa.setDuration(2000);
		image.setAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent();
				intent.setClass(StartActivityFram.this, MainActivity_TabHost.class);
				startActivity(intent);
				login_off = false;
				su.setPrefboolean(StartActivityFram.this, "login_off", login_off);
				finish();
			}
		});
	}
}
