package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 
 */
public class ScrollViewMoveNoConflict extends ScrollView {
	// 滑动距离及坐标
	private float xDistance, yDistance, xLast, yLast;

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	//
	// switch (ev.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// xDistance = yDistance = 0f;
	// xLast = ev.getX();
	// yLast = ev.getY();
	// break;
	// case MotionEvent.ACTION_MOVE:
	// final float curX = ev.getX();
	// final float curY = ev.getY();
	//
	// xDistance += Math.abs(curX - xLast);
	// yDistance += Math.abs(curY - yLast);
	// xLast = curX;
	// yLast = curY;
	//
	// if (xDistance > yDistance) {
	// return false;
	// }
	//
	// }
	// return super.onInterceptTouchEvent(ev);
	// }
	private GestureDetector mGestureDetector;

	public ScrollViewMoveNoConflict(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			/**
			 * 如果我们滚动更接近水平方向,返回false,让子视图来处理它
			 */
			return (Math.abs(distanceY) > Math.abs(distanceX));
		}
	}
}
