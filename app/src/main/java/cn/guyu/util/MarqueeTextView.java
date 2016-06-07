package cn.guyu.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

//跑马灯效果(自定义UI控件)
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context) {

		super(context);
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	public boolean isFocused() {

		return true;
	}

}
