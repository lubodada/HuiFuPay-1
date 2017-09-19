package com.jhj.Function;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jhj.Dialog.YWLoadingDialog;
import com.jhjpay.zyb.R;

/**
 * 产品
 * @author lb
 */
public class ProductActivity extends Activity {

	private final static int LOAD_SUC_FINISH = -1;
	private ImageButton img_back;
	private ListView listview_product;
	private LinearLayout ll_noproduct;
	private YWLoadingDialog mDialog;
	private ProductAdapter productAdapter;
	private List<Product> productlist = new ArrayList<Product>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_product);
		mDialog = new YWLoadingDialog(this);
		img_back=(ImageButton) findViewById(R.id.img_back);
		img_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ProductActivity.this.finish();
			}
		});

		ll_noproduct=(LinearLayout) findViewById(R.id.ll_noproduct);
		listview_product=(ListView) findViewById(R.id.listview_product);
		mDialog.show();
		getProductList();
		if(productlist.size()!=0){
			listview_product.setVisibility(View.VISIBLE);
			productAdapter = new ProductAdapter(ProductActivity.this,productlist);
			listview_product.setAdapter(productAdapter);
			mDialog.dismiss();
		}
	}

	/**
	 * 模拟添加产品数据
	 */
	public void getProductList(){
		Product product = new Product();
		product.setProduct_imgUrl("http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg");
		product.setProduct_name("翼开店");
		product.setProduct_synopsis("翼开店是一款便于支付的应用平台哈哈哈哈哈哈哈哈");
		product.setProduct_state(true);
		productlist.add(product);
		
		Product product1 = new Product();
		product1.setProduct_imgUrl("http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg");
		product1.setProduct_name("翼开店");
		product1.setProduct_synopsis("翼开店是一款便于支付的应用平台哈哈哈哈哈哈哈哈");
		product1.setProduct_state(true);
		productlist.add(product1);
		
		Product product2 = new Product();
		product2.setProduct_imgUrl("http://pic18.nipic.com/20111215/577405_080531548148_2.jpg");
		product2.setProduct_name("翼开店");
		product2.setProduct_synopsis("翼开店是一款便于支付的应用平台哈哈哈哈哈哈哈哈");
		product2.setProduct_state(true);
		productlist.add(product2);
		
		Product product3 = new Product();
		product3.setProduct_imgUrl("http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg");
		product3.setProduct_name("翼开店");
		product3.setProduct_synopsis("翼开店是一款便于支付的应用平台哈哈哈哈哈哈哈哈");
		product3.setProduct_state(true);
		productlist.add(product3);
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_SUC_FINISH:
				mDialog.dismiss();
				ll_noproduct.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		};
	};
}
