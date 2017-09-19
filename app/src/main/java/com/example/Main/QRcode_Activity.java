package com.example.Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bestpay.cn.utils.Denglu_time;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.bestpay.cn.utils.ViewPagerAdapter_QR;
import com.jhj.Dialog.PromptBoxDialog;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.print.Print;
import com.jhjpay.zyb.R;
import com.mining.app.zxing.image.QRImage;

public class QRcode_Activity extends Activity {
	SharedPreferences_util su = new SharedPreferences_util();
	int orderType;
	Denglu_time dt = new Denglu_time();
	String merchantId, keyString, order, outOrderId, clientId;
	String qr_code;
	String qr_code_monery;
	// 商品列表
	String PayDetails;

	private ImageView QR_code_weixin, QR_code_zhifubao, QR_code_yizhifu;
	private ImageButton img_back;
	private ViewPager mViewPager;
	private ViewPagerAdapter_QR mAdapter;
	private ArrayList<View> pageview;

	private YWLoadingDialog mDialog = null;
	private final int LOAD_SUC_FINISH = -1;
	private final int LOAD_FAIL_FINISH = -2;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycollection_code);
		MyAppLication.getInstance().addActivity(this);
		merchantId = su.getPrefString(QRcode_Activity.this, "merchantId", null);
		keyString = su.getPrefString(QRcode_Activity.this, "key", null);
		clientId = su.getPrefString(QRcode_Activity.this, "clientId", null);

		initView();
		initData();// 初始化数据
		// onShowSuc();
		showmessage(6);
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		img_back = (ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(myclicklist);
	}

	/**
	 * 初始化数据
	 */
	@SuppressLint("InflateParams")
	private void initData() {

		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		// 查找布局文件用LayoutInflater.inflate
		LayoutInflater inflater = this.getLayoutInflater();
		View view1 = inflater.inflate(R.layout.activity_qr_code_one, null);
		// View view2 = inflater.inflate(R.layout.activity_qr_code_two, null);
		// View view3 = inflater.inflate(R.layout.activity_qr_code_three, null);

		QR_code_weixin = (ImageView) view1.findViewById(R.id.QR_code_one);
		// QR_code_zhifubao = (ImageView) view2.findViewById(R.id.QR_code_two);
		// QR_code_yizhifu = (ImageView) view3.findViewById(R.id.QR_code_three);

		// 将view装入数组
		pageview = new ArrayList<View>();
		pageview.add(view1);
		// pageview.add(view2);
		// pageview.add(view3);
		mAdapter = new ViewPagerAdapter_QR(this, pageview);
		mViewPager.setAdapter(mAdapter);

		// 将ViewPager定位到中间页（Short.MAX_VALUE/2附近的图片资源数组第1个元素对应的页面）
		// 目的：1.图片个数 >1 才轮播 2.定位到中间页，向左向右都可滑
		if (pageview.size() > 1) {
			mViewPager.setCurrentItem(1, false);
		}

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:// 微信
					orderType = 4;
					break;
				case 1:// 支付宝
					orderType = 1;
					break;
				case 2:// 翼支付
					orderType = 13;
					break;
				default:
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
	}

	/**
	 * 加载进度条
	 * 
	 * @param view
	 */
	public void onShowSuc() {
		mDialog = new YWLoadingDialog(this);
		mDialog.show();
		handler.sendEmptyMessageDelayed(LOAD_SUC_FINISH, 1500);
	}

	public void onShowFail(View view) {
		mDialog = new YWLoadingDialog(this);
		mDialog.show();
		handler.sendEmptyMessageDelayed(LOAD_FAIL_FINISH, 1500);
	}

	public Handler handler = new Handler() {
		@SuppressWarnings("static-access")
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dimissSuc();
				PromptBoxDialog promptDialog = new PromptBoxDialog(
						QRcode_Activity.this, "左右滑动可切换二维码");
				promptDialog.show();
				break;
			case LOAD_FAIL_FINISH:
				mDialog.dimissFail();
				break;
			case 1:
				// toast("待付款");
				break;
			case 3:
				break;
			case 4:
				toast("已取消");
				break;
			case 6:
				if(TextUtils.isEmpty(merchantId)){
					toast("该商户没有商户号");
					return;
				}
				Bitmap scanbitmap = QRImage
						.createQRImage("http://wechat.hfpay.net/wechat/parse/split/"
								+ merchantId);
				QR_code_weixin.setImageBitmap(QRImage
						.GetRoundedCornerBitmap(scanbitmap));

				break;
			case 7:
				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				String Time_new = sDateFormat.format(new java.util.Date());
				toast("支付成功");
				if (su.getPrefboolean(QRcode_Activity.this, "on_off", false)) {

					if (Build.MANUFACTURER.contains("BASEWIN")
							|| Build.MANUFACTURER.contains("basewin")) {
						Print pt = new Print();
						pt.prilay(QRcode_Activity.this, outOrderId,
								Double.toString(Double
										.parseDouble(qr_code_monery) / 100),
								"无（非扫码方式）", Paymode(orderType),
								MainActivity.cashiername, clientId, Time_new);
						toast("打印");
					} else {
						toast("未检测到打印机！无法进行打印打印");
					}
				} else {
					toast("打印机未开启");
				}
				finish();
				break;
			case 8:
				toast("获取信息超时！");
				break;
			}
		}
	};

	View.OnClickListener myclicklist = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_back:
				QRcode_Activity.this.finish();
				break;
			default:
				break;
			}

		}
	};

	public void showmessage(int message) {
		Message localMessage = new Message();
		localMessage.what = message;
		this.handler.sendMessage(localMessage);
	}

	private void toast(String str) {
		Toast.makeText(QRcode_Activity.this, str, Toast.LENGTH_SHORT).show();
	}

	public String Paymode(int modenum) {
		if (modenum == 1) {
			return "支付宝";
		} else if (modenum == 4) {
			return "微信";
		} else if (modenum == 13) {
			return "翼支付";
		}
		return null;
	}

	public int paymode_change(int r) {
		if (r == 1) {
			return 3;
		} else if (r == 4) {
			return 4;
		} else if (r == 13) {
			return 6;
		}
		return 0;
	}

}
