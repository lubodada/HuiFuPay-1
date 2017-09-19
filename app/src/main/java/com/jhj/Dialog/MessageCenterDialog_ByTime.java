package com.jhj.Dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;

import com.jhjpay.zyb.R;

/**
 * 自定义时间选择框
 * @author Administrator
 *
 */
public class MessageCenterDialog_ByTime extends Dialog {

	public MessageCenterDialog_ByTime(Context context, int theme) {
        super(context, theme);
    }
 
    public MessageCenterDialog_ByTime(Context context) {
        super(context,R.style.Dialog);
    }
 
    /**
     * 
     */
    public static class Builder implements  OnDateChangedListener,OnTimeChangedListener{
 
        private Context context;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private String dateTime;
    	private String initDateTime;
      //开始日期时间UI
    	private DatePicker DatePicker;
 
        private DialogInterface.OnClickListener 
                        positiveButtonClickListener,
                        negativeButtonClickListener;
 
        public Builder(Context context) {
            this.context = context;
        }
 
 
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
 
        public Builder setPositiveButton(int positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }
 
        public Builder setPositiveButton(String positiveButtonText,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }
 
        public Builder setNegativeButton(int negativeButtonText,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }
 
        public Builder setNegativeButton(String negativeButtonText,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }
 
        public void init(DatePicker datePicker)
    	{
    		Calendar calendar= Calendar.getInstance();
    		initDateTime=calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+
    				calendar.get(Calendar.DAY_OF_MONTH)+" "+
    				calendar.get(Calendar.HOUR_OF_DAY)+":"+
    				calendar.get(Calendar.MINUTE)+":"+
    				calendar.get(Calendar.SECOND);
    		datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), this);
    	}
        
        @SuppressLint("InflateParams")
		public MessageCenterDialog_ByTime create(final Button dateTimeTextEdite) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            final MessageCenterDialog_ByTime dialog = new MessageCenterDialog_ByTime(context, R.style.Dialog);
            
            dialog.setCanceledOnTouchOutside(false);
            View layout = inflater.inflate(R.layout.activity_dialog_wallet_message_bytime, null);
            dialog.addContentView(layout, new LayoutParams(
            		LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            
            DatePicker = (DatePicker) layout.findViewById(R.id.DatePicker);
			init(DatePicker);
			
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                	dateTimeTextEdite.setText(dateTime);
                                    positiveButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                	negativeButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            dialog.setContentView(layout);
            onDateChanged(null, 0, 0, 0);
            return dialog;
        }

		@Override
		public void onTimeChanged(android.widget.TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			onDateChanged(null, 0, 0, 0);
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onDateChanged(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			Calendar calendar = Calendar.getInstance();
			calendar.set(DatePicker.getYear(), DatePicker.getMonth(),
					DatePicker.getDayOfMonth());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			dateTime=sdf.format(calendar.getTime());
//			builder.setTitle(dateTime);
		}
 
    }
}
