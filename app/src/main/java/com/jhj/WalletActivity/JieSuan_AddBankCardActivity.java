package com.jhj.WalletActivity;

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
import com.jhj.info_util.Utils;
import com.jhjpay.zyb.R;

/**
 * 添加银行卡
 * @author Administrator
 *
 */
public class JieSuan_AddBankCardActivity extends Activity implements OnClickListener{

	private EditText name,bankcard_number,phone_number,et_code;
	private Button btn_code,com_ok;
	private TextView bankcard;
	private String uname,bcard_number,bank,phone,code;
	private String settlement;
	private ImageButton img_back;
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
		settlement=su.getPrefString(JieSuan_AddBankCardActivity.this, "settlement", null);
		setContentView(R.layout.activity_wallet_jiesuan_bankcard_add);
		MyAppLication.getInstance().addActivity(this);
		initView();
		showListPopupWindow();
	}
	public void initView(){
		name=(EditText) findViewById(R.id.name);
		bankcard_number=(EditText) findViewById(R.id.bankcard_number);//银行卡号
		bankcard=(TextView) findViewById(R.id.bankcard);//银行
		phone_number=(EditText) findViewById(R.id.phone_number);
		com_ok=(Button) findViewById(R.id.com_ok);
		img_back=(ImageButton) findViewById(R.id.img_back);
		btn_code=(Button) findViewById(R.id.btn_code);//短信验证
		et_code=(EditText) findViewById(R.id.et_code);

		bankcard.setOnClickListener(this);
		img_back.setOnClickListener(this);
		com_ok.setOnClickListener(this);
		btn_code.setOnClickListener(this);

		try {
			if(settlement != null){

				JSONObject jsonObject = new JSONObject(settlement);
				uname=jsonObject.getString("name").toString();
				phone=jsonObject.getString("phone").toString();
				phone_number.setText(phone);
				name.setText(uname);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			JieSuan_AddBankCardActivity.this.finish();
			break;
		case R.id.btn_code://短信验证

			break;
		case R.id.bankcard://银行
			mListPop.show();
			break;
		case R.id.com_ok:
			uname=name.getText().toString().trim();
			bcard_number=bankcard_number.getText().toString().trim();
			bank=bankcard.getText().toString().trim();
			phone=phone_number.getText().toString().trim();
			code=et_code.getText().toString().trim();
			if(TextUtils.isEmpty(uname)){
				toast("姓名不能为空");
			}else if(TextUtils.isEmpty(bcard_number)){
				toast("银行卡号不能为空");
			}else if(TextUtils.isEmpty(bank)){
				toast("开户行不能为空");
			}else if(TextUtils.isEmpty(phone)){
				toast("手机号不能为空");
			}else if(Utils.isMobileNO(phone)==false){
				toast("手机号格式错误");
			}else if(TextUtils.isEmpty(code)){
				toast("请填写验证码");
			}else{
				finish();
			}
			break;	
		default:
			break;
		}
	}
	@SuppressLint("NewApi")
	public void showListPopupWindow(){
		mListPop = new ListPopupWindow(this);
		mListPop.setAdapter(new ArrayAdapter<Object>(this, R.layout.choice_bank_item, arr));
		mListPop.setWidth(LayoutParams.MATCH_PARENT);
		mListPop.setHeight(480);
		mListPop.setAnchorView(findViewById(R.id.view_line));//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
		mListPop.setModal(true);//设置是否是模式
		mListPop.setVerticalOffset(5);
		mListPop.setDropDownGravity(Gravity.CENTER_HORIZONTAL);
		mListPop.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.half)));
		mListPop.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				bankcard.setText(arr[position]);
				mListPop.dismiss();
			}
		});
	}
	public void toast(String str){
		Toast.makeText(JieSuan_AddBankCardActivity.this, str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
}
