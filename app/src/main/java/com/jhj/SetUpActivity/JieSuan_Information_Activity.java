package com.jhj.SetUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MyAppLication;
import com.jhj.info_util.Iinformation;
import com.jhj.info_util.Utils;
import com.jhjpay.zyb.R;

/**
 * 结算信息
 * @author Administrator
 *
 */
public class JieSuan_Information_Activity extends Activity implements OnClickListener{

	private EditText name,bankcard,phone_number;
	private Button com_ok;
	private TextView btnbank;
	private String uname,bcard,bank,phone;
	private String settlement;
	private ImageButton img_back;
	private Boolean settlement_information_off;//判断是否完善结算信息
	final String arr[]=new String[]{  
			"中国工商银行","中国农业银行","中国银行","中国建设银行","中国光大银行",  
			"中国民生银行","广东发展银行","平安银行(深圳发展银行)","招商银行","交通银行",
			"华夏银行","中信银行","兴业银行","邮政储蓄","上海浦东发展银行"  
	};
	SharedPreferences_util su = new SharedPreferences_util();
	private ListPopupWindow mListPop;
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//结算信息集合
		settlement=su.getPrefString(JieSuan_Information_Activity.this, "settlement", null);
		setContentView(R.layout.activity_setup_jiesuan_info);
		MyAppLication.getInstance().addActivity(this);
		initView();
		showListPopupWindow();
	}

	public void initView(){
		name=(EditText) findViewById(R.id.name);
		bankcard=(EditText) findViewById(R.id.bankcard);
		btnbank=(TextView) findViewById(R.id.btnbank);
		phone_number=(EditText) findViewById(R.id.phone_number);
		com_ok=(Button) findViewById(R.id.com_ok);
		img_back=(ImageButton) findViewById(R.id.img_back);

		btnbank.setOnClickListener(this);
		img_back.setOnClickListener(this);
		com_ok.setOnClickListener(this);

		try {
			if(settlement != null){

				JSONObject jsonObject = new JSONObject(settlement);
				uname=jsonObject.getString("name").toString();
				bcard=jsonObject.getString("bankcard").toString();
				bank=jsonObject.getString("bank").toString();
				phone=jsonObject.getString("phone").toString();

				name.setText(uname);
				bankcard.setText(bcard);
				btnbank.setText(bank);
				phone_number.setText(phone);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 加载开户行集合
	 */
	@SuppressLint("NewApi")
	public void showListPopupWindow(){
		 mListPop = new ListPopupWindow(this);
	        mListPop.setAdapter(new ArrayAdapter<Object>(this, R.layout.choice_item, arr));
	        mListPop.setWidth(LayoutParams.MATCH_PARENT);
	        mListPop.setHeight(LayoutParams.WRAP_CONTENT);
	        mListPop.setAnchorView(findViewById(R.id.view_jiesuan_line));//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
	        mListPop.setModal(true);//设置是否是模式
	        mListPop.setVerticalOffset(3);
	        mListPop.setDropDownGravity(Gravity.CENTER_HORIZONTAL);
	        mListPop.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.half)));
	        mListPop.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					btnbank.setText(arr[position]);
					mListPop.dismiss();
				}
			});
	}
	@SuppressWarnings("static-access")
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			JieSuan_Information_Activity.this.finish();
			break;
		case R.id.btnbank:
			mListPop.show();
			break;
		case R.id.com_ok:
			uname=name.getText().toString().trim();
			bcard=bankcard.getText().toString().trim();
			bank=btnbank.getText().toString().trim();
			phone=phone_number.getText().toString().trim();
			if(TextUtils.isEmpty(uname)){
				toast("姓名不能为空");
			}else if(TextUtils.isEmpty(bcard)){
				toast("银行卡号不能为空");
			}else if(TextUtils.isEmpty(bank)){
				toast("开户行不能为空");
			}else if(TextUtils.isEmpty(phone)){
				toast("手机号不能为空");
			}else if(Utils.isMobileNO(phone)==false){
				toast("手机号错误");
			}else{
				Iinformation info = new Iinformation();
				info.Settlement(JieSuan_Information_Activity.this, uname, bcard, bank, phone);
				settlement_information_off=true;
				su.setPrefboolean(JieSuan_Information_Activity.this, "settlement_information_off", settlement_information_off);
				su.setPrefString(JieSuan_Information_Activity.this, "bank_number", bcard);
			}
			break;
		default:
			break;
		}
	}

	public void toast(String str){
		Toast.makeText(JieSuan_Information_Activity.this, str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}

}
