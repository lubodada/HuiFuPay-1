package com.jhj.Function;

import com.jhjpay.zyb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 商城
 * @author lb
 */
public class ShopMallActivity extends Activity implements OnClickListener{

	private ImageButton img_back;
	private TextView tv_order,tv_card_money;
	private ImageView img_cardfront,img_cardback;
	private Button btn_buy;
	private String unit_price = "300.0"; 
	private String imgUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_shopping_mall);
		
		img_back=(ImageButton) findViewById(R.id.img_back);
		tv_order=(TextView) findViewById(R.id.tv_order);
		tv_card_money=(TextView) findViewById(R.id.tv_card_money);
		img_cardfront=(ImageView) findViewById(R.id.img_cardfront);
		img_cardback=(ImageView) findViewById(R.id.img_cardback);
		btn_buy=(Button) findViewById(R.id.btn_buy);
		
		img_back.setOnClickListener(this);
		tv_order.setOnClickListener(this);
		btn_buy.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_back:
			ShopMallActivity.this.finish();
			break;
		case R.id.tv_order://订单
			Intent orderintent = new Intent(ShopMallActivity.this,ShopMall_OrderActivity.class);
			startActivity(orderintent);
			break;
		case R.id.btn_buy://购买
			Intent intent = new Intent(ShopMallActivity.this,ShopMall_OrderLocationActivity.class);
			intent.putExtra("unit_price", unit_price);
			intent.putExtra("imgUrl", imgUrl);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
