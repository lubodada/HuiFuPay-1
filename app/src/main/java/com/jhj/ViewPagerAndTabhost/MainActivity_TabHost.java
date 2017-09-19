package com.jhj.ViewPagerAndTabhost;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MainActivity;
import com.example.Main.MyAppLication;
import com.jhj.Agreement.ZYB.Http_Pay_ZYB;
import com.jhj.WalletActivity.WalletActivity;
import com.jhj.imagecycleview.ImageCycleActivity;
import com.jhj.info_util.ImageUtil;
import com.jhj.info_util.Utils;
import com.jhjpay.zyb.R;

@SuppressWarnings("deprecation")
public class MainActivity_TabHost extends Activity {
	// 页卡内容
	private ViewPager mPager;
	// Tab页面列表
	private List<View> listViews;
	private Intent tab1Intent;
	private Intent tab2Intent;
	private Intent tab3Intent;
	LocalActivityManager groupActivity;
	RadioButton radio1, radio2, radio3;
	RadioButton[] radios;
	RadioGroup radiogroup;
	protected static final int TAKE_PICTURE = 0;
	protected static final int CHOOSE_PICTURE = 1;
	private static final int CROP_SMALL_PICTURE = 3;
	private final static int SCANNIN_GREQUEST_CODE = 2;
	SharedPreferences_util su = new SharedPreferences_util();
	private Uri tempUri, uri;
	private String imgPath, bigimgPath;
	private File file;
	// 退出判断
	private boolean isQuit = false;

	Http_Pay_ZYB hp = new Http_Pay_ZYB();
	// 设备终端号，收银员名称，商户号
	public static String clientId, cashiername, merchantId;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_tabhost);
		MyAppLication.getInstance().addActivity(this);
		groupActivity = new LocalActivityManager(this, true);
		groupActivity.dispatchCreate(savedInstanceState);

		clientId = su
				.getPrefString(MainActivity_TabHost.this, "clientId", null);
		merchantId = su.getPrefString(MainActivity_TabHost.this, "merchantId",
				null);
		cashiername = su.getPrefString(MainActivity_TabHost.this,
				"cashiername", null);
		
		mPager = (ViewPager) findViewById(R.id.viewpage);
		radio1 = (RadioButton) findViewById(R.id.receivables);
		radio2 = (RadioButton) findViewById(R.id.function);
		radio3 = (RadioButton) findViewById(R.id.wallet);
		radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
		RadioButton[] radioButtons = { radio1, radio2, radio3 };
		radios = radioButtons;
		tab1Intent = new Intent(this, MainActivity.class);
		tab2Intent = new Intent(this, ImageCycleActivity.class);
		tab3Intent = new Intent(this, WalletActivity.class);

		listViews = new ArrayList<View>();
		listViews.add(getView("1", tab1Intent));
		listViews.add(getView("2", tab2Intent));
		listViews.add(getView("3", tab3Intent));
		//
		MyPagerAdapter mpAdapter = new MyPagerAdapter(listViews);
		mPager.setAdapter(mpAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					radio1.setChecked(true);
					break;
				case 1:
					radio2.setChecked(true);
					break;
				case 2:
					radio3.setChecked(true);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.receivables:
					mPager.setCurrentItem(0);
					break;
				case R.id.function:
					mPager.setCurrentItem(1);
					break;
				case R.id.wallet:
					mPager.setCurrentItem(2);
					break;
				}
			}

		});
	}

	private View getView(String id, Intent intent) {
		return groupActivity.startActivity(id, intent).getDecorView();
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) { // 如果返回码是可以用的
			switch (requestCode) {
			case TAKE_PICTURE:// 相机
				file = new File(Environment.getExternalStorageDirectory()
						+ "/img_head_portrait.jpg");// 保存图片
				tempUri = Uri
						.fromFile(new File(Environment
								.getExternalStorageDirectory(),
								"img_head_portrait.jpg"));
				if (file.exists()) {
					bigimgPath = file.toString();
					startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
				}
				break;
			case CHOOSE_PICTURE:// 相册
				if (data != null) {
					uri = data.getData();
					bigimgPath = ImageUtil.getImageAbsolutePath(
							MainActivity_TabHost.this, uri);
					startPhotoZoom(uri); // 开始对图片进行裁剪处理
				}
				break;
			case CROP_SMALL_PICTURE:
				if (data != null) {
					su.setPrefString(MainActivity_TabHost.this, "bigimgPath",
							bigimgPath);
					imgPath = su.getPrefString(MainActivity_TabHost.this,
							"imgPath", null);
					if (imgPath != null) {
						file = new File(imgPath);
						if (file.exists()) {
							file.delete();
						}
					}
					setImageToView(data);// 让刚才选择裁剪得到的图片显示在界面上
				}
				break;
			case SCANNIN_GREQUEST_CODE:
				String monery = su.getPrefString(getApplicationContext(),
						"monery", null);
				Bundle bundle = data.getExtras();
				// 显示扫描到的内容
				int result = Integer.parseInt(bundle.getString("result")
						.substring(0, 2));

				if (result == 13) {
					// 微信
					hp.runService(Integer.parseInt(monery),
							MainActivity_TabHost.this,
							bundle.getString("result"), cashiername, clientId,
							merchantId, 4, "微信");
				} else if (result == 51) {
					// 翼支付
					hp.runService(Integer.parseInt(monery),
							MainActivity_TabHost.this,
							bundle.getString("result"), cashiername, clientId,
							merchantId, 13, "翼支付");

				} else if (result == 28) {
					// 支付宝
					toast("请使用翼支付付款！");
					hp.runService(Integer.parseInt(monery),
							getApplicationContext(),
							bundle.getString("result"), cashiername, clientId,
							merchantId, 1, "支付宝");

				} else {
					toast("无效二维码数据！");
				}
				MainActivity.QcTextNum();
				break;
			}
		}
	}

	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	protected void startPhotoZoom(Uri uri) {
		if (uri == null) {
			Log.i("tag", "The uri is not exist.");
		}
		tempUri = uri;
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, CROP_SMALL_PICTURE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param
	 * @param picdata
	 */
	@SuppressWarnings("static-access")
	protected void setImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			photo = Utils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
			String imagePath = Utils.savePhoto(photo, Environment
					.getExternalStorageDirectory().getAbsolutePath(), String
					.valueOf(System.currentTimeMillis()));
			su.setPrefString(MainActivity_TabHost.this, "imgPath", imagePath);
			Log.i("Tab", "圆形图片路径" + imagePath);
		}
	}

	@Override
	public void onBackPressed() {

		if (!isQuit) {
			Toast.makeText(MainActivity_TabHost.this, "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			isQuit = true;

			// 这段代码意思是,在两秒钟之后isQuit会变成false
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						isQuit = false;
					}
				}
			}).start();
		} else {
			finish();
		}
	}

	public void toast(String str) {
		Toast.makeText(MainActivity_TabHost.this, str, Toast.LENGTH_SHORT)
				.show();
	}
	
}
