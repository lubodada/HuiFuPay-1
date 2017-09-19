package com.jhj.SetUpActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bestpay.cn.utils.CryptTool;
import com.bestpay.cn.utils.Networkstate;
import com.bestpay.cn.utils.SharedPreferences_util;
import com.jhj.Dialog.PromptBoxDialog;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.info_util.FileUploadUtil;
import com.jhj.info_util.Iinformation;
import com.jhj.info_util.ImageTask;
import com.jhj.network.Http_PushTask;
import com.jhjpay.zyb.R;

/**
 * 上传资质图片
 * 
 * @author Administrator
 *
 */
public class ShangHuInfo_UploadPicturesActivity extends Activity implements
		OnClickListener {

	private static final int CAMERA_REQUEST = 1;
	private static final int PHOTO_CLIP = 2;

	private ImageButton img_back;
	private ImageView img_id_positive, img_id_back, img_id_positive_byhand,
			img_driving_license, img_bankcard_positive, img_bankcard_back;
	private Button com_ok;
	private String fileName;
	private Map<String, File> fileMap = new HashMap<String, File>();
	private boolean type_id_positive = false, type_id_back = false,
			type_id_positive_byhand = false, type_bankcard_positive = false,
			type_driving = false, type_bankcard_back = false;
	private FileUploadUtil fileUplod;
	private YWLoadingDialog mDialog;
	private String merchant, merchant_img, uid;
	private Boolean merchant_information_off;// 商户完善信息判断
	String error_msg, message;
	SharedPreferences_util su = new SharedPreferences_util();

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 商户资料信息
		merchant_information_off = su.getPrefboolean(
				ShangHuInfo_UploadPicturesActivity.this,
				"merchant_information_off", false);
		// 商户信息集合
		merchant = su.getPrefString(ShangHuInfo_UploadPicturesActivity.this,
				"merchant", null);
		merchant_img = su.getPrefString(
				ShangHuInfo_UploadPicturesActivity.this, "merchant_img", null);
		uid = su.getPrefString(ShangHuInfo_UploadPicturesActivity.this, "uid",
				null);// 用户id
		setContentView(R.layout.activity_setup_shanghuinfo_upload_pictures);
		initView();
		mDialog = new YWLoadingDialog(ShangHuInfo_UploadPicturesActivity.this);
		fileUplod = new FileUploadUtil(ShangHuInfo_UploadPicturesActivity.this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		img_back = (ImageButton) findViewById(R.id.img_back);
		img_id_positive = (ImageView) findViewById(R.id.img_id_positive);
		img_id_back = (ImageView) findViewById(R.id.img_id_back);
		img_id_positive_byhand = (ImageView) findViewById(R.id.img_id_positive_byhand);
		img_driving_license = (ImageView) findViewById(R.id.img_driving_license);
		img_bankcard_positive = (ImageView) findViewById(R.id.img_bankcard_positive);
		img_bankcard_back = (ImageView) findViewById(R.id.img_bankcard_back);
		com_ok = (Button) findViewById(R.id.com_ok);

		img_back.setOnClickListener(this);
		img_id_positive.setOnClickListener(this);
		img_id_back.setOnClickListener(this);
		img_id_positive_byhand.setOnClickListener(this);
		img_driving_license.setOnClickListener(this);
		img_bankcard_positive.setOnClickListener(this);
		img_bankcard_back.setOnClickListener(this);
		com_ok.setOnClickListener(this);

		if (!TextUtils.isEmpty(merchant_img)) {
			(new downloadImg()).start();
		}
	}

	class downloadImg extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			try {

				JSONObject jsonObject = new JSONObject(merchant_img);
				// 身份证正面
				ImageTask imageTask1 = new ImageTask(img_id_positive);
				imageTask1.execute(jsonObject.getString("id_card_front_img"));
				// type_id_positive = true;
				// 身份证反面
				ImageTask imageTask2 = new ImageTask(img_id_back);
				imageTask2.execute(jsonObject.getString("id_card_back_img"));
				// type_id_back = true;
				// 手持身份证
				ImageTask imageTask3 = new ImageTask(img_id_positive_byhand);
				imageTask3.execute(jsonObject
						.getString("holding_id_card_photo"));
				// type_id_positive_byhand = true;
				// //银行卡正面
				ImageTask imageTask4 = new ImageTask(img_bankcard_positive);
				imageTask4.execute(jsonObject.getString("bank_card_front_img"));
				// type_bankcard_positive = true;
				// //银行卡反面
				ImageTask imageTask5 = new ImageTask(img_bankcard_back);
				imageTask5.execute(jsonObject.getString("bank_card_back_img"));
				// type_bankcard_back = true;
				// //驾驶证
				ImageTask imageTask6 = new ImageTask(img_driving_license);
				imageTask6.execute(jsonObject.getString("driver_license_img"));
				// type_driving = true;

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从系统相机选择图片来源
	 */
	private void getCamera(String fileName) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		// 下面这句指定调用相机拍照后的照片存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), fileName + ".jpg")));
		startActivityForResult(intent, CAMERA_REQUEST);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			ShangHuInfo_UploadPicturesActivity.this.finish();
			break;
		case R.id.img_id_positive:// 身份证正面
			fileName = "id_card_front_img";
			getCamera(fileName);
			break;
		case R.id.img_id_back:// 身份证反面
			fileName = "id_card_back_img";
			getCamera(fileName);
			break;
		case R.id.img_id_positive_byhand:// 手持身份证正面
			fileName = "holding_id_card_photo";
			getCamera(fileName);
			break;
		case R.id.img_driving_license:// 驾驶证
			fileName = "driver_license_img";
			getCamera(fileName);
			break;
		case R.id.img_bankcard_positive:// 银行卡正面
			fileName = "bank_card_front_img";
			getCamera(fileName);
			break;
		case R.id.img_bankcard_back:// 银行卡反面
			fileName = "bank_card_back_img";
			getCamera(fileName);
			break;
		case R.id.com_ok:
			
			if (!type_id_positive || !type_id_back || !type_id_positive_byhand
					|| !type_bankcard_positive || !type_bankcard_back
					|| !type_driving) {
				showPromptBoxDialog("请上传完整照片信息");
				return;
			}
			if (fileMap != null) {
				Iinformation info = new Iinformation();
				info.Merchant_Img(ShangHuInfo_UploadPicturesActivity.this,
						fileMap);
			}

			if (Networkstate
					.isNetworkAvailable(ShangHuInfo_UploadPicturesActivity.this)) {
				mDialog.show();
				mDialog.setCanceledOnTouchOutside(false);
				(new FileUpload()).start();
			} else {
				toast("无可用网络! 请检查网络设置");
			}

			break;

		default:
			break;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CAMERA_REQUEST:
			switch (resultCode) {
			case -1:// -1表示拍照成功
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/" + fileName + ".jpg");// 保存图片
				if (file.exists()) {
					fileMap.put(
							fileName,
							new File(fileUplod.compressImage(file.toString(),
									file.toString(), 30)));

					setImageBitmap(fileUplod.cropBitmap(file.toString(), null,
							180, 180));
					// 对相机拍照照片进行裁剪
					// photoClip(Uri.fromFile(file));
				}
			}
			break;

		case PHOTO_CLIP:
			// 完成
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					// 显示图片
					setImageBitmap(photo);
				}
			}
			break;
		}
	}

	/****
	 * 调用系统自带切图工具对图片进行裁剪
	 * 
	 * @param uri
	 */
	private void photoClip(Uri uri) {
		// 调用系统中自带的图片剪裁
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_CLIP);
	}

	/**
	 * 获取照片后在imageview显示
	 * 
	 * @param photo
	 */
	public void setImageBitmap(Bitmap photo) {
		if (fileName.equals("id_card_front_img")) {
			img_id_positive.setImageBitmap(photo);
			type_id_positive = true;
		}
		if (fileName.equals("id_card_back_img")) {
			img_id_back.setImageBitmap(photo);
			type_id_back = true;
		}
		if (fileName.equals("holding_id_card_photo")) {
			img_id_positive_byhand.setImageBitmap(photo);
			type_id_positive_byhand = true;
		}
		if (fileName.equals("driver_license_img")) {
			img_driving_license.setImageBitmap(photo);
			type_driving = true;
		}
		if (fileName.equals("bank_card_front_img")) {
			img_bankcard_positive.setImageBitmap(photo);
			type_bankcard_positive = true;
		}
		if (fileName.equals("bank_card_back_img")) {
			img_bankcard_back.setImageBitmap(photo);
			type_bankcard_back = true;
		}
	}

	// 发送handle
	public void showmessage(int message) {
		Message localMessage = new Message();
		localMessage.what = message;
		this.handler.sendMessage(localMessage);
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				mDialog.dismiss();
				toast("资料上传成功！");
				break;
			case 2:
				mDialog.dismiss();
				toast(error_msg);
				break;
			case 3:
				mDialog.dismiss();
				toast("参数错误-400");
				break;
			}
		}

	};

	/**
	 * 上传注册信息
	 */
	class FileUpload extends Thread {
		@SuppressWarnings("static-access")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONObject js;
			String result = null;
			try {
				String url = "http://cnyssj.com/dfweb/merchant/register";
				Map<String, String> params = new HashMap<String, String>();
				if (merchant != null) {
					JSONObject jsonObject = new JSONObject(merchant);
					// 将商户信息封装进map
					params.put("idCardName", jsonObject.getString("idCardName")
							.toString());// 身份证姓名
					params.put("IDNumber", jsonObject.getString("idCardNumber")
							.toString());// 身份证号
					params.put("bankNumber",
							jsonObject.getString("bankCardNumber").toString());// 银行卡号
					params.put("companyName",
							jsonObject.getString("companyName").toString());// 公司名称
					params.put("mobilePhone",
							jsonObject.getString("mobileNumber").toString());// 手机号
					params.put("uid", uid);// 用户id
					
					params.put("bank", jsonObject.getString("bankcard")// 开户行
							.toString());
					params.put("scope", jsonObject.getString("business_range")// 经营范围
							.toString());
					params.put("city", jsonObject.getString("business_address")// 地址
							.toString());
					params.put("detai_address",
							jsonObject.getString("detail_address").toString());// 详细地址
				}
				result = fileUplod.uploadPost(url, params, fileMap);
				if (!TextUtils.isEmpty(result)) {
					if (result.equals("400")) {
						showmessage(3);
						return;
					}
					js = new JSONObject(result);
					if (js.getBoolean("success")) {
						// 商户状态判断 0：未注册 1：只注册蜂巢    2：蜂巢，银联都注册成功
						su.setPrefString(
								ShangHuInfo_UploadPicturesActivity.this,
								"registeredUnionpayMerchant", "2");
						merchant_information_off = true;
						su.setPrefboolean(
								ShangHuInfo_UploadPicturesActivity.this,
								"merchant_information_off",
								merchant_information_off);

						showmessage(1);
						// 费率
						su.setPrefString(
								ShangHuInfo_UploadPicturesActivity.this,
								"rate",
								js.getJSONObject("value").getString("rate"));
						// 商户号
						su.setPrefString(
								ShangHuInfo_UploadPicturesActivity.this,
								"merchantId", js.getJSONObject("value")
										.getString("merchantId"));
						// 身份证名称
						su.setPrefString(
								ShangHuInfo_UploadPicturesActivity.this,
								"idCardName", js.getJSONObject("value")
										.getString("idCardName"));
						// 公司名称
						su.setPrefString(
								ShangHuInfo_UploadPicturesActivity.this,
								"companyName", js.getJSONObject("value")
										.getString("companyName"));
						// 密钥
						su.setPrefString(
								ShangHuInfo_UploadPicturesActivity.this,
								"appSecret", js.getJSONObject("value")
										.getString("appSecret"));
						// 银行卡号
						su.setPrefString(
								ShangHuInfo_UploadPicturesActivity.this,
								"bankNumber", js.getJSONObject("value")
										.getString("bankNumber"));

					} else {
						su.setPrefString(
								ShangHuInfo_UploadPicturesActivity.this,
								"registeredUnionpayMerchant", "1");
						
						merchant_information_off = true;
						su.setPrefboolean(
								ShangHuInfo_UploadPicturesActivity.this,
								"merchant_information_off",
								merchant_information_off);
						error_msg = js.getString("messageText");
						showmessage(2);
					}
				} else {
					mDialog.dismiss();
					toast("数据获取失败！");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				mDialog.dismiss();
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化信息提示框
	 * 
	 * @param info
	 */
	public void showPromptBoxDialog(String info) {
		PromptBoxDialog D0Dialog = new PromptBoxDialog(
				ShangHuInfo_UploadPicturesActivity.this, info);
		D0Dialog.show();
	}

	public void toast(String str) {
		Toast.makeText(ShangHuInfo_UploadPicturesActivity.this, str,
				Toast.LENGTH_LONG).show();
	}
}
