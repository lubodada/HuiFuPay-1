package com.jhj.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jhj.Dialog.YWLoadingDialog;
import com.jhj.Function.RecommendProductAdapter.ViewHolder;
import com.jhj.info_util.Json;
import com.jhj.info_util.Utils;
import com.jhj.network.Http_PushTask;
import com.jhjpay.zyb.R;
import com.timepay.zyb.PayUtil;

/**
 * 推荐
 * @author lb
 *
 */
public class RecommendActivity extends Activity{

	private ImageButton img_back;
	private ListView listview;
	private Button bt_make_recommend;
	private RecommendProductAdapter recommendAdapter;
	private List<Recommend> reList;
	private String uid;//用户id 
	private YWLoadingDialog mDialog;
	private Json json;
	private Dialog re_dg;
	private ImageView img_qr_code;
	private Button bt_webview;
	private TextView tv_receName;
	private String qr_code;
	private int QR_WIDTH = 300;
	private int QR_HEIGHT = 300;
	SharedPreferences_util su = new SharedPreferences_util();
	String token;
	String url;
	Bitmap scanbitmap;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		uid = su.getPrefString(RecommendActivity.this, "uid", null);
		token = su.getPrefString(RecommendActivity.this, "token", null);
		setContentView(R.layout.activity_function_recommend);
		mDialog = new YWLoadingDialog(RecommendActivity.this);
		json = new Json();
		img_back = (ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RecommendActivity.this.finish();
			}
		});
		listview = (ListView) findViewById(R.id.listview);
		bt_make_recommend = (Button) findViewById(R.id.bt_make_recommend);
		bt_make_recommend.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String mapKey,productId ="";
				Iterator<Entry<String, Boolean>> iter = RecommendProductAdapter.getIsSelected().entrySet().iterator();
				while (iter.hasNext()) {
					HashMap.Entry entry = (HashMap.Entry) iter.next();
					if((boolean) entry.getValue() == true){
						mapKey = (String) entry.getKey();
						productId += mapKey + ",";
					}
				}
				if(TextUtils.isEmpty(productId)){
					toast("请选择推荐产品！");
					return;
				}
				qr_code = productId.substring(0, productId.length()-1);
				showmessage(2);
			}
		});

		// 绑定listView的监听器
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				ViewHolder holder = (ViewHolder) arg1.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				RecommendProductAdapter.getIsSelected().put(reList.get(arg2).getProductId(), holder.cb.isChecked());
				// 调整选定条目
				if (holder.cb.isChecked() == true) {

				} else {

				}
			}
		});
		//获取产品
		mDialog.show();
		showmessage(0);

	}
	
	/**
	 * 初始化推荐链接弹框
	 */
	public void showQuickQr_CodeDialog(){
		re_dg = new Dialog(RecommendActivity.this, R.style.edit_AlertDialog_style);
		re_dg.setContentView(R.layout.activity_quickpay_qr_code);

		img_qr_code = (ImageView) re_dg.findViewById(R.id.img_qr_code);
		bt_webview = (Button) re_dg.findViewById(R.id.bt_webview);
		tv_receName = (TextView) re_dg.findViewById(R.id.tv_receName);
		tv_receName.setText("长按图片可将二维码保存到本地！");
		bt_webview.setText("复制推荐链接");
		re_dg.setCanceledOnTouchOutside(true);
		
		bt_webview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.copyText(RecommendActivity.this, url);
				toast("推荐链接已复制到粘贴板");
			}
		});
		img_qr_code.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(scanbitmap!=null){
					Utils.saveBitmapForSdCard(getApplicationContext(),PayUtil.getCurrentDataTime1(),scanbitmap);
				}

				return false;
			}
		});
	}
	// 发送handle
	public void showmessage(int message) {
		Message localMessage = new Message();
		localMessage.what = message;
		this.handler.sendMessage(localMessage);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				(new obtainProduct()).start();
				break;
			case 1:
				mDialog.dismiss();
				if(reList.size()==0){
					toast("获取商品数据失败！");
					return;
				}
				// 实例化自定义的MyAdapter
				recommendAdapter = new RecommendProductAdapter(reList, RecommendActivity.this);
				// 绑定Adapter
				listview.setAdapter(recommendAdapter);
				bt_make_recommend.setVisibility(View.VISIBLE);
				break;
			case 2:
				showQuickQr_CodeDialog();
				url = "http://cnyssj.com/dfweb/sys/sysuser/login?"+"referrerId="+uid+"&"+"productIds="+qr_code;
				scanbitmap = createQRImage(url);
				img_qr_code.setImageBitmap(GetRoundedCornerBitmap(scanbitmap));
				re_dg.show();
				break;
			default:
				break;
			}
		};
	};
	class obtainProduct extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				
				Http_PushTask HP = new Http_PushTask();
				String result = HP.execute("",
						"http://cnyssj.com/dfweb/sys/good/getGoodList?token="+token+"&start=0&limit=100").get();

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
	// 生成二维码图片
	public Bitmap createQRImage(String url) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];

			for (int y = 0; y < QR_HEIGHT; y++) {
				for (int x = 0; x < QR_WIDTH; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * QR_WIDTH + x] = 0xff000000;
					} else {
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// 生成圆角图片
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			final float roundPx = 15;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}
	public void toast(String str) {
		Toast.makeText(RecommendActivity.this, str, Toast.LENGTH_SHORT).show();
	}

}
