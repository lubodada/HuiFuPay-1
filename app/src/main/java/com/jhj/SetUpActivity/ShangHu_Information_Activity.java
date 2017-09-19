package com.jhj.SetUpActivity;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bestpay.cn.utils.SharedPreferences_util;
import com.example.Main.MyAppLication;
import com.jhj.info_util.Iinformation;
import com.jhjpay.zyb.R;

/**
 * 商户信息
 * @author Administrator
 *
 */
public class ShangHu_Information_Activity extends BaseActivity implements View.OnClickListener,OnWheelChangedListener{

	private EditText username,ID_number,bankcard,merchant_name,detailed_address,ver_phone;
	private TextView management_scope,province_city,btnbank;
	private ImageButton img_back;
	private Button next;
	private String urname,id_number,bankcard_number,bank,shname,detai_address,phone,scope,city;
	private String merchant;
	
	final String scopearr[]=new String[]{  
			"宾馆、酒店类","餐饮类","珠宝、工艺类","酒吧、KTV","其他娱乐类","房地产类",  
			"服装类","汽车类","其他批发类","零售店","航空售票类","加油类",
			"超市类","百货商店","铁路客运类","公路客运类","计算机网络/信息服务", 
			"电器销售类","文具、办公用品","药房、药店","公立医院类","教育、学校类","其他商业服务"
	};
	final String bankarr[]=new String[]{  
			"中国工商银行","中国农业银行","中国银行","中国建设银行","中国光大银行",  
			"中国民生银行","广东发展银行","平安银行(深圳发展银行)","招商银行","交通银行",
			"华夏银行","中信银行","兴业银行","邮政储蓄","上海浦东发展银行"  
	};
	private ListPopupWindow mListPop;
	SharedPreferences_util su = new SharedPreferences_util();
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private TextView cancel,mBtnConfirm;
	private PopupWindow city_pw;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		merchant=su.getPrefString(ShangHu_Information_Activity.this, "merchant", null);//商户信息集合
		phone=su.getPrefString(ShangHu_Information_Activity.this, "userName", null);//手机号(登录的用户名)
		setContentView(R.layout.activity_setup_shanghu_info);
		MyAppLication.getInstance().addActivity(this);
		initView();
		showCityWindow();
		showListPopupWindow();
	}

	public void initView(){
		username=(EditText) findViewById(R.id.username);//商户姓名
		ID_number=(EditText) findViewById(R.id.ID_number);//身份证号
		bankcard=(EditText) findViewById(R.id.bankcard);//银行卡号
		btnbank=(TextView) findViewById(R.id.btnbank);//开户行
		merchant_name=(EditText) findViewById(R.id.merchant_name);//公司名称
		management_scope=(TextView) findViewById(R.id.management_scope);//经营范围
		province_city = (TextView) findViewById(R.id.province_city);//经营地址
		detailed_address=(EditText) findViewById(R.id.detailed_address);//详细地址
		ver_phone=(EditText) findViewById(R.id.ver_phone);//服务电话
		ver_phone.setText(phone);
		img_back=(ImageButton) findViewById(R.id.img_back);//返回
		next=(Button) findViewById(R.id.next);//下一步

		management_scope.setOnClickListener(this);
		btnbank.setOnClickListener(this);
		img_back.setOnClickListener(this);
		province_city.setOnClickListener(this);
		next.setOnClickListener(this);
		
		try {
			if(merchant != null){

				JSONObject jsonObject = new JSONObject(merchant);
				urname=jsonObject.getString("idCardName").toString();
				id_number=jsonObject.getString("idCardNumber").toString();
				bankcard_number=jsonObject.getString("bankCardNumber").toString();
				bank=jsonObject.getString("bankcard").toString();
				shname=jsonObject.getString("companyName").toString();
				scope=jsonObject.getString("business_range").toString();
				city=jsonObject.getString("business_address").toString();
				detai_address=jsonObject.getString("detail_address").toString();
				phone=jsonObject.getString("mobileNumber").toString();
				//姓名
				if(!urname.equals("null")){
					username.setText(urname);
					username.setSelection(username.getText().toString().length());
				}
				//身份证号
				if(!id_number.equals("null")){
					ID_number.setText(id_number);
				}
				//银行卡号
				if(!bankcard_number.equals("null")){
					bankcard.setText(bankcard_number);
				}
				//公司名称
				if(!shname.equals("null")){
					merchant_name.setText(shname);
				}
				//手机号
				if(!phone.equals("null")){
					ver_phone.setText(phone);
				}
				//开户行
				if(!bank.equals("null")){
					btnbank.setText(bank);
				}
				//经营范围
				if(!scope.equals("null")){
					management_scope.setText(scope);
				}
				//地址
				if(!city.equals("null")){
					province_city.setText(city);
				}
				//详细地址
				if(!detai_address.equals("null")){
					detailed_address.setText(detai_address);
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setUpViews(View v) {
		mViewProvince = (WheelView) v.findViewById(R.id.id_province);
		mViewCity = (WheelView) v.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) v.findViewById(R.id.id_district);
		mBtnConfirm = (TextView) v.findViewById(R.id.btn_confirm);
		cancel = (TextView) v.findViewById(R.id.cancel);

		mViewProvince.addChangingListener(this);
		mViewCity.addChangingListener(this);
		mViewDistrict.addChangingListener(this);
		mBtnConfirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}
	/**
	 * 加载开户行集合
	 */
	@SuppressLint("NewApi")
	public void showListPopupWindow(){
		 mListPop = new ListPopupWindow(this);
	        mListPop.setAdapter(new ArrayAdapter<Object>(this, R.layout.choice_item, bankarr));
	        mListPop.setWidth(LayoutParams.MATCH_PARENT);
	        mListPop.setHeight(LayoutParams.WRAP_CONTENT);
	        mListPop.setAnchorView(findViewById(R.id.view_jiesuan_line));//设置ListPopupWindow的锚点，即关联PopupWindow的显示位置和这个锚点
	        mListPop.setModal(true);//设置是否是模式
	        mListPop.setVerticalOffset(3);
	        mListPop.setDropDownGravity(Gravity.CENTER);
	        mListPop.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.half)));
	        mListPop.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					btnbank.setText(bankarr[position]);
					mListPop.dismiss();
				}
			});
	}
	/**
	 * 加载省市区数据
	 */
	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(ShangHu_Information_Activity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showCityWindow() {
		View contentView=getLayoutInflater().inflate(R.layout.activity_populwindow_city, null);
		city_pw=new PopupWindow(contentView, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight(), true);
		//设置点击弹窗外隐藏自身
		city_pw.setFocusable(true);
		city_pw.setOutsideTouchable(true);
		//设置pipupwindow弹出动画
		city_pw.setAnimationStyle(R.style.popupwindow_anim_style);
		//设置popupwindow背景
		city_pw.setBackgroundDrawable(new ColorDrawable());
//		pw.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);

		//处理popupwindow
		setUpViews(contentView);
		setUpData();
	}
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}


	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			ShangHu_Information_Activity.this.finish();
			break;
		case R.id.management_scope:
			Builder jobBuilder = new Builder(ShangHu_Information_Activity.this,R.style.dialog);
			jobBuilder.setAdapter(new ArrayAdapter<Object>(ShangHu_Information_Activity.this, R.layout.choice_scope_item, scopearr),
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO 选择经营范围
					scope=scopearr[which].toString();
					management_scope.setText(scope);
				}
			});
			jobBuilder.create().show();
			break;
		case R.id.province_city:
			city_pw.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
			break;
		case R.id.btn_confirm:
			province_city.setText(mCurrentProviceName+"-"+mCurrentCityName+"-"+mCurrentDistrictName);
			city_pw.dismiss();
			break;
		case R.id.btnbank:
			mListPop.show();
			break;	
		case R.id.cancel:
			city_pw.dismiss();
			break;	
		case R.id.next:
			urname=username.getText().toString().trim();
			id_number=ID_number.getText().toString().trim();
			bankcard_number=bankcard.getText().toString().trim();
			bank=btnbank.getText().toString().trim();
			shname=merchant_name.getText().toString().trim();
			scope=management_scope.getText().toString().trim();
			city=province_city.getText().toString().trim();
			detai_address=detailed_address.getText().toString().trim();
			phone=ver_phone.getText().toString().trim();
			
			if(TextUtils.isEmpty(urname)){
				toast("商户姓名不能为空");
			}else if(TextUtils.isEmpty(id_number)){
				toast("身份证号不能为空");
			}else if(TextUtils.isEmpty(bankcard_number)){
				toast("银行卡号不能为空");
			}else if(TextUtils.isEmpty(shname)){
				toast("公司名称不能为空");
			}else if(TextUtils.isEmpty(phone)){
				toast("手机号不能为空");
			}else{
				Iinformation info = new Iinformation();
				info.Merchant(ShangHu_Information_Activity.this, urname, id_number, bankcard_number, bank, shname, scope, city, detai_address, phone);
				
				Intent intent = new Intent(ShangHu_Information_Activity.this,ShangHuInfo_UploadPicturesActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	public void toast(String str){
		Toast.makeText(ShangHu_Information_Activity.this, str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyAppLication.getInstance().removeActivity(this);
	}
}
