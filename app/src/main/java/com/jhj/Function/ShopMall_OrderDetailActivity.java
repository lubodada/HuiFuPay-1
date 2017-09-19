package com.jhj.Function;

import com.jhjpay.zyb.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 订单详情
 * @author lb
 */
public class ShopMall_OrderDetailActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private TextView tv_order_number,tv_order_state,tv_card_type,tv_card_number,
	tv_order_money,tv_name,tv_location,tv_phone;
	private Button btn_pay_quick,btn_pay_weixin,btn_cancel_order;
	private LinearLayout ll_pay;
	private String order_number,order_state,card_type,
	card_number,order_money,name,location,phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_shopmall_orderdetail);

		initView();
		initData();
	}
	private void initView() {
		// TODO Auto-generated method stub
		img_back = (ImageButton) findViewById(R.id.img_back);
		tv_order_number = (TextView) findViewById(R.id.tv_order_number);
		tv_order_state = (TextView) findViewById(R.id.tv_order_state);
		tv_card_type = (TextView) findViewById(R.id.tv_card_type);
		tv_card_number = (TextView) findViewById(R.id.tv_card_number);
		tv_order_money = (TextView) findViewById(R.id.tv_order_money);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		btn_pay_quick = (Button) findViewById(R.id.btn_pay_quick);
		btn_pay_weixin = (Button) findViewById(R.id.btn_pay_weixin);
		btn_cancel_order = (Button) findViewById(R.id.btn_cancel_order);
		ll_pay = (LinearLayout) findViewById(R.id.ll_pay);

		img_back.setOnClickListener(this);
		btn_pay_quick.setOnClickListener(this);
		btn_pay_weixin.setOnClickListener(this);
		btn_cancel_order.setOnClickListener(this);
	}
	private void initData() {
		// TODO Auto-generated method stub
		order_number = getIntent().getStringExtra("order_number");
		order_state = getIntent().getStringExtra("order_state");
		card_type = getIntent().getStringExtra("card_type");
		card_number = getIntent().getStringExtra("number");
		order_money = getIntent().getStringExtra("total_money");
		name = getIntent().getStringExtra("name");
		location = getIntent().getStringExtra("location");
		phone = getIntent().getStringExtra("phone");

		
		tv_order_number.setText(order_number);
		tv_order_state.setText(getState(order_state));
		tv_card_type.setText(card_type);
		tv_card_number.setText(card_number);
		tv_order_money.setText(order_money+"元");
		tv_name.setText(name);
		tv_location.setText(location);
		tv_phone.setText(phone);
		
		if(!order_state.equals("1")){
			ll_pay.setVisibility(View.GONE);
			btn_cancel_order.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 订单状态
	 * @param state
	 * @return
	 */
	public String getState(String state){
		if(state.equals("1")){
			state = "待付款";
		}
		if(state.equals("2")){
			state = "已付款";
		}
		if(state.equals("3")){
			state = "已取消";
		}
		return state;
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			ShopMall_OrderDetailActivity.this.finish();
			break;
		case R.id.btn_pay_quick://快捷支付

			break;
		case R.id.btn_pay_weixin://微信支付

			break;
		case R.id.btn_cancel_order://取消订单

			break;

		default:
			break;
		}
	}
}
