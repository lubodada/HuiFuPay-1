package com.jhj.Dialog;

import com.jhjpay.zyb.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
/**
 * 加载大图dialog
 * @author lb
 *
 */
public class BigHeadImage_Dialog extends Dialog implements android.view.View.OnClickListener{

	
	private Window window = null;  
    private ImageView iv;  
    private Bitmap bms;  
    private View loadingview;
    public BigHeadImage_Dialog(Context context, boolean cancelable,  
                    DialogInterface.OnCancelListener cancelListener) {  
        super(context, cancelable, cancelListener);  
    }  
    public BigHeadImage_Dialog(Context context, int cancelable,int x,int y,Bitmap bm) {  
        super(context, cancelable);  
        windowDeploy(x, y);  
        bms = bm;  
  
    }  
    public BigHeadImage_Dialog(Context context) {  
        super(context);  
    }  
  
    protected void onCreate(Bundle savedInstanceState) {  
        //初始化布局  
        loadingview= LayoutInflater.from(getContext()).inflate(R.layout.activity_dialog_bigimg_headportrait,null);  
        iv=(ImageView) loadingview.findViewById(R.id.imageview_head_big); 
        iv.setOnClickListener(this);
        iv.setImageBitmap(bms);  
        //设置dialog的布局  
        setContentView(loadingview);  
        //如果需要放大或者缩小时的动画，可以直接在此出对loadingview或iv操作，在下面SHOW或者dismiss中操作  
        super.onCreate(savedInstanceState);  
    }  
  
    //设置窗口显示  
    public void windowDeploy(int x, int y){  
        window = getWindow(); //得到对话框  
        window.setWindowAnimations(R.style.popupwindow_anim_style); //设置窗口弹出动画  
        window.setBackgroundDrawableResource(R.color.vifrification); //设置对话框背景为透明  
        WindowManager.LayoutParams wl = window.getAttributes();  
        //根据x，y坐标设置窗口需要显示的位置  
        wl.x = x; //x小于0左移，大于0右移  
        wl.y = y; //y小于0上移，大于0下移  
//            wl.alpha = 0.6f; //设置透明度  
//            wl.gravity = Gravity.BOTTOM; //设置重力  
        window.setAttributes(wl);  
    }  
  
    public void show() {  
        //设置触摸对话框意外的地方取消对话框  
        setCanceledOnTouchOutside(true);  
        super.show();  
    }  
    public void dismiss() {  
        super.dismiss();  
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageview_head_big:
			dismiss();
			break;

		default:
			break;
		}
	}

    /**
     *
     */
	public void getid(){

    }
	
}
