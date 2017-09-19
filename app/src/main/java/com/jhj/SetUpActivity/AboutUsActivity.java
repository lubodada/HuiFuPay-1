package com.jhj.SetUpActivity;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.Version_util;
import com.example.Main.MyAppLication;
import com.jhj.network.Http_PushTask;
import com.jhj.servrce.UpdateService;
import com.jhjpay.zyb.R;

/**
 * 关于翼开店
 * @author Administrator
 *
 */
public class AboutUsActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private RelativeLayout re_service_agreement,re_testing_version;
	private TextView tv_version,tv_testing_version,et_UP_data;
	private String version;
	private Button UP_data_qx,UP_data_qd;
	String result;
	boolean force_update;
	String url;
	String description;
	Version_util Vn = new Version_util();
	Dialog dia_update;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyAppLication.getInstance().addActivity(this);
		setContentView(R.layout.activity_setup_about_us);
		Context context = AboutUsActivity.this;
		tv_version=(TextView) findViewById(R.id.tv_version);
		tv_testing_version=(TextView) findViewById(R.id.tv_testing_version);
		img_back=(ImageButton) findViewById(R.id.img_back);
		re_service_agreement=(RelativeLayout) findViewById(R.id.re_service_agreement);
		re_testing_version=(RelativeLayout) findViewById(R.id.re_testing_version);
		
		img_back.setOnClickListener(this);
		re_service_agreement.setOnClickListener(this);
		re_testing_version.setOnClickListener(this);
		
		dia_update = new Dialog(context, R.style.edit_AlertDialog_style);
		dia_update.setContentView(R.layout.activity_start_dialog_update);
		et_UP_data = (TextView) dia_update.findViewById(R.id.et_UP_data);
		UP_data_qx = (Button) dia_update.findViewById(R.id.UP_data_qx);
		UP_data_qd = (Button) dia_update.findViewById(R.id.UP_data_qd);
		UP_data_qx.setOnClickListener(this);
		UP_data_qd.setOnClickListener(this);
		
		version = Vn.getVersionName(AboutUsActivity.this);
		tv_version.setText(version);
		tv_testing_version.setText(version);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			AboutUsActivity.this.finish();
			break;
		case R.id.re_service_agreement:
			
			break;
		case R.id.re_testing_version:
			showmessage(1);
			break;
		case R.id.UP_data_qx:
			if (!force_update) {
				dia_update.cancel();
			} else {
				stoi("大哥，新版本可美了，你得升级啊！");
			}

			break;
		case R.id.UP_data_qd:
			dia_update.cancel();
			// 跳转service下载程序
			Intent intent = new Intent(AboutUsActivity.this,
					UpdateService.class);
			intent.putExtra("Key_App_Name", "翼开店收银台");
			intent.putExtra("Key_Down_Url", url);
			startService(intent);
			break;	
		default:
			break;
		}
	}
	// 发送handle
		public void showmessage(int message) {
			Message localMessage = new Message();
			localMessage.what = message;
			this.handler.sendMessage(localMessage);
		}
		
		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					(new UpdateVersion()).start();
					break;
				case 2:
					stoi("您已是最新版本，无需升级！");
					tv_testing_version.setText("最新版本");
					break;
				case 3:
					if (force_update) {
						UP_data_qx
								.setBackgroundResource(R.drawable.button_oncleck_item_num);
					}
					dia_update.setCancelable(false);
					Window w;
					WindowManager.LayoutParams lp;
					dia_update.show();
					dia_update.setCanceledOnTouchOutside(false);
					w = dia_update.getWindow();
					lp = w.getAttributes();
					lp.x = 0;
					lp.y = 40;
					dia_update.onWindowAttributesChanged(lp);
					et_UP_data.setText(description);
					break;

				default:
					break;
				}
			};
		};
		class UpdateVersion extends Thread {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				try {
					Http_PushTask HP = new Http_PushTask();
					result = HP.execute(
							"",
							"http://cnyssj.net/auto_update/?" + "app_name="
									+ Vn.getAppInfo(AboutUsActivity.this)).get();
					JSONObject js = new JSONObject(result);
					if (!TextUtils.isEmpty(result)) {
						js.getString("version");
						if (version.equals(js.getString("version"))
								|| "false".equals(js.getString("version"))) {
							showmessage(2);
							return;
						} else {
							force_update = js.getBoolean("force_update");
							url = js.getString("url");
							description = js.getString("description");
							showmessage(3);
							return;
						}
					} else {
						showmessage(5);
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					e1.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
		public void stoi(String str) {
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		}
}
