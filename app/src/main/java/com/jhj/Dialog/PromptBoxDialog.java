package com.jhj.Dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.jhjpay.zyb.R;

/**
 * 自定义信息提示弹框
 * @author Administrator
 *
 */
public class PromptBoxDialog extends Dialog implements android.view.View.OnClickListener{
	
	private Window window = null;  
	private TextView title;
	private Button determine;  
    private View loadingview;
    private String texttitle;
    public PromptBoxDialog(Context context, boolean cancelable,  
                    DialogInterface.OnCancelListener cancelListener) {  
        super(context, cancelable, cancelListener);  
    }  
    public PromptBoxDialog(Context context,String title) {  
        super(context, R.style.edit_AlertDialog_style);  
        windowDeploy(0, 0);  
        texttitle = title;  
  
    }  
    public PromptBoxDialog(Context context) {  
        super(context);  
    }  
  
    @SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {  
        //初始化布局  
        loadingview= LayoutInflater.from(getContext()).inflate(R.layout.activity_qrcode_dialog_prompt,null);  
        title = (TextView) loadingview.findViewById(R.id.title);
		determine = (Button) loadingview.findViewById(R.id.determine);
		title.setText(texttitle);  
		determine.setOnClickListener(this);
        //设置dialog的布局  
        setContentView(loadingview);  
        super.onCreate(savedInstanceState);  
    }  
  
    //设置窗口显示  
    public void windowDeploy(int x, int y){  
        window = getWindow(); //得到对话框  
        WindowManager.LayoutParams wl = window.getAttributes();  
        //根据x，y坐标设置窗口需要显示的位置  
        wl.x = x; //x小于0左移，大于0右移  
        wl.y = y; //y小于0上移，大于0下移  
//        wl.alpha = 0.6f; //设置透明度  
        wl.gravity = Gravity.CENTER; //设置重力  
        window.setAttributes(wl);  
    }  
  
    public void show() {  
        //设置触摸对话框意外的地方取消对话框  
        setCanceledOnTouchOutside(false);  
        super.show();  
    }  
    public void dismiss() {  
        super.dismiss();  
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.determine:
			dismiss();
			break;

		default:
			break;
		}
	}
}
