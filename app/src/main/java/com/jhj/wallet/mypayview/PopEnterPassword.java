package com.jhj.wallet.mypayview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jhjpay.zyb.R;
/**
 * 输入支付密码
 *
 * @author lining
 */
public class PopEnterPassword extends PopupWindow {

	private PoPasswordView pwdView;

	private View mMenuView;

	private Activity mContext;

	private TextView tv_name,tv_phone_number,textAmount;


	@SuppressLint("InflateParams")
	public PopEnterPassword(final Activity context,String name,String phone_number,String amount,final getPassWord getPassWord) {

		super(context);

		this.mContext = context;

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mMenuView = inflater.inflate(R.layout.pop_enter_password, null);

		tv_name=(TextView) mMenuView.findViewById(R.id.tv_name);
		tv_phone_number=(TextView) mMenuView.findViewById(R.id.tv_phone_number);
		textAmount=(TextView) mMenuView.findViewById(R.id.textAmount);
		if(TextUtils.isEmpty(name)){
			tv_name.setVisibility(View.GONE);
		}
		tv_name.setText(name);
		tv_phone_number.setText(phone_number);
		textAmount.setText("￥"+amount);

		pwdView = (PoPasswordView) mMenuView.findViewById(R.id.pwd_view);

		//添加密码输入完成的响应
		pwdView.setOnFinishInput(new OnPasswordInputFinish() {
			@Override
			public void inputFinish(final String password) {

				getPassWord.getPwd(password);
				dismiss();
			}

			@Override
			public void inputFinish() {
				// TODO Auto-generated method stub

			}
		});

		// 监听X关闭按钮
		pwdView.getImgCancel().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		// 监听键盘上方的返回
		pwdView.getVirtualKeyboardView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});



		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.pop_add_ainm);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x66000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

	}


}
