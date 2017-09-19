package com.jhj.Function;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhj.info_util.GetImageByUrl;
import com.jhjpay.zyb.R;
import com.timepay.zyb.PayUtil;

/**
 * 订单及地址
 * @author lb
 */
public class ShopMall_OrderLocationActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private ImageView img_card;
	private TextView tv_card_type,tv_modify,tv_minus,tv_number,tv_add,tv_total_money;
	private EditText et_name,et_location,et_phone;
	private Button com_ok;
	private int number = 1;
	private String amount,name,location,phone,imgUrl,money;
	private double total_money;
	private String unit_price;
	private String order_number,card_type;
	private String order_state = "1";//订单状态  1：待付款 2：已付款  3：已取消
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_shopmall_orderlocation);

		initView();
		unit_price = getIntent().getStringExtra("unit_price");
		imgUrl = getIntent().getStringExtra("imgUrl");
		GetImageByUrl getImageByUrl = new GetImageByUrl();
		getImageByUrl.setImage(img_card, imgUrl);
	}
	private void initView() {
		// TODO Auto-generated method stub
		img_back = (ImageButton) findViewById(R.id.img_back);
		img_card = (ImageView) findViewById(R.id.img_card);
		tv_card_type = (TextView) findViewById(R.id.tv_card_type);
		tv_modify = (TextView) findViewById(R.id.tv_modify);
		tv_minus = (TextView) findViewById(R.id.tv_minus);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_add = (TextView) findViewById(R.id.tv_add);
		tv_total_money = (TextView) findViewById(R.id.tv_total_money);
		et_name = (EditText) findViewById(R.id.et_name);
		et_name.setFocusableInTouchMode(false);
		et_name.setFocusable(false);
		et_name.setEnabled(false);
		et_location = (EditText) findViewById(R.id.et_location);
		et_location.setFocusableInTouchMode(false);
		et_location.setFocusable(false);
		et_location.setEnabled(false);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setFocusableInTouchMode(false);
		et_phone.setFocusable(false);
		et_phone.setEnabled(false);
		com_ok = (Button) findViewById(R.id.com_ok);

		img_back.setOnClickListener(this);
		tv_minus.setOnClickListener(this);//减
		tv_add.setOnClickListener(this);//加
		tv_modify.setOnClickListener(this);//修改信息
		com_ok.setOnClickListener(this);

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				tv_number.setText(String.valueOf(number));
				total_money = number*Double.valueOf(unit_price);
				tv_total_money.setText("￥"+total_money);
				break;

			default:
				break;
			}
		};
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			ShopMall_OrderLocationActivity.this.finish();
			break;
		case R.id.tv_minus:
			amount = tv_number.getText().toString().trim();
			number = Integer.valueOf(amount);
			if(number <= 1){
				return;
			}
			--number;
			handler.sendEmptyMessage(1);
			break;
		case R.id.tv_add:
			amount = tv_number.getText().toString().trim();
			number = Integer.valueOf(amount);
			++number;
			handler.sendEmptyMessage(1);
			break;
		case R.id.tv_modify:
			et_name.setFocusableInTouchMode(true);
			et_name.setFocusable(true);
			et_name.setEnabled(true);
			et_name.setTextColor(Color.BLACK);

			et_location.setFocusableInTouchMode(true);
			et_location.setFocusable(true);
			et_location.setEnabled(true);
			et_location.setTextColor(Color.BLACK);

			et_phone.setFocusableInTouchMode(true);
			et_phone.setFocusable(true);
			et_phone.setEnabled(true);
			et_phone.setTextColor(Color.BLACK);
			break;
		case R.id.com_ok:
			name = et_name.getText().toString().trim();
			location = et_location.getText().toString().trim();
			phone = et_phone.getText().toString().trim();
			card_type = tv_card_type.getText().toString().trim();
			money = tv_total_money.getText().toString().trim();
			if(TextUtils.isEmpty(name) || TextUtils.isEmpty(location) || TextUtils.isEmpty(phone)){
				Toast.makeText(ShopMall_OrderLocationActivity.this, "请填写完整收货信息", Toast.LENGTH_LONG).show();
				return;
			}
			order_number = PayUtil.getCurrentDataTime1();
			Intent intent = new Intent(ShopMall_OrderLocationActivity.this,ShopMall_OrderDetailActivity.class);
			intent.putExtra("order_number", order_number);//订单编号
			intent.putExtra("order_state", order_state);//订单状态
			intent.putExtra("card_type", card_type);//卡片类型
			intent.putExtra("number", String.valueOf(number));//数量
			intent.putExtra("total_money", money.subSequence(1, money.length()));//金额
			intent.putExtra("name", name);
			intent.putExtra("location", location);
			intent.putExtra("phone", phone);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
