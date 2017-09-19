package com.jhj.imagecycleview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.bestpay.cn.utils.CryptTool;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.LoginActivity;
import com.example.Main.MyAppLication;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.Function.Recommend;
import com.jhj.Function.TransactionRecordActivity;
import com.jhj.SetUpActivity.JieSuan_Information_Activity;
import com.jhj.SetUpActivity.ShangHu_Information_Activity;
import com.jhj.imagecycleview.ImageCycleView.ImageCycleViewListener;
import com.jhj.info_util.Json;
import com.jhj.info_util.Utils;
import com.jhj.network.Http_PushTask;
import com.jhjpay.zyb.R;
import com.jhjpay.zyb.wxapi.ResourcesManager;
import com.mining.app.zxing.image.QRImage;
import com.mob.commons.SHARESDK;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageCycleActivity extends Activity {

	private ImageCycleView mAdView;

	private ArrayList<ADInfo> infos = new ArrayList<ADInfo>();

	private String[] imageUrls = { "http://ylaa.net/ads_img1.jpg",
			"http://ylaa.net/ads_img2.jpg", "http://ylaa.net/ads_img3.jpg",
			"http://ylaa.net/ads_img4.jpg", "http://ylaa.net/ads_img5.jpg" };

	private TextView transaction_record, settlement_details, product, cashier,
			shopping_mall, cellular_phone_replenishing, recommend, snr_cso,
			my_service_provider, make_money, credit_card, more;


	SharedPreferences_util su = new SharedPreferences_util();
	// 激活码
	private Dialog code_dg, settlement_dg, data_dg, login_dg;
	private ImageButton img_codesao;
	private EditText write_code;
	private Button determine, cancel;
	private String code_write;
	private Boolean write_code_off, settlement_information_off,
			merchant_information_off, login_off;
	private List<Recommend> reList;
	private String token;
	private String url;
	private Bitmap scanbitmap;
	private Json json;
	private Dialog re_dg;
	private ImageView img_qr_code;
	private Button bt_webview;
	private TextView tv_receName;
	private String qr_code;
	private YWLoadingDialog mDialog;
	private String uid;// 用户id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_cycle);
		MyAppLication.getInstance().addActivity(this);
		mDialog = new YWLoadingDialog(ImageCycleActivity.this);
		json = new Json();
		imitView();

		for (int i = 0; i < imageUrls.length; i++) {
			ADInfo info = new ADInfo();
			info.setUrl(imageUrls[i]);
			info.setContent("top-->" + i);
			infos.add(info);
		}

		mAdView = (ImageCycleView) findViewById(R.id.ad_view);
		mAdView.setImageResources(infos, mAdCycleViewListener);
		// 初始化推荐链接弹框
		showQuickQr_CodeDialog();
	}

	/**
	 * 初始化控件
	 */
	public void imitView() {
		transaction_record = (TextView) findViewById(R.id.transaction_record);// 交易记录
		settlement_details = (TextView) findViewById(R.id.settlement_details);// 结算明细
		product = (TextView) findViewById(R.id.product);// 产品
		cashier = (TextView) findViewById(R.id.cashier);// 收银台
		shopping_mall = (TextView) findViewById(R.id.shopping_mall);// 商城
		cellular_phone_replenishing = (TextView) findViewById(R.id.cellular_phone_replenishing);// 手机充值
		recommend = (TextView) findViewById(R.id.recommend);// 分享
		snr_cso = (TextView) findViewById(R.id.snr_cso);// 高级客服
		my_service_provider = (TextView) findViewById(R.id.my_service_provider);// 我的服务商
		make_money = (TextView) findViewById(R.id.make_money);// 我要赚钱
		credit_card = (TextView) findViewById(R.id.credit_card);// 申请信用卡
		more = (TextView) findViewById(R.id.more);// 更多

		transaction_record.setOnClickListener(functionlistener);
		settlement_details.setOnClickListener(functionlistener);
		product.setOnClickListener(functionlistener);
		cashier.setOnClickListener(functionlistener);
		shopping_mall.setOnClickListener(functionlistener);
		cellular_phone_replenishing.setOnClickListener(functionlistener);
		recommend.setOnClickListener(functionlistener);
		snr_cso.setOnClickListener(functionlistener);
		my_service_provider.setOnClickListener(functionlistener);
		make_money.setOnClickListener(functionlistener);
		credit_card.setOnClickListener(functionlistener);
		more.setOnClickListener(functionlistener);
	}

	/**
	 * 初始化激活码输入框
	 */
	public void showCodeDialog() {
		code_dg = new Dialog(ImageCycleActivity.this,
				R.style.edit_AlertDialog_style);
		code_dg.setContentView(R.layout.activity_dialog_activation_code);

		img_codesao = (ImageButton) code_dg.findViewById(R.id.img_codesao);
		write_code = (EditText) code_dg.findViewById(R.id.write_code);
		determine = (Button) code_dg.findViewById(R.id.code_determine);
		cancel = (Button) code_dg.findViewById(R.id.code_cancel);

		img_codesao.setOnClickListener(listener);
		determine.setOnClickListener(listener);
		cancel.setOnClickListener(listener);

		code_dg.setCanceledOnTouchOutside(false);
		code_dg.show();
	}

	/**
	 * 初始化结算信息提示框
	 */
	public void showSettLementDialog() {
		settlement_dg = new Dialog(ImageCycleActivity.this,
				R.style.edit_AlertDialog_style);
		settlement_dg
				.setContentView(R.layout.activity_dialog_settlementinformation);

		TextView cancel = (TextView) settlement_dg
				.findViewById(R.id.settlement_cancel);
		TextView perfect = (TextView) settlement_dg
				.findViewById(R.id.settlement_perfect);
		cancel.setOnClickListener(listener);
		perfect.setOnClickListener(listener);

		settlement_dg.setCanceledOnTouchOutside(false);
		settlement_dg.show();
	}

	/**
	 * 初始化商户信息提示框
	 */
	public void showInformationDialog() {
		data_dg = new Dialog(ImageCycleActivity.this,
				R.style.edit_AlertDialog_style);
		data_dg.setContentView(R.layout.activity_dialog_data_information);

		TextView cancel = (TextView) data_dg.findViewById(R.id.datacancel);
		TextView perfect = (TextView) data_dg.findViewById(R.id.dataperfect);
		cancel.setOnClickListener(listener);
		perfect.setOnClickListener(listener);

		data_dg.setCanceledOnTouchOutside(false);
		data_dg.show();
	}

	/**
	 * 初始化登录提示框
	 */
	public void showLoginDialog() {
		login_dg = new Dialog(ImageCycleActivity.this,
				R.style.edit_AlertDialog_style);
		login_dg.setContentView(R.layout.activity_dialog_login);

		TextView logincancel = (TextView) login_dg
				.findViewById(R.id.logincancel);
		TextView loginperfect = (TextView) login_dg
				.findViewById(R.id.loginperfect);

		logincancel.setOnClickListener(listener);
		loginperfect.setOnClickListener(listener);

		login_dg.setCanceledOnTouchOutside(false);
		login_dg.show();
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			// Toast.makeText(ImageCycleActivity.this,
			// "content->" + info.getContent(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoader.getInstance().displayImage(imageURL, imageView);
		}
	};

	/**
	 * 判断是否输入激活码及完善商户信息
	 */
	public boolean isPerfect() {
		boolean isPerfect = true;

		if (login_off == false) {
			showLoginDialog();
			isPerfect = false;
		} else if (merchant_information_off == false) {
			showInformationDialog();
			isPerfect = false;
		}
		return isPerfect;
	}

	OnClickListener listener = new OnClickListener() {

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.img_codesao:
				toast("暂不支持使用！");
				break;
			case R.id.code_determine:// 激活码-确定
				if (!TextUtils.isEmpty(write_code.getText())) {
					code_write = write_code.getText().toString().trim();
					if (code_write.equals("001")) {
						write_code_off = true;
						su.setPrefboolean(ImageCycleActivity.this,
								"write_code_off", write_code_off);
						code_dg.cancel();
					} else {
						toast("激活码错误");
						write_code_off = false;
						su.setPrefboolean(ImageCycleActivity.this,
								"write_code_off", write_code_off);
					}
				} else {
					toast("请输入激活码");
				}
				break;
			case R.id.code_cancel:// 激活码-取消
				code_dg.cancel();
				break;
			case R.id.settlement_cancel:// 结算信息-取消
				settlement_dg.cancel();
				break;
			case R.id.settlement_perfect:// 完善结算信息
				Intent setintent = new Intent(ImageCycleActivity.this,
						JieSuan_Information_Activity.class);
				setintent.putExtra("type", "settlement");
				startActivity(setintent);
				settlement_dg.cancel();
				break;
			case R.id.datacancel:// 资料信息-取消
				data_dg.cancel();
				break;
			case R.id.dataperfect:// 完善商户资料信息
				Intent dataintent = new Intent(ImageCycleActivity.this,
						ShangHu_Information_Activity.class);
				startActivity(dataintent);
				data_dg.cancel();
				break;
			case R.id.logincancel:
				login_dg.cancel();
				break;
			case R.id.loginperfect:
				Intent loginintent = new Intent(ImageCycleActivity.this,
						LoginActivity.class);
				startActivity(loginintent);
				login_dg.cancel();
				ImageCycleActivity.this.finish();
				break;

			default:
				break;
			}
		}

	};

	OnClickListener functionlistener = new OnClickListener() {

		Intent intent = new Intent();

		@SuppressWarnings("static-access")
		@Override
		public void onClick(View v) {
			// 激活码
			write_code_off = su.getPrefboolean(ImageCycleActivity.this,
					"write_code_off", false);
			// 结算信息
			settlement_information_off = su.getPrefboolean(
					ImageCycleActivity.this, "settlement_information_off",
					false);
			// 商户资料信息
			merchant_information_off = su.getPrefboolean(
					ImageCycleActivity.this, "merchant_information_off", false);
			// 用户id
			uid = su.getPrefString(ImageCycleActivity.this, "uid", null);
			// token
			token = su.getPrefString(ImageCycleActivity.this, "token", null);
			// 判断是否已登录
			login_off = su.getPrefboolean(ImageCycleActivity.this, "login_off",
					false);

			if (!isPerfect()) {
				return;
			}
			switch (v.getId()) {
			case R.id.transaction_record:// 交易记录
				// toast("暂不支持使用");
				intent.setClass(ImageCycleActivity.this,
						TransactionRecordActivity.class);
				startActivity(intent);
				break;
			case R.id.settlement_details:// 结算明细
				if (!isPerfect()) {
					return;
				}
				// toast("暂不支持使用");
				// intent.setClass(ImageCycleActivity.this,
				// TradingPayActivity.class);
				// startActivity(intent);
				// 荣邦接口
				// (new upload()).start();
				// 分润接口
				// (new uploadFenRun()).start();

				break;
			case R.id.product:// 产品

				toast("暂不支持使用");
				// intent.setClass(ImageCycleActivity.this,
				// ProductActivity.class);
				// startActivity(intent);
				break;
			case R.id.cashier:// 收银台
				toast("暂不支持使用");
				// intent.setClass(ImageCycleActivity.this,
				// Cashier_Activity.class);
				// startActivity(intent);
				break;
			case R.id.shopping_mall:// 商城
				toast("暂不支持使用");
				// intent.setClass(ImageCycleActivity.this,
				// ShopMallActivity.class);
				// startActivity(intent);
				break;
			case R.id.cellular_phone_replenishing:// 手机充值
				toast("暂不支持使用");
				// intent.setClass(ImageCycleActivity.this,
				// PhoneRechargeActivity.class);
				// startActivity(intent);
				break;
			case R.id.recommend:// 推荐
				// intent.setClass(ImageCycleActivity.this,
				// RecommendActivity.class);
				// startActivity(intent);
				mDialog.show();
				(new obtainProduct()).start();
				break;
			case R.id.snr_cso:// 高级客服
				toast("暂不支持使用");
				break;
			case R.id.my_service_provider:// 我的服务商
				toast("暂不支持使用");
				// intent.setClass(ImageCycleActivity.this,
				// MyServiceProviderActivity.class);
				// startActivity(intent);
				break;
			case R.id.make_money:// 我要赚钱
				toast("暂不支持使用");
				break;
			case R.id.credit_card:// 申请信用卡
				toast("暂不支持使用");
				// intent.setClass(ImageCycleActivity.this,
				// ApplyCreditCardActivity.class);
				// startActivity(intent);
				break;
			case R.id.more:// 更多
				toast("暂不支持使用");
				// intent.setClass(ImageCycleActivity.this,
				// More_Activity.class);
				// startActivity(intent);
				break;

			default:
				break;
			}
		}
	};

	// 发送handle
	public void showmessage(int message) {
		Message localMessage = new Message();
		localMessage.what = message;
		this.handler.sendMessage(localMessage);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mDialog.dismiss();
				if (reList == null) {
					toast("获取商品数据失败！");
					return;
				}
				String productId = "";
				for (int i = 0; i < reList.size(); i++) {
					productId += reList.get(i).getProductId() + ",";
				}
				qr_code = productId.substring(0, productId.length() - 1);
				url = "http://cnyssj.com/dfweb/sys/sysuser/login?"
						+ "referrerId=" + uid + "&" + "productIds=" + qr_code;
				scanbitmap = QRImage.createQRImage(url);
				img_qr_code.setImageBitmap(QRImage
						.GetRoundedCornerBitmap(scanbitmap));
				re_dg.show();
				break;
			default:
				break;
			}
		};
	};

	class upload extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();

			Map<String, String> param = new HashMap<String, String>();// 组装请求参数
			param.put("idCardName", "卢波");
			param.put("IDNumber", "37142519930502763X");
			param.put("bankNumber", "6227002271530099694");
			param.put("companyName", "山东题开店收银台");
			param.put("mobilePhone", "18315913992");
			param.put("accounttype", "1");
			param.put("bankcardtype", "1");
			param.put("storeAddress", "山东省济南市天桥区时代总部基地4期H3楼201");
			param.put("uid", "3617");

			Http_PushTask HP = new Http_PushTask();
			try {
				String result = HP.execute(CryptTool.transMapToString(param),
						"http://cnyssj.com/dfweb_test/rbkjsame.do").get();
				if (!TextUtils.isEmpty(result)) {
					JSONObject js = new JSONObject(result);
					if (js.getBoolean("result")) {

					} else {
						String message = js.getString("message");
					}
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class uploadFenRun extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();

			Map<String, String> param = new HashMap<String, String>();// 组装请求参数
			param.put("UID", "3617");

			Http_PushTask HP = new Http_PushTask();
			try {
				String result = HP
						.execute(
								"",
								"http://192.168.51.200:8080/dfweb/sys/profitFR/profitResultList?UID=10&start=1&limit=5")
						.get();
				if (!TextUtils.isEmpty(result)) {
					JSONObject js = new JSONObject(result);
					if (js.getBoolean("result")) {

					} else {
						String message = js.getString("message");
					}
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 推荐链接
	 * 
	 * @author lb
	 */
	class obtainProduct extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				Http_PushTask HP = new Http_PushTask();
				String result = HP.execute(
						"",
						"http://cnyssj.com/dfweb/sys/good/getGoodList?token="
								+ token + "&start=0&limit=100").get();

				if (!TextUtils.isEmpty(result)) {
					reList = new ArrayList<Recommend>();
					reList = json.parseJsonsByReProduct(result);
					showmessage(1);
				} else {
					toast("获取数据失败！");
					mDialog.dismiss();
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 初始化推荐链接弹框
	 */
	public void showQuickQr_CodeDialog() {
		re_dg = new Dialog(ImageCycleActivity.this,
				R.style.edit_AlertDialog_style);
		re_dg.setContentView(R.layout.activity_dialog_tuijian);

		img_qr_code = (ImageView) re_dg.findViewById(R.id.img_qr_code);
		bt_webview = (Button) re_dg.findViewById(R.id.bt_webview);
		tv_receName = (TextView) re_dg.findViewById(R.id.tv_receName);
		tv_receName.setText("长按图片可将二维码保存到本地！");
		re_dg.setCanceledOnTouchOutside(true);

		bt_webview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Utils.copyText(ImageCycleActivity.this, url);
//				toast("推荐链接已复制到粘贴板");
				if (scanbitmap != null) {
					Utils.saveBitmapForSdCard(getApplicationContext(),
							"huifu_tuijian", scanbitmap);
				}
				showShare();
			}
		});
//		img_qr_code.setOnLongClickListener(new OnLongClickListener() {
//
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				if (scanbitmap != null) {
//					Utils.saveBitmapForSdCard(getApplicationContext(),
//							"huifu_tuijian", scanbitmap);
//				}
//
//				return false;
//			}
//		});
	}
	/**
	 * 初始化分享弹框
	 */
	private void showShare() {

		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		oks.setTitle("挥付产品推荐链接");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(url);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(url);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/huifu_tuijian.png");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("挥付产品推荐链接");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(url);
		oks.setSilent(true);
		oks.setCallback(new PlatformActionListener() {
			@Override
			public void onComplete(Platform platform, int i,
					HashMap<String, Object> hashMap) {
				String msg = ResourcesManager.actionToString(i);
				String text = platform.getName() + " completed at " + msg;
				toast(getString(R.string.ssdk_oks_share_completed));
//				toast(text);
			}

			@Override
			public void onError(Platform platform, int i, Throwable throwable) {
				String msg = ResourcesManager.actionToString(i);
				String text = platform.getName() + "caught error at " + msg;
				toast(getString(R.string.ssdk_oks_share_failed));
//				toast(text);
			}

			@Override
			public void onCancel(Platform platform, int i) {
				String msg = ResourcesManager.actionToString(i);
				String text = platform.getName() + " canceled at " + msg;
				toast(getString(R.string.ssdk_oks_share_canceled));
//				toast(text);
			}
		});

		// 启动分享GUI
		oks.show(this);
	}

	public void toast(String str) {
		Toast.makeText(ImageCycleActivity.this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdView.startImageCycle();
	};

	@Override
	protected void onPause() {
		super.onPause();
		mAdView.pushImageCycle();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAdView.pushImageCycle();
	}

}
