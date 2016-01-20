package com.example.sortlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#" };
	private int choose = -1;// 选中
	private Paint paint = new Paint();

	private TextView mTextDialog;
	private float textSize;
	private int normalTextColor;
	private int selectedTextColor;
	private int selectedBgResId;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initValues(14, Color.rgb(33, 65, 98), Color.parseColor("#3399ff"), R.drawable.sidebar_background);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initValues(14, Color.rgb(33, 65, 98), Color.parseColor("#3399ff"), R.drawable.sidebar_background);
	}

	public SideBar(Context context) {
		super(context);
		initValues(14, Color.rgb(33, 65, 98), Color.parseColor("#3399ff"), R.drawable.sidebar_background);
	}

	/**
	 * 
	 * @param textSize
	 *            字体大小
	 * @param normalTextColor
	 *            默认字体颜色
	 * @param selectedTextColor
	 *            选中字体颜色
	 * @param selectedBgColor
	 *            选中文字背景
	 */
	public void initValues(float textSize, int normalTextColor, int selectedTextColor, int selectedBgResId) {
		setTextSize(textSize);
		this.normalTextColor = normalTextColor;
		this.selectedTextColor = selectedTextColor;
		this.selectedBgResId = selectedBgResId;
	}

	public void setTextSize(float textSize) {
		this.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize,
				getResources().getDisplayMetrics());
	}

	public float getTextSize() {
		return textSize;
	}

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取焦点改变背景颜色.
		int height = getHeight();// 获取对应高度
		int width = getWidth(); // 获取对应宽度
		int singleHeight = height / b.length;// 获取每一个字母的高度
		for (int i = 0; i < b.length; i++) {
			paint.setColor(normalTextColor);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(textSize);
			// 选中的状态
			if (i == choose) {
				paint.setColor(selectedTextColor);
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();// 重置画笔
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

		switch (action) {
		case MotionEvent.ACTION_UP:
			setBackgroundColor(Color.TRANSPARENT);
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;

		default:
			setBackgroundResource(selectedBgResId);
			if (oldChoose != c) {
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					if (mTextDialog != null) {
						mTextDialog.setText(b[c]);
						mTextDialog.setVisibility(View.VISIBLE);
					}

					choose = c;
					invalidate();
				}
			}

			break;
		}
		return true;
	}

	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}